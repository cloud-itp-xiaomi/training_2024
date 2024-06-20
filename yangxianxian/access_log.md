#!/bin/bash

# 设置Nginx access日志文件的路径
LOG_FILE="/var/log/nginx/access.log"

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
    }
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

# 在 END 块中打印结果
END {
    for (m in status_200) {
        print "Status 200 count at minute " m ": " status_200[m];
        print "Total requests at minute " m ": "total_requests[m];
    }

    for (m in status_5xx) {
         print "Status 5xx count at minute " m ": " status_5xx[m];
         print "Total requests (including non-5xx) at minute " m " " total_requests[m];
    }

}
' "$LOG_FILE"

