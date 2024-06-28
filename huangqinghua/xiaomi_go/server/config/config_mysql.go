package config

import (
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"log"
)

type MySQLConfig struct {
	Host     string `yaml:"host"`
	Port     int    `yaml:"port"`
	User     string `yaml:"user"`
	Password string `yaml:"password"`
	Dbname   string `yaml:"dbname"`
}

var db *gorm.DB

// InitMySQL 初始化 MySQL 连接
func InitMySQL(cfg *MySQLConfig) {
	// username:password@tcp(host:port)/dbname 的格式，被 Go 的数据库驱动用来建立和MySQL的连接
	dsn := fmt.Sprintf("%s:%s@tcp(%s:%d)/%s", cfg.User, cfg.Password, cfg.Host, cfg.Port, cfg.Dbname)

	var err error
	// 使用gorm打开连接
	db, err = gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatalf("无法连接到MySQL: %v", err)
	}

	fmt.Println("成功连接到MySQL数据库")
}

// GetDB 获取连接
func GetDB() *gorm.DB {
	return db
}
