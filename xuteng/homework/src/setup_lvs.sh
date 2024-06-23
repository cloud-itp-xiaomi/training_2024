#!/bin/bash

# 虚拟IP地址（VIP）
VIP=192.168.137.129

# 真实服务器（Real Servers）的IP地址和端口
RS1=192.168.137.129:8080
RS2=192.168.137.129:8081
RS3=192.168.137.131:8080

# 清除现有的LVS配置
ipvsadm --clear

# 添加虚拟服务，使用轮询算法（round-robin）
ipvsadm -A -t $VIP:80 -s rr

# 添加服务器到虚拟服务中
ipvsadm -a -t $VIP:80 -r $RS1 -m
ipvsadm -a -t $VIP:80 -r $RS2 -m
ipvsadm -a -t $VIP:80 -r $RS3 -m

# 显示LVS配置
ipvsadm -Ln

