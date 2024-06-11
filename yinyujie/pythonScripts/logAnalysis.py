#!/usr/bin/env python3
import re
from datetime import datetime, timedelta
import pymysql

conn = pymysql.connect(host='192.168.254.128', user='log_user', password='root', db='log_analysis')
cursor = conn.cursor()

machine_id = 'ALL_RS'

# 时间范围（前一分钟）
end_time = datetime.now()
start_time = end_time - timedelta(minutes=1)

# 正则匹配HTTP状态码
status_code_pattern = re.compile(r'\s(\d{3})\s')

# 计数器
requests_count = 0
status_200_count = 0
status_5xx_count = 0

# 日志文件路径为 /usr/local/logstash/logstash_filtered_logs.log
with open('/usr/local/logstash/logstash_filtered_logs.log') as file:
    for line in file:
         if start_time <= datetime.strptime(line.split()[5][1:], '%d/%b/%Y:%H:%M:%S') <= end_time:
            requests_count += 1
            match = status_code_pattern.search(line)
            if match:
                code = int(match.group(1))
                if code == 200:
                    status_200_count += 1
                elif 500 <= code < 600:
                    status_5xx_count += 1

qps = requests_count / (end_time - start_time).total_seconds()

# 存储到MySQL
query = "INSERT INTO access_logs (machine_id, qps, status_200, status_5xx) VALUES (%s, %s, %s, %s)"
cursor.execute(query, (machine_id, qps, status_200_count, status_5xx_count))
conn.commit()

cursor.close()
conn.close()