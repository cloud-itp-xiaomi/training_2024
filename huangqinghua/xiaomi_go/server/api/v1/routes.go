package v1

import (
	"encoding/json"
	"fmt"
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"net/url"
	"server/internal/model"
	"server/internal/repository"
	"server/internal/server"
	"strconv"
)

// QueryParams 定义查询参数的结构体
type QueryParams struct {
	Endpoint string `json:"endpoint"`
	Metric   string `json:"metric"`
	StartTs  int64  `json:"start_ts"`
	EndTs    int64  `json:"end_ts"`
}

func NewRouter(srv *server.Server) *mux.Router {
	router := mux.NewRouter()

	// 配置路由
	router.HandleFunc("/api/metric/upload", insertMetric).Methods("POST")
	router.HandleFunc("/api/metric/query", getMetric).Methods("GET")

	return router
}

// 上报指标数据接口
func insertMetric(writer http.ResponseWriter, request *http.Request) {
	var data map[string]interface{}

	// 解析请求体中的 JSON 数据
	err := json.NewDecoder(request.Body).Decode(&data)
	if err != nil {
		http.Error(writer, "无法解析请求数据", http.StatusBadRequest)
		return
	}

	// 插入数据
	err = repository.InsertMetric(data)
	if err != nil {
		http.Error(writer, "插入数据失败", http.StatusBadRequest)
		return
	}

	// 回报结果
	response := model.Result{
		Code: http.StatusOK,
		Msg:  "ok",
		Data: nil,
	}
	writer.WriteHeader(http.StatusOK)
	err = json.NewEncoder(writer).Encode(response)
	if err != nil {
		log.Printf("响应写入失败: %v\n", err)
		return
	}

	fmt.Printf("指标数据已插入: \n")
	fmt.Println(data)
}

// 查询接口
func getMetric(writer http.ResponseWriter, request *http.Request) {
	queryValues := request.URL.Query()
	fmt.Println(queryValues)
	params, err := getQueryParams(queryValues)
	if err != nil {
		http.Error(writer, err.Error(), http.StatusBadRequest)
	}

	// 查询
	var metricDataList []model.MetricData
	metricDataList, err = repository.GetMetrics(params.Endpoint, params.Metric, params.StartTs, params.EndTs)

	if err != nil {
		log.Printf("查询失败: %v\n", err)
		http.Error(writer, "查询失败", http.StatusInternalServerError)
		return
	}

	// 回报结果
	response := model.Result{
		Code: http.StatusOK,
		Msg:  "ok",
		Data: groupByMetric(metricDataList),
	}

	writer.WriteHeader(http.StatusOK)
	err = json.NewEncoder(writer).Encode(response)
	if err != nil {
		log.Printf("响应写入失败: %v\n", err)
		return
	}
}

// getQueryParams 从请求中获取查询参数并转换为结构体
func getQueryParams(queryValues url.Values) (*QueryParams, error) {
	endpoint := queryValues.Get("endpoint")
	if endpoint == "" {
		return nil, fmt.Errorf("缺少 endpoint 参数")
	}

	// 检查是否传入了 metric 参数，如果未传入则设置为默认值
	var metric string
	if metricValues, ok := queryValues["metric"]; ok && len(metricValues) > 0 {
		metric = metricValues[0]
	} else {
		metric = ""
	}

	startTsStr := queryValues.Get("start_ts")
	if startTsStr == "" {
		return nil, fmt.Errorf("缺少 start_ts 参数")
	}
	startTs, err := strconv.ParseInt(startTsStr, 10, 64)
	if err != nil {
		return nil, fmt.Errorf("无效的 start_ts 参数")
	}

	endTsStr := queryValues.Get("end_ts")
	if endTsStr == "" {
		return nil, fmt.Errorf("缺少 end_ts 参数")
	}
	endTs, err := strconv.ParseInt(endTsStr, 10, 64)
	if err != nil {
		return nil, fmt.Errorf("无效的 end_ts 参数")
	}

	return &QueryParams{
		Endpoint: endpoint,
		Metric:   metric,
		StartTs:  startTs,
		EndTs:    endTs,
	}, nil
}

// groupByMetric 封装回报的指标类，按照指标分组
func groupByMetric(metricDataList []model.MetricData) []model.MetricResponse {
	groupedByMetric := make(map[string][]model.ValueData)

	// 遍历metricData数组
	for _, data := range metricDataList {
		groupedByMetric[data.Metric] = append(groupedByMetric[data.Metric], model.ValueData{
			Timestamp: data.Timestamp,
			Value:     data.Value,
		})
	}

	var metricResponseList []model.MetricResponse
	// 遍历map
	for metric, data := range groupedByMetric {
		metricResponseList = append(metricResponseList, model.MetricResponse{
			Metric: metric,
			Data:   data,
		})
	}

	return metricResponseList
}
