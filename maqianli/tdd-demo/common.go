package tdd_demo

type Server interface {
	saveLog(log Log) error
	readLog() Log
	saveLogs(logs []Log) error
	readLogs() []Log
}
