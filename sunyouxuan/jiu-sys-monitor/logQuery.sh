#!/bin/bash

# 默认地址和参数值
DEFAULT_API_URL="http://127.0.0.1:8080/api/log/query"
DEFAULT_LOG_HOSTNAME="my-computer"
DEFAULT_FILE="/home/work/a.log"

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    key="$1"

    case $key in
        -url)
        API_URL="$2"
        shift
        shift
        ;;
        -hostname)
        LOG_HOSTNAME="$2"
        shift
        shift
        ;;
        -file)
        FILE="$2"
        shift
        shift
        ;;
        *)
        # 位置参数，忽略
        shift
        ;;
    esac
done

# 如果没有提供API URL，则使用默认地址
API_URL="${API_URL:-$DEFAULT_API_URL}"

# 如果没有提供hostname和file参数，则使用默认值
LOG_HOSTNAME="${LOG_HOSTNAME:-$DEFAULT_LOG_HOSTNAME}"
FILE="${FILE:-$DEFAULT_FILE}"

# 构造GET请求的URL
QUERY_URL="${API_URL}?hostname=${LOG_HOSTNAME}&file=${FILE}"
echo "${QUERY_URL}"

# 发送GET请求并获取数据
RESPONSE=$(curl -X GET "${QUERY_URL}")

# 检查是否请求成功
if [ $? -eq 0 ]; then
    # 检查API响应是否为空
    if [ -n "$RESPONSE" ]; then
        # 打印API响应
        echo "Query API response:"
        echo "${RESPONSE}"
    else
        echo "No data returned from Query API."
    fi
else
    echo "Failed to connect to Query API."
fi