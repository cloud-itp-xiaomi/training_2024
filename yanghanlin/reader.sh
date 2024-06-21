#!/bin/bash

# Server端的IP地址和端口,具体是指主机的端口这里上传的时候模糊处理一下
SERVER_IP="172.27.xx.xxx"
SERVER_PORT="8080"
# 查询参数
ENDPOINT="my-computer"
METRIC="cpu.used.percent"
START_TS=$(date +%s -d '1 hour ago')
END_TS=$(date +%s)
# 从Server端查询数据
response=$(curl -s "http://${SERVER_IP}:${SERVER_PORT}/api/metric/query?endpoint=${ENDPOINT}&metric=${METRIC}&startTs=${START_TS}&endTs=${END_TS}")

# 输出查询结果
echo "Query Result:"
echo "${response}"

