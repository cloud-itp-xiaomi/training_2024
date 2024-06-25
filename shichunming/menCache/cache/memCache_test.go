package cache

import (
	"fmt"
	"testing"
	"time"
)

func TestMenCache0P(t *testing.T) {
	testData := []struct {
		key    string
		val    interface{}
		expire time.Duration
	}{
		{"123133", 231321, time.Second * 0},
		{"123132", map[string]interface{}{"a": 3, "b": false}, time.Second * 0},
		{"123131", "231321", time.Second * 0},
		{"123130", true, time.Second * 0},
		{"123134", false, time.Second * 10},
		{"123135", "231321", time.Second * 15},
		//{"123136", map[string]interface{}{"a": 3, "b": false}, time.Second * 20},
	}
	c := NewMenCache()
	c.SetMaxMemory("10MB")
	for _, item := range testData {
		c.Set(item.key, item.val, item.expire)
		val, ok := c.Get(item.key)
		if !ok {
			t.Error("Get(key)失败！")
		}
		if item.key != "123132" && val != item.val {
			t.Error("0取值与预期不一致！")
		}
		_, ok1 := val.(map[string]interface{})
		if item.key == "123132" && !ok1 {
			t.Error("1取值与预期不一致！")
		}
	}
	if int64(len(testData)) != c.Keys() {
		t.Error("0缓存数量不一致！")
	}
	c.Del(testData[0].key)
	if int64(len(testData)-1) != c.Keys() {
		t.Error("1缓存数量不一致！")
	}
	time.Sleep(time.Second * 20)
	fmt.Println(c.Keys())

}
