package databaseOperate

import (
	"server/serverDataType"
	"testing"
)

func TestInitMySQL(t *testing.T) {
	type args struct {
		dsn string
	}
	tests := []struct {
		name string
		args args
	}{
		// TODO: Add test cases.
		{
			name: "test1",
			args: args{
				dsn: "root:123456@tcp(mysql:3306)/task",
			},
		},
		{
			name: "test2",
			args: args{
				dsn: "root:123@tcp(mysql:3306)/task",
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			InitMySQL(tt.args.dsn)
		})
	}
}

func TestInsertMetric(t *testing.T) {
	InitMySQL("root:123456@tcp(mysql:3306)/task")
	type args struct {
		metric serverDataType.MetricData
	}
	tests := []struct {
		name    string
		args    args
		wantErr bool
	}{
		// TODO: Add test cases.
		{
			name: "test1",
			args: args{
				metric: serverDataType.MetricData{
					Metric:    "mem.used.percent",
					Endpoint:  "mcp",
					Timestamp: 171818000,
					Step:      60,
					Value:     0.1,
				},
			},
			wantErr: true,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if err := InsertMetricToMySQL(tt.args.metric); (err != nil) != tt.wantErr {
				t.Errorf("InsertMetricToMySQL() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}
}
