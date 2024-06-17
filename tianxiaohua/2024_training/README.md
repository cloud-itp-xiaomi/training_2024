田小花2024_training

host-collection-system项目

- collector：实现采集系统cpu和内存利用率，并上报给service
- interface：dubbo接口，提供数据上报接口，用于service模块和collector模块通信
- service：实现数据存储到mysql和redis，并进行查询



optimization-code项目

- 对示例代码getUserDirectories的优化

tdd-demo

- 对作业2 的测试驱动开发
- txh_test.go用于测试

host-collection-system-log

- 在作业1的基础上扩展，写的作业2
- collector：实现采集系统cpu、内存利用率和日志，并上报给service
- interface：dubbo接口，提供数据上报接口，用于service模块和collector模块通信
- service：实现数据存储到mysql和redis，日志也可以存储到elasticsearch并进行查询
