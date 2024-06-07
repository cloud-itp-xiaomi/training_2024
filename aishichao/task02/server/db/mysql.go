package db

import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
	"log"

	"server/models"
)

var DB *sql.DB

func InitMySQL(dsn string) {
	var err error
	DB, err = sql.Open("mysql", dsn)
	if err != nil {
		log.Fatal("Error connecting to MySQL:", err)
	}
}

func InsertLogs(logs models.LogInformation) error {
	result, err := DB.Exec("INSERT INTO hosts_logs (hostname,file) VALUES (?, ?)",
		logs.Hostname, logs.File)
	if err != nil {
		log.Printf("Error inserting data into host_logs: %v", err)
	}

	hostAndFileId, err := result.LastInsertId()
	if err != nil {
		return err
	}

	for _, logEntry := range logs.Logs {
		_, err = DB.Exec("INSERT INTO log_entries (host_log_id,log_entry) VALUES (?, ?)",
			hostAndFileId, logEntry)
		if err != nil {
			log.Printf("Error inserting data into log_entries: %v", err)
		}
	}
	return err
}

func QueryLogs(metric string, endpoint string, startTs int64, endTs int64) ([]models.LogInformation, error) {
	query := "SELECT Metric, Endpoint, Timestamp, Step, Value FROM metric WHERE Metric = ? AND Endpoint = ? AND Timestamp BETWEEN ? AND ?"
	rows, err := DB.Query(query, metric, endpoint, startTs, endTs)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var logsInformation []models.LogInformation
	for rows.Next() {
		var logs models.LogInformation
		if err := rows.Scan(&logs.Hostname, &logs.File, &logs.Logs); err != nil {
			log.Printf("Error scanning MySQL row: %v", err)
			continue
		}
		logsInformation = append(logsInformation, logs)
	}
	return logsInformation, nil
}
