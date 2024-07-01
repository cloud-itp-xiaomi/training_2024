#!/bin/bash

# 目标 URL
URL="192.168.72.130"

# 请求总数计数器
REQUEST_COUNT=0
# 一分钟内随机间隔时间发送请求
END=$((SECONDS+60))
while [ $SECONDS -lt $END ]; do
 # 随机生成一个数来决定请求类型
     RANDOM_NUMBER=$((RANDOM % 10))
     if [ $RANDOM_NUMBER -lt 8 ]; then
	     curl -s -o /dev/null -w "%{http_code}" $URL
     else
	     curl -s -o /dev/null -w "%{http_code}" "$URL/error"  
     fi
     
     REQUEST_COUNT=$((REQUEST_COUNT + 1))
     SLEEP_TIME=$(awk -v min=0.1 -v max=1.5 'BEGIN{srand(); print min+rand()*(max-min)}')
     sleep $SLEEP_TIME
 done

echo "Total requests sent: $REQUEST_COUNT" 
