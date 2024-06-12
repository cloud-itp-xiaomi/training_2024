# 数据采集系统
## 功能要求
...

## 项目介绍
1. DataItem是数据库存数据的类型
2. result包的作用是统一返回结果
3. 连接数据库的操作，主要在DataMapper、IDataService、DataService中
4. 单元测试在test文件夹中
5. 配置在.properties文件中，依赖在pom.xml中
6. 代码规范有待优化

## 部署与运行
1. 打jar包，在target目录下
2. 放进虚拟机
3. 在docker中拉镜像和容器，运行容器