package main

import (
	"collector/collector"
	"time"
)

func main() {
	collector.SendMetricData()

	ticker := time.NewTicker(1 * time.Minute)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			collector.SendMetricData()
		}
	}
}
