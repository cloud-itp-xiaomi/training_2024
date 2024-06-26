package main

import (
	"fmt"
	cacheServer "menCache/cache-server"
	"time"
)

func main() {
	cache := cacheServer.NewMemoryCache()
	//cache.SetMaxMemory("100MB")
	//cache.Set("int", 1, time.Second)
	//cache.Set("bool", false, time.Second)
	//cache.Set("data", map[string]interface{}{"a": 1}, time.Second)
	///*
	//	cache.Set("int", 1)
	//	cache.Set("bool", false)
	//	cache.Set("data", map[string]interface{}{"a": 1})
	//*/
	//cache.Get("int")
	//cache.Del("int")
	//cache.Flush()
	//cache.Keys()
	fmt.Println(cache.SetMaxMemory("100MB"))
	fmt.Println(cache.Set("int", 1, time.Second))
	fmt.Println(cache.Set("bool", false, time.Second))
	fmt.Println(cache.Set("data", map[string]interface{}{"a": 1}, time.Second))
	fmt.Println(cache.Get("int"))
	fmt.Println(cache.Keys())
}
