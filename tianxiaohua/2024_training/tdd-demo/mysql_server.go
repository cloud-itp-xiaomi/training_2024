package tdd_demo

import (
	"fmt"
	"strings"
)

type MysqlServer struct {
	Server
}

func (s *MysqlServer) saveLog(log Log) error {
	sqlStr := "insert into log_data(hostname,file,logs,last_update_time) values(?,?,?,?)" //sql语句
	logs := strings.Join(log.Logs, "|")
	ret, err := db.Exec(sqlStr, log.Hostname, log.File, logs, log.FileLastUpdateTime) //执行sql语句
	if err != nil {
		fmt.Printf("insert failed,err:%v\n", err)
		return err
	}
	//如果是插入数据的操作，能够拿到插入数据的id
	id, err := ret.LastInsertId()
	if err != nil {
		fmt.Printf("get id failed,err:%v\n", err)
		return err
	}
	fmt.Println("id", id)
	return nil
}

//func (s *MysqlServer) saveLogs(logs []Log) error {
//
//}

func saveLogs(logs []Log) {

}
