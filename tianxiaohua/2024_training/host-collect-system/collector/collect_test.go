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
	startCollectUtilization()
}

func TestUpload(t *testing.T) {
	upload()
}

func TestGetFilesAndMemType(t *testing.T) {
	fmt.Println(getFilesAndMemType())
}

func TestGetLogsFromLogFile(t *testing.T) {
	files := getFilesAndMemType().Files

	for _, file := range files {
		fi := getLogsFromLogFile(file)
		for _, f := range fi {
			fmt.Println(f)
		}
	}
}

func TestUploadLog(t *testing.T) {
	uploadLog()
}
