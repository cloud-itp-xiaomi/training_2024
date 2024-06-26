package handler

import (
	"net/http"
	"strconv"
	"time"
)

func ConfirmTs(queryParams map[string]string, w http.ResponseWriter) (int64, int64, string, string) {
	var startTs, endTs int64
	var metric, endpoint string
	var err error
	if queryParams["start_ts"] == "" {
		startTs = 0 // Use the earliest timestamp
	} else {
		startTs, err = strconv.ParseInt(queryParams["start_ts"], 10, 64)
		if err != nil {
			http.Error(w, "invalid start_ts", http.StatusBadRequest)
			return 0, 0, "", ""
		}
	}

	if queryParams["end_ts"] == "" {
		endTs = time.Now().Unix() // Use the current timestamp
	} else {
		endTs, err = strconv.ParseInt(queryParams["end_ts"], 10, 64)
		if err != nil {
			http.Error(w, "invalid end_ts", http.StatusBadRequest)
			return 0, 0, "", ""
		}
	}

	metric = queryParams["metric"]
	endpoint = queryParams["endpoint"]
	return startTs, endTs, metric, endpoint
}
