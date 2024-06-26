package cache

import "time"

type Cache interface {
	// SetMaxMemory size : 1KB, 100Kb, 1MB,2MB,1GB
	SetMaxMemory(size string) bool

	// Set 将value写入缓存
	Set(key string, value interface{}, expire time.Duration) bool

	// Get 根据Key值获取Value
	Get(key string) (interface{}, bool)

	// Del 删除Key值
	Del(key string) bool

	// Exist 判断Key是否存在
	Exist(key string) bool

	// Flush 清空所有Key
	Flush() bool

	// Keys 获取所有Key的数量
	Keys() int64
}
