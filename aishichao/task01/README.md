# 1.项目描述
    本项目在Ubuntu系统下,基于Golang开发，部署在Docker上运行，用于采集计算机cpu和内存利用率；
    项目通过collector端，每分钟采集一次数据，并使用upload接口上传数据到server端；
    server端提供upload接口接收并存储数据到MySQL和Redis；提供query接口供查询数据；
    用户可利用Shell脚本和query接口在终端上查询数据。
# 2.项目部署
## (1).安装Docker
    curl -fsSL https://test.docker.com -o test-docker.sh
    如果提示未找到命令curl,需安装：sudo apt-get install curl
    sudo sh test-docker.sh
    这一步与之后的compose安装和运行可能会出现timeout错误，建议配置镜像源或网络代理。
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
    创建表：create table metric(
             id int AUTO_INCREMENT PRIMARY KEY,
             Metric varchar(255) not null,
             Endpoint varchar(255) not null,
             Timestamp bigint not null,
             Step bigint not null,
             Value double precision not null);
## (5).项目部署完成

# 3.项目使用
    安装jq工具：`sudo-apt-get install jq`
    在项目根目录执行:添加脚本执行权限：`chmod +x reader.sh`
    在项目根目录执行：./reader.sh 根据提示进行查询
    查询支持相关数据缺失，以查询对应所有值的数据
