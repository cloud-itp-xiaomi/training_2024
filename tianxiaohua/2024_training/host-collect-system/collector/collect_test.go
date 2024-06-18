package collector

import (
	"fmt"
	"testing"
	"time"
)

func TestGetCPUUtilization(t *testing.T) {
	for {
		getCPUUtilization()
		time.Sleep(time.Minute)
	}

}

func TestGetMemUtilization(t *testing.T) {
	for {
		getMemUtilization()
		time.Sleep(time.Minute)
	}
}

func TestGetHostName(t *testing.T) {
	fmt.Println(getHostname())
}

func TestStartCollect(t *testing.T) {
	startCollect()
}

func TestUpload(t *testing.T) {
	upload()
}
