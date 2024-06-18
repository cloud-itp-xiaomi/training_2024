package service

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
)

type Values struct {
	Timestamp int64
	Value     float64
}

func startQuery() {
	r := gin.Default()
	r.GET("/api/metric/query", func(c *gin.Context) {
		// 获取查询参数
		metric := c.Query("metric")
		endpoint := c.Query("endpoint")
		startTS, err1 := strconv.ParseInt(c.Query("start_ts"), 10, 64)
		endTS, err2 := strconv.ParseInt(c.Query("end_ts"), 10, 64)

		if err1 != nil || err2 != nil {
			c.JSON(http.StatusBadRequest, gin.H{
				"error": "Invalid timestamp format",
			})
			return
		}

		utilizations := queryUtilizationS(metric, endpoint, startTS, endTS)
		var values []Values
		for i := 0; i < len(utilizations); i++ {
			value := Values{
				Timestamp: utilizations[i].CollectTime,
				Value:     utilizations[i].Value,
			}
			values = append(values, value)
		}
		// 返回查询参数
		c.JSON(http.StatusOK, gin.H{
			"metric":   metric,
			"endpoint": endpoint,
			"values":   values,
		})
	})
	r.Run()
}
