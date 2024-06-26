# 1.项目描述
    *本项目在Ubuntu系统下,基于Golang开发，部署在Docker上运行，用于采集指定日志文件的日志；
    *项目通过collector端，每10秒检测一次日志文件，如果有新的数据写入，则通过upload接口上传数据到server端；
    *server端提供upload接口接收并存储数据到指定的存储模式中；提供query接口供查询数据；
    *用户可利用Shell脚本和query接口在终端上查询数据。
# 2.注意事项
    *存储模式可在/collector/configFile/config.json里配置，有"mysql","local_file","mongo"三种存储模式;
    *"local_file"存储路径：/server/localLogs/logs.txt;"mysql"数据库密码：123456,数据库名task;"mongo"数据库名：mangoDB,集合名：mongoCollection;
    *需要采集的日志文件可在/collector/configFile/config.json里配置，如果选择非/collector/configFile/路径下的其他日志文件，需要在docker-compose.yml里设置挂载路径；
    */collector/fileOffsets/里记录了文件偏移量，如果将存储数据清空，需将对应偏移量设置为0；
# 3.项目部署
## (1).安装Docker
    curl -fsSL https://test.docker.com -o test-docker.sh
    如果提示未找到命令curl,需安装：sudo apt-get install curl
    sudo sh test-docker.sh
    这一步与之后的compose运行可能会出现timeout错误，建议配置镜像源或网络代理。
## (2).安装docker-compose
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.2.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    添加可执行权限：
    sudo chmod +x /usr/local/bin/docker-compose
## (3).进入到项目根目录，启动项目
    sudo docker-compose up --build
    此时会提示找不到MySQL表，需进行配置
## (4).MySQL配置
    找到MySQL容器id:sudo docker ps -a
    进入MySQL：sudo docker exec -it (ID) /bin/bash
    创建数据库：CREATE DATABASE IF NOT EXISTS task;
    进入task库：use task;
    创建表：
    CREATE TABLE hosts_logs (
        id INT AUTO_INCREMENT PRIMARY KEY,
        hostname VARCHAR(255) NOT NULL,
        file VARCHAR(255) NOT NULL
        );

    CREATE TABLE log_entries (
        id INT AUTO_INCREMENT PRIMARY KEY,
        host_log_id INT,
        log_entry TEXT NOT NULL,
        FOREIGN KEY (host_log_id) REFERENCES hosts_logs(id)
        );
## (5).项目部署完成

# 4.项目使用
    安装jq工具：`sudo-apt-get install jq`
    在项目根目录执行:添加脚本执行权限：`chmod +x reader.sh`
    在项目根目录执行：./reader.sh 根据提示进行查询
    查询支持相关数据缺失，以查询对应所有值的数据
