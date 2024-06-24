package databaseOperate

import (
	"reflect"
	"server/serverDataType"
	"testing"
)

func TestInitRedis(t *testing.T) {
	type args struct {
		addr     string
		password string
		db       int
	}
	tests := []struct {
		name string
		args args
	}{
		// TODO: Add test cases.
		{
			name: "test1",
			args: args{
				addr:     "redis:6379",
				password: "",
				db:       0,
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			InitRedis(tt.args.addr, tt.args.password, tt.args.db)
		})
	}
}

func TestInsertMetricToRedis(t *testing.T) {
	InitRedis("redis:6379", "", 0)
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
			wantErr: false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if err := InsertMetricToRedis(tt.args.metric); (err != nil) != tt.wantErr {
				t.Errorf("InsertMetricToRedis() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}
}

func TestQueryMetricsFromRedis(t *testing.T) {
	//InitRedis("redis:6379", "", 0)
	//var metric serverDataType.MetricData
	//metric.Metric = "mem.used.percent"
	//metric.Endpoint = "mcp"
	//metric.Timestamp = 171818000
	//metric.Step = 60
	//metric.Value = 0.1
	//err := InsertMetricToRedis(metric)
	//if err != nil {
	//	log.Println("InsertError")
	//	return
	//}
	type args struct {
		metric   string
		endpoint string
		startTs  int64
		endTs    int64
	}
	tests := []struct {
		name    string
		args    args
		want    []serverDataType.MetricData
		want1   int64
		wantErr bool
	}{
		// TODO: Add test cases.
		//{
		//	name: "test1",
		//	args: args{
		//		metric:   "mem.used.percent",
		//		endpoint: "mcp",
		//		startTs:  171818000,
		//		endTs:    171818000,
		//	},
		//	want: []serverDataType.MetricData{
		//		{
		//			Metric:    "mem.used.percent",
		//			Endpoint:  "mcp",
		//			Timestamp: 171818000,
		//			Step:      60,
		//			Value:     0.1,
		//		},
		//	},
		//	want1:   171818000,
		//	wantErr: true,
		//},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got, got1, err := QueryMetricsFromRedis(tt.args.metric, tt.args.endpoint, tt.args.startTs, tt.args.endTs)
			if (err != nil) != tt.wantErr {
				t.Errorf("QueryMetricsFromRedis() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("QueryMetricsFromRedis() got = %v, want %v", got, tt.want)
			}
			if got1 != tt.want1 {
				t.Errorf("QueryMetricsFromRedis() got1 = %v, want %v", got1, tt.want1)
			}
		})
	}
}
