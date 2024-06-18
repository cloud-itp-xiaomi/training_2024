package service

import "fmt"

func queryUtilizationS(metric string, endpoint string, start, end int64) []Utilization {
	// 先去redis查
	redisClient := redisClient()
	defer redisClient.Close()
	jsonString := LRange("uti_go", 0, 0, redisClient)

	var utilizations []Utilization

	fmt.Println(jsonString)
	for i := 0; i < len(jsonString); i++ {
		var utilization Utilization
		// 反序列化
		deserialize(jsonString[i], &utilization)
		//fmt.Println(utilization)
		if start > utilization.CollectTime {
			jsonStrings := LRange("uti_go", 0, LLen("uti_go", redisClient), redisClient)
			for j := 0; j < len(jsonStrings); j++ {
				var utilization Utilization
				deserialize(jsonStrings[j], &utilization)
				if start <= utilization.CollectTime && end >= utilization.CollectTime {
					if metric == utilization.Metric {
						utilizations = append(utilizations, utilization)
					}
				}
			}
		}

	}

	if len(utilizations) == 0 {
		db := connectServer()
		defer db.Close()
		utilizations = queryUtilization(metric, endpoint, start, end, db)
	}
	return utilizations
}
