#!/bin/bash

# MySQL 配置
MYSQL_HOST="localhost"
MYSQL_USER="root"
MYSQL_PASS="QP984333zsy."
MYSQL_DB="ilovexiaomi"
MYSQL_TABLE="nginx_log_stats"

# Nginx 日志文件路径
LOG_FILE="/var/log/nginx/access.log"

# 获取当前时间
CURRENT_TIME=$(date +"%Y-%m-%d %H:%M:%S")

# 输出调试信息
echo "Current Time: $CURRENT_TIME"
echo "Log File: $LOG_FILE"

# 初始化统计变量
TOTAL_REQUESTS=0
TOTAL_HTTP_200=0
TOTAL_HTTP_500=0

# 获取一分钟前和当前时间的时间戳范围
START_TIME=$(date -d "1 minute ago" +"%s")
END_TIME=$(date +"%s")

# 输出起始和结束时间的字符串形式
START_TIME_STRING=$(date -d "@$START_TIME" +"%Y-%m-%dT%H:%M:%S%z")
END_TIME_STRING=$(date -d "@$END_TIME" +"%Y-%m-%dT%H:%M:%S%z")
echo "Start Time String: $START_TIME_STRING"
echo "End Time String: $END_TIME_STRING"

# 初始化第一个和最后一个日志时间戳
FIRST_LOG_TIMESTAMP_EPOCH=0
LAST_LOG_TIMESTAMP_EPOCH=0

# 读取日志并进行统计
while IFS= read -r line; do
    # 提取日志中的时间戳和状态码
    LOG_TIMESTAMP=$(echo "$line" | awk '{print $4 " " $5}' | tr -d '[]')
    LOG_STATUS_CODE=$(echo "$line" | awk '{print $9}')

    # 转换日志时间戳为标准格式
    LOG_TIMESTAMP_FORMATTED=$(echo "$LOG_TIMESTAMP" | sed -E 's#([0-9]{2})/([A-Za-z]+)/([0-9]{4}):([0-9]{2}):([0-9]{2}):([0-9]{2}) ([+-][0-9]{4})#\3-\2-\1T\4:\5:\6\7#' | sed 's/Jan/01/;s/Feb/02/;s/Mar/03/;s/Apr/04/;s/May/05/;s/Jun/06/;s/Jul/07/;s/Aug/08/;s/Sep/09/;s/Oct/10/;s/Nov/11/;s/Dec/12/')

    # 转换日志时间戳为时间戳
    LOG_TIMESTAMP_EPOCH=$(date -d "$LOG_TIMESTAMP_FORMATTED" +"%s" 2>/dev/null)

    # 检查转换是否成功
    if [[ -z "$LOG_TIMESTAMP_EPOCH" ]]; then
        echo "date: invalid date ‘$LOG_TIMESTAMP’"
        continue
    fi

    # 检查日志时间戳是否在指定时间范围内
    if [[ "$LOG_TIMESTAMP_EPOCH" -ge "$START_TIME" && "$LOG_TIMESTAMP_EPOCH" -le "$END_TIME" ]]; then
        TOTAL_REQUESTS=$((TOTAL_REQUESTS + 1))
        if [ "$LOG_STATUS_CODE" -eq 200 ]; then
            TOTAL_HTTP_200=$((TOTAL_HTTP_200 + 1))
        elif [ "$LOG_STATUS_CODE" -ge 500 ] && [ "$LOG_STATUS_CODE" -le 599 ]; then
            TOTAL_HTTP_500=$((TOTAL_HTTP_500 + 1))
        fi

        # 更新第一个和最后一个日志时间戳
        if [[ "$FIRST_LOG_TIMESTAMP_EPOCH" -eq 0 ]]; then
            FIRST_LOG_TIMESTAMP_EPOCH=$LOG_TIMESTAMP_EPOCH
        fi
        LAST_LOG_TIMESTAMP_EPOCH=$LOG_TIMESTAMP_EPOCH
    fi
done < "$LOG_FILE"

# 计算时间差和 QPS
if [[ "$FIRST_LOG_TIMESTAMP_EPOCH" -ne 0 && "$LAST_LOG_TIMESTAMP_EPOCH" -ne 0 ]]; then
    TIME_DIFF=$((LAST_LOG_TIMESTAMP_EPOCH - FIRST_LOG_TIMESTAMP_EPOCH))
else
    TIME_DIFF=0
fi

if [ "$TIME_DIFF" -gt 0 ]; then
    QPS=$(echo "scale=2; $TOTAL_REQUESTS / $TIME_DIFF" | bc)
else
    QPS=0
fi

# 输出统计信息
echo "Total Requests: $TOTAL_REQUESTS"
echo "Time Diff (s): $TIME_DIFF"
echo "QPS: $QPS"
echo "Total HTTP 200: $TOTAL_HTTP_200"
echo "Total HTTP 500: $TOTAL_HTTP_500"

# 将结果保存到 MySQL 数据库
mysql -h $MYSQL_HOST -u $MYSQL_USER -p$MYSQL_PASS $MYSQL_DB <<EOF
INSERT INTO $MYSQL_TABLE (timestamp, qps, http_200, http_500)
VALUES ('$CURRENT_TIME', '$QPS', '$TOTAL_HTTP_200', '$TOTAL_HTTP_500');
EOF

