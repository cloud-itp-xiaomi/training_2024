package main

import (
	"fmt"
	"log"
	"net/http"
	v1 "server/api/v1"
	"server/config"
	"server/internal/server"
)

func main() {
	// 加载配置
	cfg, err := config.LoadConfig()
	if err != nil {
		log.Fatal("无法加载配置: %V", err)
	}

	config.InitMySQL(&cfg.MySQL)

	srv := server.NewServer(cfg)
	router := v1.NewRouter(srv)

	// 启动服务器
	fmt.Printf("服务器正在端口 %d 上运行...\n", cfg.Port)
	log.Fatal(http.ListenAndServe(fmt.Sprintf(":%d", cfg.Port), router))
}
