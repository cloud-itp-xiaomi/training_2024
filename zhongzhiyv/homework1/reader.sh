#!/bin/bash

# 检查参数数量
if [ "$#" -ne 4 ]; then
    echo "Usage: $0  ENDPOINT METRIC START_TS END_TS"
    exit 1
fi

# 从命令行获取变量
BASIC_URL="http://localhost:8080/api/metric/query"
ENDPOINT=$1
METRIC=$2
START_TS=$3
END_TS=$4

# 构建完整的 URL
SERVER_URL="${BASIC_URL}?metric=${METRIC}&endpoint=${ENDPOINT}&start_ts=${START_TS}&end_ts=${END_TS}"

# 使用 curl 调用 URL
response=$(curl -s "$SERVER_URL")

# 输出响应
echo "$response"
