package db

import (
    "log"
    "database/sql"
    _ "github.com/go-sql-driver/mysql"
    
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

func InsertMetric(metric models.MetricData) error {
    _, err := DB.Exec("INSERT INTO metric (Metric, Endpoint, Timestamp, Step, Value) VALUES (?, ?, ?, ?, ?)",
        metric.Metric, metric.Endpoint, metric.Timestamp, metric.Step, metric.Value)
    if err != nil {
        log.Printf("Error inserting data into MySQL: %v", err)
    }
    return err
}

func QueryMetrics(metric string, endpoint string, startTs int64, endTs int64) ([]models.MetricData, error) {
    query := "SELECT Metric, Endpoint, Timestamp, Step, Value FROM metric WHERE Metric = ? AND Endpoint = ? AND Timestamp BETWEEN ? AND ?"
    rows, err := DB.Query(query, metric, endpoint, startTs, endTs)
    if err != nil {
        return nil, err
    }
    defer rows.Close()

    var metrics []models.MetricData
    for rows.Next() {
        var metricData models.MetricData
        if err := rows.Scan(&metricData.Metric, &metricData.Endpoint, &metricData.Timestamp, &metricData.Step, &metricData.Value); err != nil {
            log.Printf("Error scanning MySQL row: %v", err)
            continue
        }
        metrics = append(metrics, metricData)
    }
    return metrics, nil
}

