package storageOperate

import (
	"reflect"
	"server/serverDataType"
	"testing"
)

func TestParseLogLine(t *testing.T) {
	type args struct {
		line string
	}
	tests := []struct {
		name string
		args args
		want serverDataType.LogInformation
	}{
		// TODO: Add test cases.
		{
			name: "TestParseLogLine1",
			args: args{
				line: "Hostname: asc_mcp, File: /configFiles/gpu-manager.log, Log: Is nouveau loaded? no",
			},
			want: serverDataType.LogInformation{
				Hostname: "asc_mcp",
				File:     "/configFiles/gpu-manager.log",
				Logs:     []string{"Is nouveau loaded? no"},
			},
		},
		{
			name: "TestParseLogLine2",
			args: args{
				line: "Hostname: asc_mcp, File: /configFiles/gpu-manager.log, Log: last cards number = 2",
			},
			want: serverDataType.LogInformation{
				Hostname: "asc_mcp",
				File:     "/configFiles/gpu-manager.log",
				Logs:     []string{"last cards number = 2"},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := ParseLogLine(tt.args.line); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("ParseLogLine() = %v, want %v", got, tt.want)
			}
		})
	}
}

func TestMergeLogs(t *testing.T) {
	type args struct {
		logs []serverDataType.LogInformation
	}
	tests := []struct {
		name string
		args args
		want []serverDataType.LogInformation
	}{
		// TODO: Add test cases.
		{
			name: "test1",
			args: args{
				logs: []serverDataType.LogInformation{
					{
						Hostname: "testName",
						File:     "testFile",
						Logs:     []string{"log1", "log2"},
					},
					{
						Hostname: "testName",
						File:     "testFile",
						Logs:     []string{"log3", "log4"},
					},
				},
			},
			want: []serverDataType.LogInformation{
				{
					Hostname: "testName",
					File:     "testFile",
					Logs:     []string{"log1", "log2", "log3", "log4"},
				},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := MergeLogs(tt.args.logs); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("MergeLogs() = %v, want %v", got, tt.want)
			}
		})
	}
}

func TestQueryLocalLogs(t *testing.T) {
	type args struct {
		hostname string
		file     string
		logInfo  []serverDataType.LogInformation
	}
	tests := []struct {
		name string
		args args
		want []serverDataType.LogInformation
	}{
		// TODO: Add test cases.
		{
			name: "testHaveHostNameAndFile",
			args: args{
				hostname: "testName1",
				file:     "testFile1",
				logInfo: []serverDataType.LogInformation{
					{
						Hostname: "testName1",
						File:     "testFile1",
						Logs:     []string{"log1"},
					},
				},
			},
			want: []serverDataType.LogInformation{
				{
					Hostname: "testName1",
					File:     "testFile1",
					Logs:     []string{"log1"},
				},
			},
		},
		{
			name: "testHaveHostNameNOFile",
			args: args{
				hostname: "testName2",
				file:     "",
				logInfo: []serverDataType.LogInformation{
					{
						Hostname: "testName2",
						File:     "testFile2",
						Logs:     []string{"log2"},
					},
				},
			},
			want: []serverDataType.LogInformation{
				{
					Hostname: "testName2",
					File:     "testFile2",
					Logs:     []string{"log2"},
				},
			},
		},
		{
			name: "testNoHostNameHaveFile",
			args: args{
				hostname: "",
				file:     "testFile3",
				logInfo: []serverDataType.LogInformation{
					{
						Hostname: "HostName3",
						File:     "testFile3",
						Logs:     []string{"log3"},
					},
				},
			},
			want: []serverDataType.LogInformation{
				{
					Hostname: "HostName3",
					File:     "testFile3",
					Logs:     []string{"log3"},
				},
			},
		},
		{
			name: "testNoHostNameNoFile",
			args: args{
				hostname: "",
				file:     "",
				logInfo: []serverDataType.LogInformation{
					{
						Hostname: "HostName4",
						File:     "testFile4",
						Logs:     []string{"log4"},
					},
				},
			},
			want: []serverDataType.LogInformation{
				{
					Hostname: "HostName4",
					File:     "testFile4",
					Logs:     []string{"log4"},
				},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := QueryLocalLogs(tt.args.hostname, tt.args.file, tt.args.logInfo); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("QueryLocalLogs() = %v, want %v", got, tt.want)
			}
		})
	}
}
