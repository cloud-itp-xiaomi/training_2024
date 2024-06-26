package db

import (
	"context"
	"fmt"
	"github.com/redis/go-redis/v9"
	"github.com/spf13/viper"
	"log"
)

var (
	RedisClient *redis.Client
	Ctx         = context.Background()
)

type RedisConfig struct {
	Host     string
	Port     int
	Password string
	DB       int
}

func InitRedis() {
	viper.SetConfigName("config")
	viper.SetConfigType("yaml")
	viper.AddConfigPath(".")
	if err := viper.ReadInConfig(); err != nil {
		log.Fatalf("Error reading config file, %s", err)
	}

	var config RedisConfig
	if err := viper.UnmarshalKey("redis", &config); err != nil {
		log.Fatalf("Unable to decode into struct, %v", err)
	}

	RedisClient = redis.NewClient(&redis.Options{
		Addr:     fmt.Sprintf("%s:%d", config.Host, config.Port),
		Password: config.Password,
		DB:       config.DB,
	})

	_, err := RedisClient.Ping(Ctx).Result()
	if err != nil {
		log.Fatalf("Failed to connect to Redis: %v", err)
	}
}
