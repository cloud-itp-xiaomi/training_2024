#!/bin/bash

# 设置Nginx access日志文件的路径
LOG_FILE="/var/log/nginx/access.log"

# 设置MySQL配置
MYSQL_USER="root"
MYSQL_PASS="@Qq020916"
MYSQL_DB="nginx_stats"
TMP_FILE="/tmp/nginx_stats.sql"

# 使用awk处理日志文件
awk ' {

# 提取时间戳和分钟部分
    timestamp = $4;
    gsub(/[\[\]:]/, " ", timestamp);
    split(timestamp, time_parts, " ");
    minute = time_parts[5];

# 初始化数组
    if (!(minute in status_200)) {
       status_200[minute] = 0;
    }
    if (!(minute in status_5xx)) {
       status_5xx[minute] = 0;
    if (!(minute in total_requests)) {
       total_requests[minute] = 0;
    }

# 更新状态码计数和总请求数
    status = $9;
    if (status == 200) {
       status_200[minute]++;
    } else if (status ~ /^5[0-9][0-9]$/) {
        status_5xx[minute]++;
    }

    total_requests[minute]++;
}

    # 打印结果

END {
    for (m in status_200) {
      print "INSERT INTO access_log_stats (minute, status_200, status_5xx, total_requests) VALUES (\"" m "\", " status_200[m]   ", " (m in status_5xx>
    }
}
' "$LOG_FILE" > "$TMP_FILE"

