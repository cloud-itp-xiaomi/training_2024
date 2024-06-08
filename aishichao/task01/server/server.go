package main

import (
	"database/sql"
	"fmt"
	"log"
	"net/http"
	"server/databaseOperate"
	"server/handler"
)

func main() {
	fmt.Println("Starting server...")

	databaseOperate.InitMySQL("root:123456@tcp(mysql:3306)/task")
	defer func(MysqlDatabase *sql.DB) {
		err := MysqlDatabase.Close()
		if err != nil {
			log.Println("Error close mysql:", err)
		}
	}(databaseOperate.MysqlDatabase)

	databaseOperate.InitRedis("redis:6379", "", 0)

	http.HandleFunc("/api/metric/upload", handler.UploadHandler)
	http.HandleFunc("/api/metric/query", handler.QueryHandler)

	log.Fatal(http.ListenAndServe(":8080", nil))
}
