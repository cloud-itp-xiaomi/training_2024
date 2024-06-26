package main

import (
	"reflect"
	"testing"
)

func Test_sendData(t *testing.T) {
	type args struct {
		data       []LogInformation
		logStorage string
	}
	tests := []struct {
		name string
		logs args
	}{
		//todo list
		{
			name: "test_sendData1",
			logs: args{
				data: []LogInformation{
					{
						Hostname: "mcp",
						File:     "/home/asc/task/test.go",
						Logs:     []string{"logTest1", "logTest2"},
					},
				},
			},
		},
		{
			name: "test_sendData2",
			logs: args{
				data: []LogInformation{
					{
						Hostname: "mcp",
						File:     "access_log",
						Logs:     []string{"logTest1", "logTest2", "logTest3"},
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

func Test_getConfig(t *testing.T) {
	type args struct {
		jsonFileName string
	}
	tests := []struct {
		name    string
		args    args
		want    *ConfigData
		wantErr bool
	}{
		// TODO: Add test cases.
		{
			name: "testGetConfig1",
			args: args{
				jsonFileName: "./configFile/config.json",
			},
			want: &ConfigData{
				Files:      []string{"/configFiles/gpu-manager.log", "/configFiles/access_log"},
				LogStorage: "local_file",
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got, err := getConfig(tt.args.jsonFileName)
			if (err != nil) != tt.wantErr {
				t.Errorf("getConfig() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("getConfig() got = %v, want %v", got, tt.want)
			}
		})
	}
}

func Test_getLogsInformation(t *testing.T) {
	type args struct {
		logPaths []string
	}
	tests := []struct {
		name    string
		args    args
		want    []LogInformation
		wantErr bool
	}{
		// TODO: Add test cases.
		{
			name: "test_getLogsInformation1",
			args: args{
				logPaths: []string{"./configFile/test.log"},
			},
			want: []LogInformation{
				{
					Hostname: "mcp",
					File:     "./configFile/test.log",
					Logs:     []string{"test1", "test2"},
				},
			},
			wantErr: false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got, err := getLogsInformation(tt.args.logPaths)
			if (err != nil) != tt.wantErr {
				t.Errorf("getLogsInformation() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("getLogsInformation() got = %v, want %v", got, tt.want)
			}
		})
	}
}
