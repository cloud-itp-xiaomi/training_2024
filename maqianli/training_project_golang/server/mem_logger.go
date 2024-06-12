package server

import llog "log"

type MemLogger struct {
	savedLogs []Log
}

func (l *MemLogger) readLogs() ([]Log, error) {
	return l.savedLogs, nil
}

func (l *MemLogger) saveLogs(logs []Log) error {
	l.savedLogs = logs
	return nil
}

func (l *MemLogger) deserializeMultiLogs(logs []string) ([]Log, error) {
	llog.Panic("implement me")
	return nil, nil
}

func (l *MemLogger) serializeMultiLogs(logs []Log) ([]string, error) {
	llog.Panic("implement me")
	return nil, nil
}

func (l *MemLogger) deserializeLog(log string) (Log, error) {
	llog.Panic("implement me")
	return Log{}, nil
}

func (l *MemLogger) serializeLog(log Log) ([]string, error) {
	llog.Panic("implement me")
	return nil, nil
}
