package main

import (
	"fmt"
	"log"
	"net/http"
	"server/db"
	"server/handler"
)

func main() {
	fmt.Println("Starting server...")

	db.InitMySQL("root:123456@tcp(mysql:3306)/task")
	defer db.DB.Close()

	http.HandleFunc("/api/log/upload", handler.UploadHandler)
	http.HandleFunc("/api/log/query", handler.QueryHandler)

	log.Fatal(http.ListenAndServe(":8080", nil))
}
