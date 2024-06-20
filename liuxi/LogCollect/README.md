# 作业二
## 项目详情
作业二整合了SpringBoot、MySQL与RocketMQ,collector模块负责监听日志文件和配置文件,是RocketMQ的生产者<br/>
collector模块监听到日志文件新增日志后，会读取新增日志并包装成消息发送至消息队列”HOST_LOG_TOPIC“主题中<br/>
collector模块监听到配置文件cfg.json中存储方式发生变化后，会发送消息至消息队列”HOST_CONFIG_TOPIC“主题中<br/>
server模块在消费主题为”HOST_LOG_TOPIC“的消息时，会根据存储日志的方式对日志进行持久化<br/>
server模块在消费主题为”HOST_CONFIG_TOPIC“的消息时，会改变存储日志的方式<br/>
该项目所使用的MySQL与RocketMQ组件部署在Docker中<br/>
## 运行
1. 创建并运行MySQLl容器
   ```
   docker pull mysql:latest
   docker run -d -p 3306:3306 -v /usr/local/mysql/conf:/etc/mysql/conf.d -v /usr/local/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 --name  mysql mysql:latest
2. 创建并运行Redis容器(如需同时采集主机利用率则启动该容器)
   ```
   docker pull redis:latest
   docker run -p 6379:6379 --name redis --privileged=true -v /home/UtilizeApp/config/redis/redis.conf:/etc/redis/redis.conf -v /home/UtilizeApp/data/redis:/data/redis -d redis redis-server /etc/redis/redis.conf
3. 创建并运行RocketMQ相关容器
   ```
   docker pull rocketmqinc/rocketmq
   docker run -d -p 10911:10911 -p 10909:10909 -v  /home/UtilizeApp/log/broker:/root/logs -v   /home/UtilizeApp/data/broker:/root/store -v  /home/UtilizeApp/config/broker/broker.conf:/opt/rocketmq/conf/broker.conf --name rmqbroker --link
   rmqnamesrv:namesrv -e
   "NAMESRV_ADDR=namesrv:19876" -e "MAX_POSSIBLE_HEAP=200000000" rocketmqinc/rocketmq sh mqbroker -c /opt/rocketmq/conf/broker.conf
   docker run -d -p 9876:9876 -v /home/UtilizeApp/log/namesrv:/root/logs -v /home/UtilizeApp/data/namesrv:/root/store --name rmqnamesrv -e "MAX_POSSIBLE_HEAP=100000000" rocketmqinc/rocketmq sh mqnamesrv
   docker run -d --name rmqconsole -p 8180:8180 --link rmqnamesrv:namesrv -e "JAVA_OPTS=-Drocketmq.namesrv.addr=namesrv:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false"  -t styletang/rocketmq-console-ng
4. 在浏览器中输入 http://hostName:port/api/log/query?hostName=param1&file=param2即可访问query接口
##注意
如需监听windows主机中的文件时，由于其文件全路径在浏览器中直接输入会存在编码问题，在查询时需要使用%5C代替斜杠,例如：
http://localhost:8083/api/log/query?hostName=DESKTOP-F02F9FG&file=E%3A%5Cjava_study%5CjavaProjects%5ClogCollect%5Ccollector%5Csrc%5Cmain%5Cjava%5Ccom%5Clx%5Ccollector%5Clog%5Ca.log