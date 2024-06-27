#!/bin/bash

SERVER_URL="http://localhost:8080"
HOSTNAME=$(hostname)

QUERY_URL="${SERVER_URL}/api/logs?hostname=${HOSTNAME}"

response=$(curl -s -w "%{http_code}" -o response.json "$QUERY_URL")
http_code=$(tail -n1 <<< "$response")

if [ "$http_code" -ne 200 ]; then
  echo "查询日志失败: HTTP $http_code"
  exit 1
fi

jq -c '.[]' response.json | while read -r log_entry; do
  hostname=$(jq -r '.hostname' <<< "$log_entry")
  file=$(jq -r '.file_path' <<< "$log_entry")
  echo "主机名: $hostname"
  echo "文件: $file"
  echo "日志:"

  jq -r '.logs[]' <<< "$log_entry"
  echo "-------"
done

# 清理临时文件
rm -f response.json
