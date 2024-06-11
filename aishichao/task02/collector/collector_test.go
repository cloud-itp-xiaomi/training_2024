package main

import (
	"testing"
)

func Test_sendData(t *testing.T) {
	type logs struct {
		data       []LogInformation
		logStorage string
	}
	tests := []struct {
		name string
		logs logs
	}{
		//todo list
		{
			name: "testName",
			logs: logs{
				data: []LogInformation{
					{
						Hostname: "mcp",
						File:     "/home/asc/task/test.go",
						Logs:     []string{"logTest1", "logTest2"},
					},
				},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			sendData(tt.logs.data, tt.logs.logStorage)
		})
	}
}
