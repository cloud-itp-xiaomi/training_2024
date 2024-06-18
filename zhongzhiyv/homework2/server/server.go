package main

import (
	// "context"
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"os"

	// "strings"
	// "io/ioutil"
	// "math"
	"net/http"
	// "strconv"

	"strings"

	// "github.com/go-redis/redis/v8"
	_ "github.com/go-sql-driver/mysql"
)

type LogData struct {
	Hostname string
	File     string
	Logs     []string
}

type LogPost struct {
	LogStorage string    `json:"log_storage"`
	LogData    []LogData `json:"logdata"`
}

var db *sql.DB

func main() {
	fmt.Println("Starting server.........")

	//connect mysql
	err := MySqlInit("root:123456@tcp(mysql:3306)/mi")
	if err != nil {
		return
	}
	defer db.Close()

	//connect interface with function
	http.HandleFunc("/api/metric/upload", UploadHandler)

	http.HandleFunc("/api/metric/query", QueryHandler)

	log.Fatal(http.ListenAndServe(":8080", nil))
}

func MySqlInit(dsn string) error {
	var err error
	db, err = sql.Open("mysql", dsn)
	if err != nil {
		fmt.Println("err open mysql", err)
		return err
	}
	query := `
	CREATE TABLE IF NOT EXISTS Host (
		Hostname VARCHAR(255) NOT NULL,
		File VARCHAR(255) NOT NULL,
		PRIMARY KEY (Hostname, File)
	);`
	_, err = db.Exec(query)
	if err != nil {
		log.Fatalf("Could not create table Host: %v", err)
	}

	query = `
	CREATE TABLE IF NOT EXISTS Log (
		Log VARCHAR(2048) NOT NULL,
		Hostname VARCHAR(255) NOT NULL,
		File VARCHAR(255) NOT NULL,
		FOREIGN KEY (Hostname, File) REFERENCES Host (Hostname, File)
	);`
	_, err = db.Exec(query)
	if err != nil {
		log.Fatalf("Could not create table Host: %v", err)
		return err
	}
	return nil
}

func UploadHandler(writer http.ResponseWriter, request *http.Request) {
	//get data
	var data LogPost
	decoder := json.NewDecoder(request.Body)
	err := decoder.Decode(&data)
	if err != nil {
		http.Error(writer, "get data erro", http.StatusBadRequest)
	}

	//storedata
	switch data.LogStorage {
	case "local_file":
		for _, logEntry := range data.LogData {
			err := FileWriteLogsTo(logEntry)
			if err != nil {
				fmt.Println("Error writing logs to file: %v\n", err)
			}
		}
	case "mysql":
		for _, logEntry := range data.LogData {
			err := MysqlInsertLog(logEntry)
			if err != nil {
				fmt.Println("Error writing logs to mysql: %v\n", err)
			}
		}
	default:
		fmt.Println("no value LogStorage")
	}

}

func FileWriteLogsTo(logData LogData) error {
	// create folder path
	folderPath := "/savefile"

	// create folder if it doesn't exist
	if _, err := os.Stat(folderPath); os.IsNotExist(err) {
		err := os.MkdirAll(folderPath, 0755)
		if err != nil {
			return err
		}
	}
	// create filename
	filename := fmt.Sprintf("%s/%s_%s", folderPath, logData.Hostname, logData.File)

	// make slice to string
	content := strings.Join(logData.Logs, "\n")

	// add data to file
	err := os.WriteFile(filename, []byte(content), 0644)

	if err != nil {
		return err
	}

	fmt.Printf("Logs written to file: %s\n", filename)
	return err
}

func MysqlInsertLog(logData LogData) error {
	//insert table host
	query := "INSERT IGNORE INTO Host (Hostname, File) VALUES (?, ?)"

	_, err := db.Exec(query, logData.Hostname, logData.File)
	if err != nil {
		log.Println("err insert host:", err)
		return err
	}

	//insert table log
	query = "INSERT INTO Log (Log, Hostname, File) VALUES (?, ?, ?)"
	for _, logEntry := range logData.Logs {
		_, err := db.Exec(query, logEntry, logData.Hostname, logData.File)
		if err != nil {
			log.Println("err insert log", err)
			return err
		}
	}

	fmt.Printf("Logs written to mysql: \n")
	return err
}
func QueryHandler(writer http.ResponseWriter, request *http.Request) {

}
