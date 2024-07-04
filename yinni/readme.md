# Docker和LVS搭建指南

## 一、Docker环境搭建

### 1. 创建两台Nginx容器并挂载本地配置文件、HTML文件和日志文件

#### 步骤：
1. 创建Nginx容器1：
   ```sh
   docker run -d --name nginx1 -v /ssh/conf:/etc/nginx/conf.d -v /ssh/html:/usr/share/nginx/html -v /ssh/logs:/var/log/nginx nginx
2. 创建Nginx容器2：
    ```sh
   docker run -d --name nginx2 -v /ssh/conf:/etc/nginx/conf.d -v /ssh/html:/usr/share/nginx/html -v /ssh/logs:/var/log/nginx nginx
### 2. Nginx配置基于域名的虚拟主机

创建Nginx配置文件 `/ssh/conf/www.example.com.conf`：

```nginx
server {
    listen 80;
    server_name www.example.com;

    location / {
        root /usr/share/nginx/html;
        index index.html;
    }

    location /hello.html {
        alias /usr/share/nginx/html/hello.html;
    }

    location /mi.html {
        alias /usr/share/nginx/html/mi.html;
    }

    error_page 404 /404.html;
        location = /40x.html {
    }

    error_page 500 502 503 504 /50x.html;
        location = /50x.html {
    }
}
```
创建Nginx的html文件 `/ssh/html/mi.html`：
```html
<!DOCTYPE html>
<html>
<head>
    <title>MI</title>
</head>
<body>
    I lLOVE XIAOMI
</body>
</html>

```
### 3.创建MySQL容器
```Mysql
docker run --name mysql -e MYSQL_ROOT_PASSWORD=123 -d mysql:latest
```
## 二、LVS-NAT配置
### 1. LVS负载调度器配置

#### 环境说明：

- 内网IP：192.168.44.103 (ens33)
- 外网IP：172.27.22.100 (ens36)
- Nginx服务器IP：192.168.44.101、192.168.44.102

#### 关闭所有虚拟机的防火墙和SELinux

```sh
systemctl stop firewalld
setenforce 0
```
### 2. LVS虚拟机配置

#### 步骤：

1. 添加新的网卡 `ens36` 并将 `ens33` 和 `ens36` 的网关都设置成自己。
2. 开启路由转发功能：

    ```sh
    echo 1 > /proc/sys/net/ipv4/ip_forward
    ```

3. 安装 `ipvsadm`：

    ```sh
    yum install ipvsadm -y
    ```

4. 配置 `ipvsadm`：

    编辑 `/etc/sysconfig/ipvsadm` 文件：

    ```sh
    -A -t 172.27.22.100:80 -s rr
    -a -t 192.168.22.100:80 -r 192.168.44.101:80 -m
    -a -t 192.168.22.100:80 -r 192.168.44.102:80 -m
    ```

### 3. 修改真实服务器网关地址

确保RIP和DIP在同一个IP网络，并使用私网地址；将真实服务器（RS）的网关指向DIP。
