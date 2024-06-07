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

    db.InitRedis("redis:6379", "", 0)

    http.HandleFunc("/api/metric/upload", handler.UploadHandler)
    http.HandleFunc("/api/metric/query", handler.QueryHandler)

    log.Fatal(http.ListenAndServe(":8080", nil))
}
