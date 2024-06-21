package storageOperate

import (
	"database/sql"
	"log"
	"reflect"
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

func TestInsertLogs(t *testing.T) {
	var err error
	MysqlDatabase, err = sql.Open("mysql", "root:123456@tcp(mysql:3306)/task")
	if err != nil {
		log.Fatal("Error connecting to MySQL:", err)
	}
	type args struct {
		logs serverDataType.LogInformation
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
				logs: serverDataType.LogInformation{
					Hostname: "asc_mcp",
					File:     "access_logs",
					Logs:     []string{"logTest1", "logTest2"},
				},
			},
			wantErr: true,
		},
		{
			name: "test2",
			args: args{
				logs: serverDataType.LogInformation{
					Hostname: "asc_mcp",
					File:     "access_logs",
					Logs:     []string{"logTest1", "logTest2"},
				},
			},
			wantErr: true,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if err := InsertLogsIntoMySQL(tt.args.logs); (err != nil) != tt.wantErr {
				t.Errorf("InsertLogsIntoMySQL() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}

}

func TestFileLogsExist(t *testing.T) {
	var err error
	MysqlDatabase, err = sql.Open("mysql", "root:123456@tcp(mysql:3306)/task")
	if err != nil {
		log.Fatal("Error connecting to MySQL:", err)
	}
	type args struct {
		hostname string
		file     string
	}
	tests := []struct {
		name    string
		args    args
		want    bool
		want1   int64
		wantErr bool
	}{
		// TODO: Add test cases.
		{
			name: "TestFileLogsExist1",
			args: args{
				hostname: "asc_mcp",
				file:     "/configFiles/access_log",
			},
			want:    false,
			want1:   0,
			wantErr: true,
		},
		{
			name: "TestFileLogsExist2",
			args: args{
				hostname: "asc",
				file:     "gpu-manager.log",
			},
			want:    false,
			want1:   0,
			wantErr: true,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got, got1, err := FileLogsExist(tt.args.hostname, tt.args.file)
			if (err != nil) != tt.wantErr {
				t.Errorf("FileLogsExist() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if got != tt.want {
				t.Errorf("FileLogsExist() got = %v, want %v", got, tt.want)
			}
			if got1 != tt.want1 {
				t.Errorf("FileLogsExist() got1 = %v, want %v", got1, tt.want1)
			}
		})
	}
}

func TestQueryLogs(t *testing.T) {
	var err error
	MysqlDatabase, err = sql.Open("mysql", "root:123456@tcp(mysql:3306)/task")
	if err != nil {
		log.Fatal("Error connecting to MySQL:", err)
	}
	type args struct {
		hostname string
		file     string
	}
	tests := []struct {
		name    string
		args    args
		want    []string
		wantErr bool
	}{
		// TODO: Add test cases.
		{
			name: "test1",
			args: args{
				hostname: "asc_mcp",
				file:     "access_log",
			},
			wantErr: true,
		},
		{
			name: "test1",
			args: args{
				hostname: "asc",
				file:     "test",
			},
			wantErr: true,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			got, err := QueryLogsFromMySQL(tt.args.hostname, tt.args.file)
			if (err != nil) != tt.wantErr {
				t.Errorf("QueryLogsFromMySQL() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("QueryLogsFromMySQL() got = %v, want %v", got, tt.want)
			}
		})
	}
}
