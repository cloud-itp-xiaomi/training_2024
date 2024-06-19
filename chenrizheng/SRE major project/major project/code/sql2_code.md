## nginx2 sql_file

```sql
#nginx服务器192.168.222.135
#创建数据库
create database if not exists logs;
use logs;

#创建表，设置自增主键，约束时间不重复
create table if not exists log1(
	id int AUTO_INCREMENT primary key,
	time datetime UNIQUE,
	qps int,
	status_200_count int,
	status_500_count int
	);
#创建一个没有UNIQUE约束的临时表
create table log1_temp like log1;
alter table log1_temp drop index time;

#load data infile 命令导入shell脚本分析好的内容到临时表	
load data infile '/var/lib/mysql-files/nginx.logs'
	into table log1_temp
	fields terminated by '/'
	lines terminated by '\n'
	(
	@time,
	qps,
	status_200_count,
	status_500_count
	)
set time = str_to_date(@time, '%Y-%m-%d %H:%i');

#将数据从临时表转到实际表，忽略重复时间
insert into log1 (time, qps, status_200_count, status_500_count)  
select time, qps, status_200_count, status_500_count  
from log1_temp  
on duplicate key update  
    qps = values(qps),  
    status_200_count = values(status_200_count),  
    status_500_count = values(status_500_count);  

#展示导入结果
select * from log1;
#删除临时表
drop table log1_temp;
```



