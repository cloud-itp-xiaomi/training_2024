package storageOperate

import (
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"log"
	"server/serverDataType"
)

var MysqlDatabase *sql.DB

func InitMySQL(dsn string) {
	var err error
	MysqlDatabase, err = sql.Open("mysql", dsn)
	if err != nil {
		log.Fatal("Error connecting to MySQL:", err)
	}
}

func InsertLogsIntoMySQL(logs serverDataType.LogInformation) error {
	exist, hostAndFileId, err := FileLogsExist(logs.Hostname, logs.File)
	if err != nil {
		return err
	}
	//如果文件未存入，则需要存入表hosts_logs
	if !exist {
		result, err := MysqlDatabase.Exec("INSERT INTO hosts_logs (hostname, file) VALUES (?, ?)",
			logs.Hostname, logs.File)
		if err != nil {
			log.Printf("Error inserting data into hosts_logs: %v", err)
			return err
		}
		hostAndFileId, err = result.LastInsertId()
		if err != nil {
			log.Printf("Error getting last insert id from hosts_logs: %v", err)
			return err
		}
	}

	for _, logEntry := range logs.Logs {
		fmt.Println("插入：", hostAndFileId, logEntry)
		_, err := MysqlDatabase.Exec("INSERT INTO log_entries (host_log_id, log_entry) VALUES (?, ?)",
			hostAndFileId, logEntry)
		if err != nil {
			log.Printf("Error inserting data into log_entries: %v", err)
		}
	}
	return err
}

func QueryLogsFromMySQL(hostname string, file string) ([]serverDataType.LogInformation, error) {
	var query string
	var rows *sql.Rows
	var err error

	if hostname == "" && file == "" {
		query = "SELECT hl.hostname, hl.file, le.log_entry FROM hosts_logs hl JOIN log_entries le ON hl.id = le.host_log_id"
		rows, err = MysqlDatabase.Query(query)
	} else if hostname != "" && file == "" {
		query = "SELECT hl.hostname, hl.file, le.log_entry FROM hosts_logs hl JOIN log_entries le ON hl.id = le.host_log_id WHERE hl.hostname = ?"
		rows, err = MysqlDatabase.Query(query, hostname)
	} else if hostname == "" {
		query = "SELECT hl.hostname, hl.file, le.log_entry FROM hosts_logs hl JOIN log_entries le ON hl.id = le.host_log_id WHERE hl.file = ?"
		rows, err = MysqlDatabase.Query(query, file)
	} else {
		query = "SELECT hl.hostname, hl.file, le.log_entry FROM hosts_logs hl JOIN log_entries le ON hl.id = le.host_log_id WHERE hl.hostname = ? AND h1.file = ?"
		rows, err = MysqlDatabase.Query(query, hostname, file)
	}

	if err != nil {
		return nil, err
	}
	defer func(rows *sql.Rows) {
		err := rows.Close()
		if err != nil {
			log.Printf("Error querying MySQL rows: %v", err)
		}
	}(rows)

	var logsInformation []serverDataType.LogInformation
	for rows.Next() {
		var hostname string
		var file string
		var logEntry string

		if err := rows.Scan(&hostname, &file, &logEntry); err != nil {
			log.Printf("Error scanning MySQL row: %v", err)
			continue
		}

		logsInformation = append(logsInformation, serverDataType.LogInformation{
			Hostname: hostname,
			File:     file,
			Logs:     []string{logEntry},
		})
	}
	return logsInformation, nil
}

func FileLogsExist(hostname, file string) (bool, int64, error) {
	var id int64
	query := "SELECT id FROM hosts_logs WHERE hostname = ? AND file = ?"
	err := MysqlDatabase.QueryRow(query, hostname, file).Scan(&id)
	if err != nil && err != sql.ErrNoRows {
		log.Println("error checking host_log existence: %w", err)
		return false, 0, err
	}
	if err == sql.ErrNoRows {
		return false, 0, nil
	}
	return true, id, nil
}
