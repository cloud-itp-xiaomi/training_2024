package handler

import (
	"encoding/json"
	"log"
	"net/http"
	"server/databaseOperate"
	"server/serverDataType"
)

func UploadHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		w.Header().Set("Content-Type", "application/json")
		response := serverDataType.Response{Code: http.StatusMethodNotAllowed, Message: "Invalid request method"}
		err := json.NewEncoder(w).Encode(response)
		if err != nil {
			log.Println("json encode err:", err)
			return
		}
		return
	}

	// 接收数据
	var data []serverDataType.MetricData
	if err := json.NewDecoder(r.Body).Decode(&data); err != nil {
		w.Header().Set("Content-Type", "application/json")
		response := serverDataType.Response{Code: http.StatusBadRequest, Message: "Invalid request body"}
		err := json.NewEncoder(w).Encode(response)
		if err != nil {
			log.Println("json encode err:", err)
			return
		}
		return
	}

	//写入数据
	for _, metric := range data {
		log.Println("metric:", metric.Metric, ", endpoint:", metric.Endpoint, ", timestamp:", metric.Timestamp, ", step:", metric.Step, ", value:", metric.Value)
		if err := databaseOperate.InsertMetricToMySQL(metric); err != nil {
			continue
		}
		if err := databaseOperate.InsertMetricToRedis(metric); err != nil {
			continue
		}
	}

	w.Header().Set("Content-Type", "application/json")
	response := serverDataType.Response{Code: http.StatusOK, Message: "ok"}
	err := json.NewEncoder(w).Encode(response)
	if err != nil {
		return
	}
}

func QueryHandler(w http.ResponseWriter, r *http.Request) {
	params := []string{"metric", "endpoint", "start_ts", "end_ts"}
	queryParams := make(map[string]string)

	for _, param := range params {
		value := r.URL.Query().Get(param)
		queryParams[param] = value
	}

	var err error
	startTs, endTs, metric, endpoint := ConfirmTs(queryParams, w)

	// 读取redis数据并获得最早时间戳
	redisMetrics, lastRedisTs, err := databaseOperate.QueryMetricsFromRedis(metric, endpoint, startTs, endTs)
	if err != nil {
		response := serverDataType.QueryResponse{
			Code:    http.StatusInternalServerError,
			Message: "redis query error!",
			Data:    nil,
		}
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusInternalServerError)
		err := json.NewEncoder(w).Encode(response)
		if err != nil {
			log.Println("json encode err:", err)
			return
		}
		return
	}

	//如果redis中没有数据，则将最后的时间戳设置为输入的end_ts
	if redisMetrics == nil {
		lastRedisTs = endTs
	}
	// redis之外的在mysql读取
	var mysqlMetrics []serverDataType.MetricData
	if lastRedisTs > startTs {
		mysqlMetrics, err = databaseOperate.QueryMetricsFromMySQL(metric, endpoint, startTs, lastRedisTs-1) //避免有边界数据与redis左边界重复
		if err != nil {
			response := serverDataType.QueryResponse{
				Code:    http.StatusInternalServerError,
				Message: "mysql query error!",
				Data:    nil,
			}
			w.Header().Set("Content-Type", "application/json")
			w.WriteHeader(http.StatusInternalServerError)
			err := json.NewEncoder(w).Encode(response)
			if err != nil {
				log.Println("json encode err:", err)
				return
			}
			return
		}
	}

	combinedMetrics := append(mysqlMetrics, redisMetrics...)

	response := serverDataType.QueryResponse{
		Code:    http.StatusOK,
		Message: "ok",
		Data:    combinedMetrics,
	}

	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(response); err != nil {
		http.Error(w, "Error encoding response", http.StatusInternalServerError)
	}
}
