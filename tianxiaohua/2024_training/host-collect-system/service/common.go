package service

import (
	"encoding/json"
)

// 序列化字符串
func serialize(value interface{}) []byte {
	// 将结构体序列化为 JSON 字符串
	utilizationJSON, err := json.Marshal(value)
	if err != nil {
		panic(err)
	}
	return utilizationJSON
}

func deserialize(jsonString string, value interface{}) {
	err := json.Unmarshal([]byte(jsonString), value)
	if err != nil {
		panic(err)
	}
}
