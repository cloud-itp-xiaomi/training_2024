package server

import (
	"encoding/json"
	"net/http"
	"sync"

	"github.com/gorilla/mux"
)

// 日志条目结构体
type LogEntry struct {
	Hostname string   `json:"hostname"`
	FilePath string   `json:"file"`
	Logs     []string `json:"logs"`
}

var (
	// logStorage 用于存储日志条目
	logStorage = make(map[string][]LogEntry)
	mutex      sync.Mutex
)

// uploadLogs 处理日志上传请求
// 其中使用互斥锁mutex来保证logStorage读写安全
func uploadLogs(responseWriter http.ResponseWriter, request *http.Request) {
	var logEntries []LogEntry
	err := json.NewDecoder(request.Body).Decode(&logEntries)
	if err != nil {
		http.Error(responseWriter, err.Error(), http.StatusBadRequest)
		return
	}

	mutex.Lock()
	defer mutex.Unlock()
	for _, entry := range logEntries {
		logStorage[entry.Hostname] = append(logStorage[entry.Hostname], entry)
	}

	responseWriter.WriteHeader(http.StatusOK)
	json.NewEncoder(responseWriter).Encode(map[string]string{"message": "ok"})
}

// queryLogs 处理日志查询请求
// 其中使用互斥锁mutex来保证logStorage访问安全
func queryLogs(responseWriter http.ResponseWriter, request *http.Request) {
	hostname := request.URL.Query().Get("hostname")
	if hostname == "" {
		http.Error(responseWriter, "必须提供主机名查询参数", http.StatusBadRequest)
		return
	}

	mutex.Lock()
	defer mutex.Unlock()
	logs, found := logStorage[hostname]
	if !found {
		http.Error(responseWriter, "未找到指定主机名的日志", http.StatusNotFound)
		return
	}

	responseWriter.WriteHeader(http.StatusOK)
	json.NewEncoder(responseWriter).Encode(logs)
}

func StartServer(address string) {
	router := mux.NewRouter()
	router.HandleFunc("/api/log/upload", uploadLogs).Methods("POST")
	router.HandleFunc("/api/logs", queryLogs).Methods("GET")

	http.ListenAndServe(address, router)
}
