package handler

import (
    "encoding/json"
    "fmt"
    "net/http"
    "server/db"
    "server/models"
    "strconv"
)

func UploadHandler(w http.ResponseWriter, r *http.Request) {
    if r.Method != http.MethodPost {
        http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
        return
    }

    //接收数据
    var data []models.MetricData
    if err := json.NewDecoder(r.Body).Decode(&data); err != nil {
        http.Error(w, "Invalid request body", http.StatusBadRequest)
        return
    }

    //写入数据
    for _, metric := range data {
        if err := db.InsertMetric(metric); err != nil {
            continue
        }
        if err := db.InsertMetricToRedis(metric); err != nil {
            continue
        }
    }

    w.WriteHeader(http.StatusOK)
}

func QueryHandler(w http.ResponseWriter, r *http.Request) {
    //使用map集合存储获取的查询参数
    params := []string{"metric", "endpoint", "start_ts", "end_ts"}
    queryParams := make(map[string]string)

    for _, param := range params {
        value := r.URL.Query().Get(param)
        if value == "" {
            http.Error(w, fmt.Sprintf("query error: missing parameter %s", param), http.StatusBadRequest)
            return
        }
        queryParams[param] = value
    }
     
    //格式转换
    startTs, err := strconv.ParseInt(queryParams["start_ts"], 10, 64)
    if err != nil {
        http.Error(w, "invalid start_ts", http.StatusBadRequest)
        return
    }
    endTs, err := strconv.ParseInt(queryParams["end_ts"], 10, 64)
    if err != nil {
        http.Error(w, "invalid end_ts", http.StatusBadRequest)
        return
    }

    metric := queryParams["metric"]
    endpoint := queryParams["endpoint"]
    
    //读取redis数据并获得最早时间戳
    redisMetrics, lastRedisTs, err := db.QueryMetricsFromRedis(metric, endpoint, startTs, endTs)
    if err != nil {
        http.Error(w, "redis query error!", http.StatusInternalServerError)
        return
    }
    
    //redis之外的在mysql读取
    var mysqlMetrics []models.MetricData
    if lastRedisTs > startTs {
        mysqlMetrics, err = db.QueryMetrics(metric, endpoint, startTs, lastRedisTs-1)//避免有边界数据与redis左边界重复
        if err != nil {
            http.Error(w, "mysql query error!", http.StatusInternalServerError)
            return
        }
    }

    combinedMetrics := append(mysqlMetrics, redisMetrics...)

    w.Header().Set("Content-Type", "application/json")
    if err := json.NewEncoder(w).Encode(combinedMetrics); err != nil {
        http.Error(w, "Error encoding response", http.StatusInternalServerError)
    }
}
