package main

import (
	"testing"
)

func Test_getCpuUsage(t *testing.T) {
	tests := []struct {
		name    string
		want    float64
		wantErr bool
	}{
		// TODO: Add test cases.
		{
			name:    "test1",
			want:    float64(0.1),
			wantErr: false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			_, err := getCpuUsage()
			if (err != nil) != tt.wantErr {
				t.Errorf("getCpuUsage() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			//if got != tt.want {
			//	t.Errorf("getCpuUsage() got = %v, want %v", got, tt.want)
			//}
		})
	}
}

func Test_getMemoryUsage(t *testing.T) {
	tests := []struct {
		name    string
		want    float64
		wantErr bool
	}{
		// TODO: Add test cases.
		{
			name:    "test1",
			want:    float64(0.1),
			wantErr: false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			_, err := getMemoryUsage()
			if (err != nil) != tt.wantErr {
				t.Errorf("getMemoryUsage() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			//if got != tt.want {
			//	t.Errorf("getMemoryUsage() got = %v, want %v", got, tt.want)
			//}
		})
	}
}

func Test_getHostName(t *testing.T) {
	tests := []struct {
		name string
		want string
	}{
		// TODO: Add test cases.
		{
			name: "test1",
			want: "mcp",
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := getHostName(); got != tt.want {
				t.Errorf("getHostName() = %v, want %v", got, tt.want)
			}
		})
	}
}

func Test_sendData(t *testing.T) {
	type args struct {
		data []MetricData
	}
	tests := []struct {
		name string
		args args
	}{
		// TODO: Add test cases.
		{
			name: "test1",
			args: args{
				data: []MetricData{
					{
						Metric:    "mem.used.percent",
						Endpoint:  "mcp",
						Timestamp: 171818000,
						Step:      60,
						Value:     0.1,
					},
				},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			sendData(tt.args.data)
		})
	}
}

func Test_collectData(t *testing.T) {
	tests := []struct {
		name string
		want []MetricData
	}{
		// TODO: Add test cases.
		{
			name: "test1",
			want: []MetricData{
				{
					Metric:    "mem.used.percent",
					Endpoint:  "mcp",
					Timestamp: 171818000,
					Step:      60,
					Value:     0.1,
				},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got := collectData()
			if got == nil {
				t.Errorf("collectData() = %v, want %v", got, tt.want)
			}
		})
	}
}
