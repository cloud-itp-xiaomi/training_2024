package tdd_demo

import (
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql" //导入包但不使用，init()
)

var db *sql.DB //连接池对象
func connectServer() {
	//数据库
	//用户名:密码啊@tcp(ip:端口)/数据库的名字
	dsn := "root:355355@tcp(localhost:3306)/xiaomi"
	//连接数据集
	db, err := sql.Open("mysql", dsn) //open不会检验用户名和密码
	if err != nil {
		fmt.Printf("dsn:%s invalid,err:%v\n", dsn, err)
		return
	}
	err = db.Ping() //尝试连接数据库
	if err != nil {
		fmt.Printf("open %s faild,err:%v\n", dsn, err)
		return
	}
	fmt.Println(db)
	//设置数据库连接池的最大连接数
	db.SetMaxIdleConns(10)
}

func insertLog(log Log) {

}

func queryAll() {
	//1.查询单挑记录的sql语句  ?是参数
	sqlStr := "select * from log"
	//2.执行
	rows, err := db.Query(sqlStr) //从连接池里取一个连接出来去数据库查询单挑记录
	if err != nil {
		fmt.Printf("%s query failed,err:%v\n", sqlStr, err)
		return
	}
	//3.一定要关闭rows
	defer rows.Close()
	//4.拿到结果
	for rows.Next() {
		var logs Log
		rows.Scan(&logs.Hostname, &logs.File, &logs.Logs)
		fmt.Printf("u1:%#v\n", logs)
	}
}
