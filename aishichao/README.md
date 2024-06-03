# 1.项目描述
  本项目在Ubuntu20.04系统下,基于Golang开发，部署在Docker上运行，用于采集计算机cpu和内存的利用率，并可在Shell终端上查询数据。  
# 2.项目部署
  ## (1).安装Docker
      curl -fsSL https://test.docker.com -o test-docker.sh  
      如果提示未找到命令curl,需安装：sudo apt-get install curl  
      sudo sh test-docker.sh  
  ## (2).安装Docker Compose  
      sudo curl -L "https://github.com/docker/compose/releases/download/v2.2.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose  
      sudo chmod +x /usr/local/bin/docker-compose  
  ## (3).进入到项目根目录，启动项目  
      sudo docker-compose up --build
      此时会提示找不到MySQL表，需进行配置  
  ## (4).MySQL配置  
      找到MySQL容器id:sudo docker ps -a  
      进入MySQL：sudo docker exec -it <Container_ID> /bin/bash  
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
  (1).安装jq工具：`sudo-apt-get install jq`</p>  
  (2).在项目根目录执行:添加脚本执行权限：`chmod +x reader.sh`</p>   
  (3).在项目根目录执行：./reader.sh 根据提示进行查询</p>  
      
