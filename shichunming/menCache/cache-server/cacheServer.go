package cache_server

import (
	"menCache/cache"
	"time"
)

type cacheServer struct {
	menCache cache.Cache
}

func NewMemoryCache() *cacheServer {
	return &cacheServer{
		menCache: cache.NewMenCache(),
	}
}

// SetMaxMemory size : 1KB, 100Kb, 1MB,2MB,1GB
func (cs *cacheServer) SetMaxMemory(size string) bool {
	return cs.menCache.SetMaxMemory(size)
}

// Set 将value写入缓存
func (cs *cacheServer) Set(key string, value interface{}, expire ...time.Duration) bool {
	expireTs := time.Second * 0
	if len(expire) > 0 {
		expireTs = expire[0]
	}
	return cs.menCache.Set(key, value, expireTs)
}

// Get 根据Key值获取Value
func (cs *cacheServer) Get(key string) (interface{}, bool) {
	return cs.menCache.Get(key)
}

// Del 删除Key值
func (cs *cacheServer) Del(key string) bool {
	return cs.menCache.Del(key)
}

// Exist 判断Key是否存在
func (cs *cacheServer) Exist(key string) bool {
	return cs.menCache.Exist(key)
}

// Flush 清空所有Key
func (cs *cacheServer) Flush() bool {
	return cs.menCache.Flush()
}

// Keys 获取所有Key的数量
func (cs *cacheServer) Keys() int64 {
	return cs.menCache.Keys()
}
