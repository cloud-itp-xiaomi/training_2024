package databaseOperate

import (
	"database/sql"
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

func InsertMetricToMySQL(metric serverDataType.MetricData) error {
	_, err := MysqlDatabase.Exec("INSERT INTO metric (Metric, Endpoint, Timestamp, Step, Value) VALUES (?, ?, ?, ?, ?)",
		metric.Metric, metric.Endpoint, metric.Timestamp, metric.Step, metric.Value)
	if err != nil {
		log.Printf("Error inserting data into MySQL: %v", err)
	}
	return err
}

func QueryMetricsFromMySQL(metric string, endpoint string, startTs int64, endTs int64) ([]serverDataType.MetricData, error) {
	var query string
	var rows *sql.Rows
	var err error

	//根据metric和endpoint是否为空决定查询逻辑
	if metric == "" {
		if endpoint == "" {
			query = "SELECT Metric, Endpoint, Timestamp, Step, Value FROM metric WHERE Timestamp BETWEEN ? AND ?"
			rows, err = MysqlDatabase.Query(query, startTs, endTs)
		} else {
			query = "SELECT Metric, Endpoint, Timestamp, Step, Value FROM metric WHERE Endpoint = ? AND Timestamp BETWEEN ? AND ?"
			rows, err = MysqlDatabase.Query(query, endpoint, startTs, endTs)
		}

	} else {
		if endpoint == "" {
			query = "SELECT Metric, Endpoint, Timestamp, Step, Value FROM metric WHERE Metric = ? AND Timestamp BETWEEN ? AND ?"
			rows, err = MysqlDatabase.Query(query, metric, startTs, endTs)
		} else {
			query = "SELECT Metric, Endpoint, Timestamp, Step, Value FROM metric WHERE Metric = ? AND Endpoint = ? AND Timestamp BETWEEN ? AND ?"
			rows, err = MysqlDatabase.Query(query, metric, endpoint, startTs, endTs)
		}
	}

	if err != nil {
		return nil, err
	}
	defer func(rows *sql.Rows) {
		err := rows.Close()
		if err != nil {
			log.Println("Error query data from mysql:", err)
		}
	}(rows)

	var metrics []serverDataType.MetricData

	for rows.Next() {
		var metricData serverDataType.MetricData
		if err := rows.Scan(&metricData.Metric, &metricData.Endpoint, &metricData.Timestamp, &metricData.Step, &metricData.Value); err != nil {
			log.Printf("Error scanning MySQL row: %v", err)
			continue
		}
		metrics = append(metrics, metricData)
	}
	return metrics, nil
}
