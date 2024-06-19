package service

import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
	"strings"
)

func insertUtilization(utilization Utilization, db *sql.DB) {
	stmtIns, err := db.Prepare("INSERT INTO utilization(metric, endpoint, collect_time, value_metric) VALUES( ?, ?, ?, ?)")
	if err != nil {
		panic(err.Error())
	}
	defer stmtIns.Close()

	_, err = stmtIns.Exec(utilization.Metric, utilization.Endpoint, utilization.CollectTime, utilization.Value)
	if err != nil {
		panic(err.Error())
	}
}

func queryUtilization(metric string, endpoint string, start, end int64, db *sql.DB) []Utilization {
	stmtOut, err := db.Prepare("SELECT collect_time, value_metric FROM utilization WHERE metric = ? and endpoint = ? and collect_time between ? and ?")
	if err != nil {
		panic(err.Error())
	}
	defer stmtOut.Close()

	var utilizations []Utilization

	rows, err := stmtOut.Query(metric, endpoint, start, end)
	if err != nil {
		panic(err.Error())
	}
	for rows.Next() {
		var utilization Utilization
		err := rows.Scan(&utilization.CollectTime, &utilization.Value)
		if err != nil {
			panic(err.Error())
		}
		utilizations = append(utilizations, utilization)
	}

	return utilizations
}

func insertLog(log Log, db *sql.DB) {
	stmtIns, err := db.Prepare("INSERT INTO log(hostname, file, logs, last_update_time) VALUES( ?, ?, ?, ?)")
	if err != nil {
		panic(err.Error())
	}
	defer stmtIns.Close()

	logs := strings.Join(log.Logs, "|")

	_, err = stmtIns.Exec(log.Hostname, log.File, logs, log.FileLastUpdateTime)
	if err != nil {
		panic(err.Error())
	}
}

func queryLog(hostname string, file string, db *sql.DB) Log {
	stmtOut, err := db.Prepare("SELECT logs, last_update_time FROM log WHERE hostname = ? and file = ?")
	if err != nil {
		panic(err.Error())
	}
	defer stmtOut.Close()

	var logs string
	var timestamp int64

	err = stmtOut.QueryRow(hostname, file).Scan(&logs, &timestamp)
	if err != nil {
		//panic(err.Error())
		return Log{}
	}

	logs1 := strings.Split(logs, "|")

	log := Log{
		Hostname:           hostname,
		File:               file,
		Logs:               logs1,
		FileLastUpdateTime: timestamp,
	}

	return log
}

func updateLog(log Log, db *sql.DB) {
	stmtIns, err := db.Prepare("UPDATE log SET logs = ?, last_update_time = ? WHERE hostname = ? and file = ?")
	if err != nil {
		panic(err.Error())
	}
	defer stmtIns.Close()

	logs := strings.Join(log.Logs, "|")

	_, err = stmtIns.Exec(logs, log.FileLastUpdateTime, log.Hostname, log.File)
	if err != nil {
		panic(err.Error())
	}
}
