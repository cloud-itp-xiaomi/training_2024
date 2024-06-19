package main

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"math"
	"net/http"
	"strconv"

	"github.com/go-redis/redis/v8"
	_ "github.com/go-sql-driver/mysql"
)

type JsonStruct struct {
	Metric    string  `json:"metric"`
	Endpoint  string  `json:"endpoint"`
	Timestamp int64   `json:"timestamp"`
	Step      int64   `json:"step"`
	Value     float64 `json:"value"`
}
type Value struct {
	Timestamp int64   `json:"timestamp"`
	Vvalue    float64 `json:"value"`
}
type Metric struct {
	Metric string  `json:"metric"`
	Values []Value `json:"values"`
}

// var (
//
//	webappAddr = "0.0.0.0:9393"
//
// )
var rdb *redis.Client
var ctx = context.Background()
var db *sql.DB

func main() {
	fmt.Println("Starting server....")
	var err error
	rdb = redis.NewClient(&redis.Options{
		Addr:     "redis:6379",
		Password: "", // no password set
		DB:       0,  // use default DB
	})
	http.HandleFunc("/api/metric/upload", UploadHandler)
	http.HandleFunc("/api/metric/query", QueryHandler)
	db, err = sql.Open("mysql", "root:123456@tcp(mysql:3306)/mi")
	if err != nil {
		fmt.Println("err open mysql", err)
		return
	}
	defer db.Close()
	query := `
	CREATE TABLE IF NOT EXISTS metric (
		Metric VARCHAR(255) NOT NULL,
		Endpoint VARCHAR(255) NOT NULL,
		Timestamp BIGINT NOT NULL,
		Step BIGINT NOT NULL,
		Value DOUBLE NOT NULL,
		PRIMARY KEY (Metric, Endpoint, Timestamp)
	);`
	_, err = db.Exec(query)
	if err != nil {
		log.Fatalf("Could not create table: %v", err)
	}

	log.Fatal(http.ListenAndServe(":8080", nil))
}

func UploadHandler(writer http.ResponseWriter, request *http.Request) {
	//create decoder from request.body,get data
	fmt.Println("Starting UploadHandler....")
	var data []JsonStruct
	decoder := json.NewDecoder(request.Body)
	err := decoder.Decode(&data)
	if err != nil {
		http.Error(writer, "get data erro", http.StatusBadRequest)
	}

	//store data
	for _, metric := range data {
		//store data to redis
		key := "metrics:" + metric.Metric
		jsonData, err := json.Marshal(metric)
		if err != nil {
			http.Error(writer, "store data to redis erro", http.StatusInternalServerError)
			return
		}
		lPushResult := rdb.LPush(ctx, key, jsonData)
		lTrimResult := rdb.LTrim(ctx, key, 0, 9)

		// Process the results
		if lPushResult.Err() != nil {
			http.Error(writer, "store data to redis erro", http.StatusInternalServerError)
			return
		}
		if lTrimResult.Err() != nil {
			http.Error(writer, "store data to redis erro", http.StatusInternalServerError)
			return
		}
		// store data to mysql
		// query := `
		// CREATE TABLE IF NOT EXISTS metric (
		// 	Metric VARCHAR(255) NOT NULL,
		// 	Endpoint VARCHAR(255) NOT NULL,
		// 	Timestamp BIGINT NOT NULL,
		// 	Step BIGINT NOT NULL,
		// 	Value DOUBLE NOT NULL,
		// 	PRIMARY KEY (Metric, Endpoint, Timestamp)
		// );`
		// _, err = db.Exec(query)
		// if err != nil {
		// 	log.Fatalf("Could not create table: %v", err)
		// }

		// fmt.Println("Table 'metric' checked/created")

		_, err = db.Exec("INSERT INTO metric (Metric, Endpoint, Timestamp, Step, Value) VALUES(?,?,?,?,?)",
			metric.Metric, metric.Endpoint, metric.Timestamp, metric.Step, metric.Value)
		if err != nil {
			log.Println("Store data to mysql erro", err)
			http.Error(writer, "store data to mysql erro", http.StatusInternalServerError)
			return
		}

	}

}
func QueryHandler(writer http.ResponseWriter, request *http.Request) {
	params := []string{"metric", "endpoint", "start_ts", "end_ts"}
	parammap := make(map[string]string)
	vars := request.URL.Query()
	for _, param := range params {
		value := vars.Get(param)
		parammap[param] = value
	}
	startime, err := strconv.ParseInt(parammap["start_ts"], 10, 64)
	if err != nil {
		http.Error(writer, "startime err", http.StatusBadRequest)
	}
	endtime, err := strconv.ParseInt(parammap["end_ts"], 10, 64)
	if err != nil {
		http.Error(writer, "endtime err", http.StatusBadRequest)
	}
	metric := parammap["metric"]
	endpoint := parammap["endpoint"]
	//first query from redis
	key := "metrics:" + metric
	Queryresult, err := rdb.LRange(ctx, key, 0, -1).Result()
	if err != nil {
		http.Error(writer, "redis query err", http.StatusInternalServerError)
	}
	retmess := []Metric{
		{
			Metric: "cpu.used.percent",
			Values: []Value{},
		},
		{
			Metric: "memory.used.percent",
			Values: []Value{},
		},
	}
	var uptime int64
	uptime = math.MaxInt64
	for _, item := range Queryresult {
		var metricData JsonStruct
		err := json.Unmarshal([]byte(item), &metricData)
		if err != nil {
			log.Printf("Unmarshal data err", err)
			continue
		}
		if metricData.Endpoint == endpoint && metricData.Timestamp >= startime && metricData.Timestamp <= endtime {
			if metric == "cpu.used.percent" {
				retmess[0].Values = append(retmess[0].Values, Value{Timestamp: metricData.Timestamp, Vvalue: metricData.Value})
			}
			if metric == "memory.used.percent" {
				retmess[1].Values = append(retmess[1].Values, Value{Timestamp: metricData.Timestamp, Vvalue: metricData.Value})
			}
			if metricData.Timestamp < uptime {
				uptime = metricData.Timestamp
			}
		}
	}
	//data is out of startime use mysql
	if uptime > startime {
		query := "SELECT Metric, Endpoint, Timestamp, Step, Value FROM metric WHERE Metric = ? AND Endpoint = ?	AND Timestamp BETWEEN ? AND ?"
		rows, err := db.Query(query, metric, endpoint, startime, uptime)
		if err != nil {
			http.Error(writer, "mysql query err", http.StatusInternalServerError)
		}
		defer rows.Close()

		for rows.Next() {
			var rowval JsonStruct
			err := rows.Scan(&rowval.Metric, &rowval.Endpoint, &rowval.Timestamp, &rowval.Step, &rowval.Value)
			if err != nil {
				log.Printf("get rows errr", err)
				continue
			}
			if rowval.Metric == "cpu.used.percent" {
				retmess[0].Values = append(retmess[0].Values, Value{Timestamp: rowval.Timestamp, Vvalue: rowval.Value})
			}
			if rowval.Metric == "memory.used.percent" {
				retmess[1].Values = append(retmess[1].Values, Value{Timestamp: rowval.Timestamp, Vvalue: rowval.Value})
			}
		}
	}
	writer.Header().Set("Content-Type", "application/json")
	err = json.NewEncoder(writer).Encode(retmess)
	if err != nil {
		http.Error(writer, "encoding err", http.StatusInternalServerError)
	}
}

// func IndexHandle(res http.ResponseWriter, req *http.Request) {
// 	sayTo := req.FormValue("name")
// 	res.Write([]byte("Hello!" + sayTo))
// }
