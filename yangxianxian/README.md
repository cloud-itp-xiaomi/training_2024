### 一、项目描述

* 该项目首先是要用户通过www.example.com的方式访问lvs服务，lvs将请求均衡转发到nginx服务，然后根据不同请求路径/静态文件，nginx返回不同html文件内容。
* 然后通过shell脚本，分析2台nginx机器上的access 日志，把qps（每秒的请求数）和http code为200 和500+的数量按照1分钟统计出来， 都保存到mysql数据库中。

### 二、运行该项目所需环境

linux系统发行版本ubuntu、Ubuntu安装nginx、安装mysql

### 三、项目部署

#### 1、修改nginx的配置文件

* 首先下载nginx，它的默认配置文件保存在/etc/nginx/nginx.conf中。
* 打开/etc/nginx/nginx.conf文件，了解Nginx的主要配置结构。
* Nginx的配置文件采用块结构，主要分为events、http和servers等块。在http块中，包含全局的HTTP配置，而servers块包含了虚拟主机的配置。
* 修改/etc/nginx/nginx.conf配置文件：

`user www-data;`
`worker_processes auto`;
`pid /run/nginx.pid;`
`#include /etc/nginx/modules-enabled/*.conf;`

`events {
    worker_connections 1024;
}`
`http {
      include mime.types;
      default_type text/html;
      sendfile on;
      keepalive_timeout 65;
`
      `server {
        listen 80;
        root /var/www/html;
        location /hello.html {
           index hello.html hello.htm;
        }
        location /mi.html  {
           index mi.html mi.htm;
       }
    }`
`}`
其中server块包括了监听端口，静态网站文件地址。

#### 2、创建.html文件
* 在html目录下面先复制nginx自带的网站文件index.nginx-debian.html;
* 修改名称为hello.html和mi.html，然后根据自己需要的内容进行修改。
* hello.html文件配置如下：（mi.html的配置类似）
<!DOCTYPE html>
<html>
<head>
<title>Hello world!</title>
<style>
    body {
        width: 35em;
        margin: 0 auto;
        font-family: Tahoma, Verdana, Arial, sans-serif;
    }
</style>
</head>
<body>
<h1>Hello world!</h1>
</body>
</html>

#### 3、搭建负载均衡

* Nginx的负载均衡配置主要通过nginx.conf配置文件中的upstream模块和server模块的配合使用来实现。
* 修改配置文件：主要是在之前的基础上添加两个server段，监听8081和8082端口，并分别执行对应的测试文件。

`http {

      include mime.types;
      default_type text/html;
      sendfile on;
      keepalive_timeout 65;

      server {
          listen 8081;
          root /var/www/hello.html;
          location /hello.html {
           index hello.html hello.htm;
        }
        
    }

      server {
          listen 8082;
          root /var/www/mi.html;
          location /mi.html  {
          index mi.html mi.htm;
    }
}`

#### 4、提取分析日志

* 进行lvs搭建完之后，在浏览器可以通过自己的域名访问不同的html文件。
* 接下来在nginx利用cat查看自己的分析日志，可以获取时间戳、状态返回值等日志结果，这正是我们需要的。
* 通过shell脚本，编写一个提取分析日志的脚本。
* 提取的难点我认为是时间戳的获取，时间戳[16/Jun/2024:19:51:13 +0800]，可以看到它符号比较多，很容易搞错！
* 关于提取时间戳的部分：
- 使用gsub函数移除时间戳中的方括号和冒号。
- 使用split函数将时间戳分割为多个部分，并取第五个部分作为分钟。

代码：

`#!/bin/bash`

`# 设置Nginx access日志文件的路径
LOG_FILE="/var/log/nginx/access.log"
`
`awk ' {
    # 提取时间戳和分钟部分
    timestamp = $4;
    gsub(/[\[\]:]/, " ", timestamp);
    split(timestamp, time_parts, " ");
    minute = time_parts[5];`

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
`}`

`# 在 END 块中打印结果`
`END {
    for (m in status_200) {
        print "Status 200 count at minute " m ": " status_200[m];
        print "Total requests at minute " m ": "total_requests[m];
    }`

    for (m in status_5xx) {
         print "Status 5xx count at minute " m ": " status_5xx[m];
         print "Total requests (including non-5xx) at minute " m " " total_requests[m];
    }

`}
' "$LOG_FILE"`

#### 5、将数据导入到mysql

* 导入的前提是在mysql有一个用户，且有充分的权限，这样才能查看mqsql中的数据库，并进行导入导出的操作.
* 首先要在mysql创建一个数据库，然后再数据库中创建一个表，要保证表的格式与我们提取的分析日志的格式相匹配，不然很有可能导不进去。

创建表的代码如下：
`CREATE TABLE nginx_stats (  
    id INT AUTO_INCREMENT PRIMARY KEY,  
    timestamp DATETIME NOT NULL,  
    qps FLOAT NOT NULL,  
    http_200 INT NOT NULL,  
    http_500_plus INT NOT NULL  
);`

然后根据自己的用户信息，与mysql建立连接，检查是否连接成功：

`# 检查 MySQL 连接`
``CHECK_CMD="mysql -h \"$DB_HOST\" -u \"$DB_USER\" -p\"$DB_PASS\" -e 'SELECT 1;' \"$DB_NAME\""
 if ! $CHECK_CMD; then
 echo "Error: Unable to  connect to MySQL"
 exit 1
 fi```

导入的代码如下：
`# 使用mysql命令将数据插入到MySQL数据库中`
`END {
    for (m in status_200) {`
     ` cmd = sprintf("INSERT INTO minute_stats (minute, status_200, status_5xx, total_requests) VALUES ('%s', %d, %d, %d) ON DUPLICATE >
      system("mysql -h \"$DB_HOST\" -u \"$DB_USER\" -p\"$DB_PASS\" \"$DB_NAME\" -e \"$cmd\"")
    }`
`}`

#### 6、整合代码

最后将日志分析提取与导入mysql的代码进行汇总整合。
