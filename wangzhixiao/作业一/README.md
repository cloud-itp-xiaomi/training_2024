
1、登录MySQL数据库
 mysql -u root -p

2、创建数据库和用户
 CREATE DATABASE metrics;
 CREATE USER 'user'@'localhost' IDENTIFIED BY 'password';
 GRANT ALL PRIVILEGES ON metrics.* TO 'user'@'localhost';
 FLUSH PRIVILEGES;

3、启动reids
 ./bin/redis-server ./redis.conf

4、启动redis客户端
 ./bin/redis-cli

5、服务器端配置与运行
  - 初始化Go模块：go mod init server
  - 安装依赖包：go get github.com/go-redis/redis/v8 github.com/gorilla/mux github.com/go-sql-driver/mysql
  - 编写并运行服务器代码：go run server.go

6、Collector端配置与运行
 - 初始化Go模块：go mod init collector
 - 安装依赖包：go get
 - 编写并运行Collector代码：go run Collector.go

7、Reader端配置与运行
 - 初始化Go模块：go mod init reader
 - 安装依赖包：go get
 - 编写并运行Reader代码：go run Reader.go

8、验证数据库中是否存储到数据：
  在 MySQL 中，使用 SQL 查询语句来查看表中的数据，使用像 SELECT * FROM metrics; 这样的语句来检索所有的数据。
  在 Redis 中，使用 redis-cli 命令行工具来连接到 Redis 服务器，并执行 LRANGE metrics 0 -1 来获取列表中的所有数据。这会返回列表中的所有元素，你可以通过遍历这些元素来查看数据。
