package cache

import (
	"encoding/json"
	"log"
	"regexp"
	"strconv"
	"strings"
)

const (
	B = 1 << (iota * 10)
	KB
	MB
	GB
	TB
	PB
)

func ParseSize(size string) (int64, string) {
	//默认值为100MB
	re, _ := regexp.Compile("[0-9]+")
	uint := string(re.ReplaceAll([]byte(size), []byte("")))
	num, _ := strconv.ParseInt(strings.Replace(size, uint, "", 1), 10, 64)
	uint = strings.ToUpper(uint)
	var byteNum int64 = 0
	switch uint {
	case "B":
		byteNum = num * B
	case "KB":
		byteNum = num * KB
	case "MB":
		byteNum = num * MB
	case "GB":
		byteNum = num * GB
	case "TB":
		byteNum = num * TB
	case "PB":
		byteNum = num * PB
	default:
		num = 0
	}

	if num == 0 {
		log.Println("ParseSize 仅支持 B, KB, MB, TB, PB!!!")
		num = 100
		byteNum = num * MB
		uint = "MB"
	}

	sizeStr := strconv.FormatInt(num, 10) + uint

	return byteNum, sizeStr
}

func GetValueSize(value interface{}) int64 {
	//size := unsafe.Sizeof(value)
	//TODO
	bytes, _ := json.Marshal(value)
	size := int64(len(bytes))
	return size
}
