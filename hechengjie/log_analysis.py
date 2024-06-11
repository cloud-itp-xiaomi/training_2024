import os
import re
import pymysql
import time
import subprocess

# 定义容器名称
RealServer1 = "RealServer1"
RealServer2 = "RealServer2"

# 定义容器内的日志文件路径
container_log_file = "/var/log/nginx/access.log"

# 定义主机日志文件路径
log_file_1 = "/home/kali/Downloads/LVS-NAT_nginx_shell/RealServer1_access.log"
log_file_2 = "/home/kali/Downloads/LVS-NAT_nginx_shell/RealServer2_access.log"

# 复制日志文件到主机路径
subprocess.run(["docker", "cp", f"{RealServer1}:{container_log_file}", log_file_1])
subprocess.run(["docker", "cp",f"{RealServer2}:{container_log_file}", log_file_2])

# 连接到MySQL数据库
db = pymysql.connect(host="localhost", user="root", password="1234", database="LVS_DATA")
cursor = db.cursor()

# 清空之前插入的数据
cursor.execute("TRUNCATE TABLE nginx_stats")

# 创建数据库表
cursor.execute("CREATE TABLE IF NOT EXISTS nginx_stats (server_name VARCHAR(50), timestamp DATETIME, url VARCHAR(255), qps INT, http_200 INT, http_500 INT)")

# 解析单个日志文件
def parse_log_file(log_file, server_name):
    url_stats = {}  # 存储不同网址的统计信息

    with open(log_file, 'r') as file:
        for line in file:
            if "GET" in line:    #filebeat
                timestamp = re.search(r'\[(.*?)\]', line).group(1)
                timestamp = time.strptime(timestamp, "%d/%b/%Y:%H:%M:%S %z")
                timestamp = time.strftime('%Y-%m-%d %H:%M:00', timestamp)  # 舍弃秒数，只保留分钟
                
                
                url = re.search(r'\"GET\s(.*?)\sHTTP', line).group(1)
                status_code = re.search(r'\s(\d{3})\s', line).group(1)

                if server_name not in url_stats:
                    url_stats[server_name] = {}
                if timestamp not in url_stats[server_name]:
                    url_stats[server_name][timestamp] = {}
                if url not in url_stats[server_name][timestamp]:
                    url_stats[server_name][timestamp][url] = {"qps": 0, "http_200": 0, "http_500": 0}

                url_stats[server_name][timestamp][url]["qps"] += 1
                if status_code == "200":
                    url_stats[server_name][timestamp][url]["http_200"] += 1
                elif status_code.startswith("5"):
                    url_stats[server_name][timestamp][url]["http_500"] += 1

    return url_stats

# 解析日志文件并将统计结果插入到数据库
def process_log_files(log_files):
    for log_file, server_name in log_files:
        url_stats = parse_log_file(log_file, server_name)

        # 将统计结果插入到数据库
        for server_name in url_stats:
            for timestamp in url_stats[server_name]:
                for url in url_stats[server_name][timestamp]:
                    qps = url_stats[server_name][timestamp][url]["qps"]
                    http_200 = url_stats[server_name][timestamp][url]["http_200"]
                    http_500 = url_stats[server_name][timestamp][url]["http_500"]
                    insert_query = "INSERT INTO nginx_stats (server_name, timestamp, url, qps, http_200, http_500) VALUES (%s, %s, %s, %s, %s, %s)"
                    cursor.execute(insert_query, (server_name, timestamp, url, qps, http_200, http_500))

    db.commit()

# 日志文件列表
log_files = [(log_file_1, "RealServer1"), (log_file_2, "RealServer2")]

# 处理日志文件
process_log_files(log_files)

# 关闭数据库连接
db.close()
