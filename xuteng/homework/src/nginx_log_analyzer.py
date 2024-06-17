import subprocess
import re
import pymysql
from datetime import datetime, timedelta

# MySQL数据库连接配置
db_config = {
    'host': 'localhost',
    'user': 'xuteng',
    'password': '123456',
    'database': 'nginx_logs'
}

# Docker容器名称和日志标识
container_logs = {
    'nginx1': 'nginx_1',
    'nginx2': 'nginx_2'
}

# 解析日志行的正则表达式
log_pattern = re.compile(r'(\S+) - - \[(.*?)\] "[A-Z]+ (.*?) HTTP/\d\.\d" (\d+) \d+ "(.*?)" "(.*?)"')

# 解析日志行
def parse_log_line(line):
    match = log_pattern.match(line)
    if match:
        return match.groups()
    return None

# 获取Docker容器的标准输出流
def get_container_logs(container_name):
    cmd = ['/usr/bin/docker', 'logs', container_name]
    process = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    stdout, stderr = process.communicate()
    return stdout.decode('utf-8')

# 处理日志内容
def process_logs(logs):
    log_lines = logs.split('\n')
    logs_data = []
    for line in log_lines:
        log = parse_log_line(line)
        if log:
            logs_data.append(log)
    return logs_data

# 分析日志并将结果保存到数据库
def analyze_and_save_to_database():
    conn = pymysql.connect(**db_config)
    cursor = conn.cursor()

    for server, container_name in container_logs.items():
        # 获取容器日志
        logs = get_container_logs(container_name)
        
        # 处理日志内容
        logs_data = process_logs(logs)
        
        # 初始化时间戳
        timestamp = None
        
        # 转换时间戳并统计
        timestamp_counts = {}
        for log in logs_data:
            timestamp = datetime.strptime(log[1], '%d/%b/%Y:%H:%M:%S %z')
            timestamp = timestamp.replace(tzinfo=None)  # 移除时区信息
            timestamp_key = timestamp.replace(second=0, microsecond=0)
            if timestamp_key in timestamp_counts:
                timestamp_counts[timestamp_key]['total'] += 1
                if log[3] == '200':
                    timestamp_counts[timestamp_key]['http_200'] += 1
                elif int(log[3]) >= 500:
                    timestamp_counts[timestamp_key]['http_500_plus'] += 1
            else:
                timestamp_counts[timestamp_key] = {'total': 1, 'http_200': 0, 'http_500_plus': 0}
                if log[3] == '200':
                    timestamp_counts[timestamp_key]['http_200'] += 1
                elif int(log[3]) >= 500:
                    timestamp_counts[timestamp_key]['http_500_plus'] += 1
        
        # 插入数据库
        for timestamp, counts in timestamp_counts.items():
            try:
                query = """
                    INSERT INTO nginx_logs (server, timestamp, qps, http_200, http_500_plus)
                    VALUES (%s, %s, %s, %s, %s)
                """
                cursor.execute(query, (server, timestamp, counts['total'], counts['http_200'], counts['http_500_plus']))
            except pymysql.IntegrityError as e:
                # 如果是唯一约束错误，则忽略
                if e.args[0] == 1062:
                    pass
                else:
                    raise

    conn.commit()
    cursor.close()
    conn.close()

if __name__ == '__main__':
    analyze_and_save_to_database()
    