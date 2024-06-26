package service

type MysqlServer struct {
	LogServer
}

func (s *MysqlServer) saveLog(log Log) error {
	db := connectServer()
	defer db.Close()
	insertLog(log, db)
	return nil
}

func (s *MysqlServer) readLog(hostname string, file string) Log {
	db := connectServer()
	defer db.Close()
	log := queryLog(hostname, file, db)
	return log
}

func (s *MysqlServer) updateLog(log Log) error {
	db := connectServer()
	defer db.Close()
	updateLog(log, db)
	return nil
}

func (s *MysqlServer) getLastUpdateTime(hostname string, file string) int64 {
	db := connectServer()
	defer db.Close()
	log := queryLog(hostname, file, db)
	if log.Hostname == "" {
		return 0
	}
	return log.FileLastUpdateTime
}
