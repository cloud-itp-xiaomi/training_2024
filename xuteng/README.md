# 2024_training


学习笔记：https://iaumfbljev8.feishu.cn/wiki/PZSJwNhfSiUoKMkXd9gc9uq1nCd?from=from_copylink

大作业：https://github.com/Chocolate2323/training_2024/tree/main/xuteng/homework

编码规范作业：https://github.com/Chocolate2323/training_2024/tree/main/xuteng/codingStyle



# 作业要求：

nginx负载均衡搭建，QPS和http code统计分析。

流程图：https://e4w9oyo2lk.feishu.cn/docx/Eyordf47PocbOnxI8HCcKF88nMg#SRa6dn3ezoe1Xwx2WsuchwUunjf

组件说明：https://e4w9oyo2lk.feishu.cn/docx/Eyordf47PocbOnxI8HCcKF88nMg#KNXbdvlhzoAN85xhS35cJD5OnUD

场景描述：
1.用户通过www.example.com的方式访问lvs服务，lvs将请求均衡转发到nginx服务，然后根据不同请求路径/静态文件，nginx返回不同html文件内容。
例如：
 www.example.com/hello.html  返回html内容hello world
 www.example.com/mi.html      返回html内容i love xiaomi
2.通过shell或者python脚本，去分析2台nginx机器上的access 日志，把qps（每秒的请求数）和http code为200 和500+的数量按照1分钟统计出来， 都保存到mysql数据库中。
