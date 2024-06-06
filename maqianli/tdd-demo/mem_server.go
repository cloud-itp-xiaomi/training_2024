package tdd_demo

type Log struct {
	Hostname string
	File     string
	Logs     []string
}

type MemoryServer struct {
	Server
	savedLogs []Log
}

func (s *MemoryServer) saveLog(log Log) error {
	return s.saveLogs([]Log{log})
}

func (s *MemoryServer) readLog() Log {
	return s.savedLogs[0]
}

func (s *MemoryServer) saveLogs(logs []Log) error {
	s.savedLogs = logs
	return nil
}

func (s *MemoryServer) readLogs() []Log {
	return s.savedLogs
}
