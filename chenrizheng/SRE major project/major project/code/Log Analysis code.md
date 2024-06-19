## Log Analysis code

```bash
#!/bin/bash
#创建临时文件
touch /var/lib/mysql-files/nginx.logs
touch /root/nginx.log
> /root/nginx.log
> /var/lib/mysql-files/nginx.logs

#awk命令结合关联数组来分析统计nginx服务器日志
awk '
{
match($0,/\[([^\]]+)/,arr);#match函数提取时间戳
full_timestamp = substr(arr[1],1,length(arr[1]));#去掉默认格式的方括号
split(full_timestamp,timeparts,"[: /]");#提取年月日小时分钟

#构造时间键
day = timeparts[1];

#将日志中默认的英文缩写月份改为对应阿拉伯数字
if(timeparts[2] == "Jun"){
	mon = 6;
}
if(timeparts[2] == "Jan"){
	mon = 1;
}
if(timeparts[2] == "Feb"){
	mon = 2;
}
if(timeparts[2] == "Mar"){
	mon = 3;
}
if(timeparts[2] == "Apr"){
	mon = 4;
}
if(timeparts[2] == "May"){
	mon = 5;
}
if(timeparts[2] == "Jul"){
	mon = 7;
}
if(timeparts[2] == "Aug"){
	mon = 8;
}
if(timeparts[2] == "Sep"){
	mon = 9;
}
if(timeparts[2] == "Oct"){
	mon = 10;
}
if(timeparts[2] == "Nov"){
	mon = 11;
}
if(timeparts[2] == "Dec"){
	mon = 12;
}
year = timeparts[3];
hour = timeparts[4];
minute = timeparts[5];
time_key = year"-"mon"-"day" "hour":"minute;
status = $9#状态码

#更新每分钟的请求数以及对应的状态码
requests[time_key]++
if(status == 200){
	status_200[time_key]++;
}
else if(status >= 500){
	status_500[time_key]++;
}
}

#遍历数组并打印结果
END {
for(t in requests){
	qps = requests[t];
	status_200_count = status_200[t] + 0;
	status_500_count = status_500[t] + 0;
	print t,"/",qps,"/",status_200_count,"/",status_500_count;
}
}
' /var/log/nginx/access.log > /root/nginx.log

#sort命令对结果按时间顺序排序
sort -t: -k1,1n /root/nginx.log > /var/lib/mysql-files/nginx.logs
cat /var/lib/mysql-files/nginx.logs
scp /var/lib/mysql-files/nginx.logs root@192.168.222.132:/var/lib/mysql-files/

#删除临时文件
rm -rf /root/nginx.log
rm -rf /var/lib/mysql-files/nginx.logs

#连接远程MySQL服务并执行SQL语句
mysql -h192.168.222.132 -uroot -pCrz666.. < /root/sql_file
```

