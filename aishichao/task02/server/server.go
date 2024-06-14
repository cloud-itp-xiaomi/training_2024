package main

import (
	"database/sql"
	"fmt"
	"log"
	"net/http"
	"server/handler"
	"server/storageOperate"
)

func main() {
	fmt.Println("Starting server...")

	storageOperate.InitMySQL("root:123456@tcp(mysql:3306)/task")
	defer func(MysqlDatabase *sql.DB) {
		err := MysqlDatabase.Close()
		if err != nil {
			log.Println("close mysql error:", err)
		}
	}(storageOperate.MysqlDatabase)

	http.HandleFunc("/api/log/upload", handler.UploadHandler)
	http.HandleFunc("/api/log/query", handler.QueryHandler)

	log.Fatal(http.ListenAndServe(":8080", nil))
}
