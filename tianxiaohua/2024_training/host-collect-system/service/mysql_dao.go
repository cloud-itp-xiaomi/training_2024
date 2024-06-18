package service

import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
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
