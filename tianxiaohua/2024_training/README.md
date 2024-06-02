田小花2024_training

host-collection-system项目

- collector：实现采集系统cpu和内存利用率，并上报给service
- interface：dubbo接口，提供数据上报接口，用于service模块和collector模块通信
- service：实现数据存储到mysql和redis，并进行查询



