package service

import (
	"github.com/redis/go-redis/v9"
)

func RPush(key string, value interface{}, rdb *redis.Client) {
	err := rdb.RPush(ctx, key, value).Err()
	if err != nil {
		panic(err)
	}
}

func LPop(key string, rdb *redis.Client) {
	err := rdb.LPop(ctx, key).Err()
	if err != nil {
		panic(err)
	}
}

func LLen(key string, rdb *redis.Client) int64 {
	val, err := rdb.LLen(ctx, key).Result()
	if err != nil {
		panic(err.Error())
	}
	return val
}

func LRange(key string, start, end int64, rdb *redis.Client) []string {
	val, err := rdb.LRange(ctx, key, start, end).Result()
	if err != nil {
		panic(err.Error())
	}
	return val
}

func trim(key string, start, end int64, rdb *redis.Client) {
	err := rdb.LTrim(ctx, key, start, end).Err()
	if err != nil {
		panic(err)
	}
}
