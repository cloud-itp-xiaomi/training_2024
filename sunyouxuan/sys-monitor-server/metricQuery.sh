#!/bin/bash

# 默认地址
DEFAULT_API_URL="http://127.0.0.1:8080/api/metric/query"

# 默认值
DEFAULT_ENDPOINT="jiuth@Ubuntu24.04LTS@127.0.0.1"
#DEFAULT_ENDPOINT="jiuth@Ubuntu%2024.04%20LTS"
DEFAULT_METRIC="cpu.used.percent"
DEFAULT_START_TS="1715765640"
DEFAULT_END_TS="1719151108"

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    key="$1"

    case $key in
        -url)
        API_URL="$2"
        shift
        shift
        ;;
        -endpoint)
        ENDPOINT="$2"
        shift
        shift
        ;;
        -metric)
        METRIC="$2"
        shift
        shift
        ;;
        -start_ts)
        START_TS="$2"
        shift
        shift
        ;;
        -end_ts)
        END_TS="$2"
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

# 如果没有提供其他字段的参数，则使用默认值
ENDPOINT="${ENDPOINT:-$DEFAULT_ENDPOINT}"
METRIC="${METRIC:-$DEFAULT_METRIC}"
START_TS="${START_TS:-$DEFAULT_START_TS}"
END_TS="${END_TS:-$DEFAULT_END_TS}"

# 空格转换防止出错
ENDPOINT="${ENDPOINT// /%20}"

# 构造GET请求的URL
QUERY_URL="${API_URL}?endpoint=${ENDPOINT}&metric=${METRIC}&start_ts=${START_TS}&end_ts=${END_TS}"
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
