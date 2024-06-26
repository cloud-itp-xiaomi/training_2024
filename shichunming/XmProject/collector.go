package main

import (
	"context"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"
)

func main() {
	mux := http.NewServeMux()
	mux.Handle("/", &helloHandler{})

	server := &http.Server{
		Addr:    ":8081",
		Handler: mux,
	}

	// 创建系统信号接收器
	done := make(chan os.Signal)
	signal.Notify(done, os.Interrupt, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		<-done

		if err := server.Shutdown(context.Background()); err != nil {
			log.Fatal("Shutdown server:", err)
		}
	}()

	log.Println("Starting HTTP server...")
	err := server.ListenAndServe()
	if err != nil {
		if err == http.ErrServerClosed {
			log.Print("Server closed under request")
		} else {
			log.Fatal("Server closed unexpected")
		}
	}
}

type helloHandler struct{}
type helloResponse struct {
	Message string `json:"message"`
}

func (*helloHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	if r.Method == "POST" {
		var data map[string]interface{}
		body, err := ioutil.ReadAll(r.Body)
		if err != nil {
			http.Error(w, "Can't read body", http.StatusBadRequest)
			return
		}
		defer r.Body.Close()

		err = json.Unmarshal(body, &data)
		if err != nil {
			http.Error(w, "Can't parse JSON", http.StatusBadRequest)
			return
		}

		// 假设处理数据并返回一个响应
		response := helloResponse{Message: fmt.Sprintf("Received: %+v", data)}
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	} else {
		log.Printf("访问：%s", time.Local)
	}
}
