package handler

import (
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"path/filepath"
	"server/models"
	"server/storageOperate"
)

func UploadHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Invalid request method", http.StatusMethodNotAllowed)
		return
	}

	//接收数据
	var data models.LogPost
	if err := json.NewDecoder(r.Body).Decode(&data); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}
	switch data.LogStorage {
	case "local_file":
		//写入数据
		dir := "/logs" //映射到localLogs文件夹
		filename := "logs.json"
		Filepath := filepath.Join(dir, filename)
		file, err := os.Create(Filepath)
		if err != nil {
			fmt.Println("create logs.json error:", err)
			return
		}
		defer func(file *os.File) {
			err := file.Close()
			if err != nil {
				fmt.Println("close log.txt error:", err)
			}
		}(file)

		logEncoder := json.NewEncoder(file)
		logEncoder.SetIndent("", " ") //格式化输出
		if err := logEncoder.Encode(&data.LogInformation); err != nil {
			fmt.Println("encode log.txt error:", err)
		}

	case "mysql":
		//写入数据
		for _, log := range data.LogInformation {
			if err := storageOperate.InsertLogs(log); err != nil {
				continue
			}
		}
	default:
		fmt.Println("unknown storage type:", data.LogStorage)

	}
	w.WriteHeader(http.StatusOK)
}

func QueryHandler(w http.ResponseWriter, r *http.Request) {

}
