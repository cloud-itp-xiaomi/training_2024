package cache

import (
	"fmt"
	"log"
	"sync"
	"time"
)

type menCache struct {
	//最大内存
	maxMemorySize int64
	//最大内存字符串表示
	maxMemorySizeStr string
	//当前已使用内存
	currMemorySize int64
	//缓存键值对
	values map[string]*memCacheValue
	//读写锁
	locker sync.RWMutex
	//清除过期时间缓存间隔
	clearExpiredItemTimeInterval time.Duration
}

type memCacheValue struct {
	//value值
	val interface{}
	//过期时间
	expireTime time.Time
	//有效时长
	expire time.Duration
	//value大小
	size int64
}

// SetMaxMemory size : 1KB, 100Kb, 1MB,2MB,1GB
func (mc *menCache) SetMaxMemory(size string) bool {
	mc.maxMemorySize, mc.maxMemorySizeStr = ParseSize(size)
	fmt.Println(mc.maxMemorySize, mc.maxMemorySizeStr)
	fmt.Println("call set max memory")
	return true
}

// Set 将value写入缓存
func (mc *menCache) Set(key string, value interface{}, expire time.Duration) bool {
	mc.locker.Lock()
	defer mc.locker.Unlock()
	v := &memCacheValue{
		val:        value,
		expireTime: time.Now().Add(expire),
		expire:     expire,
		size:       GetValueSize(value),
	}
	mc.del(key)
	mc.add(key, v)
	if mc.currMemorySize > mc.maxMemorySize {
		mc.del(key)
		//panic(fmt.Sprintf("maxMemory is %v", mc.maxMemorySize))
		log.Printf("maxMemory is %v", mc.maxMemorySize)
	}
	return true
}

func (mc *menCache) get(key string) (*memCacheValue, bool) {
	val, ok := mc.values[key]
	return val, ok
}

func (mc *menCache) del(key string) {
	tmp, ok := mc.get(key)
	if ok && tmp != nil {
		mc.currMemorySize -= tmp.size
		delete(mc.values, key)
	}
}

func (mc *menCache) add(key string, val *memCacheValue) {
	mc.values[key] = val
	mc.currMemorySize += val.size

}

// Get 根据Key值获取Value
func (mc *menCache) Get(key string) (interface{}, bool) {
	mc.locker.Lock()
	defer mc.locker.Unlock()
	mcv, ok := mc.get(key)
	if ok {
		//判断缓存是否过期
		if mcv.expire != 0 && mcv.expireTime.Before(time.Now()) {
			mc.del(key)
			return nil, false
		}
		return mcv.val, ok
	}
	return nil, false
}

// Del 删除Key值
func (mc *menCache) Del(key string) bool {
	mc.locker.Lock()
	defer mc.locker.Unlock()
	mc.del(key)
	return true
}

// Exist 判断Key是否存在
func (mc *menCache) Exist(key string) bool {
	mc.locker.RLock()
	defer mc.locker.RUnlock()
	_, ok := mc.values[key]
	return ok
}

// Flush 清空所有Key
func (mc *menCache) Flush() bool {
	mc.locker.Lock()
	defer mc.locker.Unlock()
	mc.values = make(map[string]*memCacheValue, 0)
	mc.currMemorySize = 0
	return true
}

// Keys 获取所有Key的数量
func (mc *menCache) Keys() int64 {
	mc.locker.RLock()
	defer mc.locker.RUnlock()
	return int64(len(mc.values))
}

func NewMenCache() Cache {
	fmt.Println("call new men cache")
	mc := &menCache{
		values:                       make(map[string]*memCacheValue, 0),
		clearExpiredItemTimeInterval: time.Second * 1, //一秒一次
	}
	go mc.clearExpiredItem()
	return mc
}

func (mc *menCache) clearExpiredItem() {
	timeTicker := time.NewTicker(mc.clearExpiredItemTimeInterval)
	defer timeTicker.Stop()
	for {
		select {
		case <-timeTicker.C:
			for key, val := range mc.values {
				if val.expire != 0 && time.Now().After(val.expireTime) {
					mc.locker.Lock()
					mc.del(key)
					mc.locker.Unlock()
				}
			}
		}
	}
}
