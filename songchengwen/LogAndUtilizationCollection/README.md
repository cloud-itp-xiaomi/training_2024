# ��ҵһ
## ��Ŀ����
��Ŀ��������ģ��collector��serverģ�飬����collectorģ�鸺��ʱ�ɼ�������������Ϣ�������͸�API�ӿ�upload��
serverģ��ʵ��upload�ӿ�������collectorģ���ϴ�����Ϣ�����洢��Mysql���ݿ��У�ͬʱ��Ҫ�����µ�ʮ�����ݱ��浽Redis�У���ʵʱ�����������ݡ�
����springboot��Ŀ���л�����jdk17��Mysql5.7.19��Redis5.0�½��С�
## ��Ŀ����
����DockerHub��ʱ�޷���ȡ��δ��ʱ����docker��ʹ�ã�
ͨ������õ�jar�����ֱ���������jar����
### 1������serverģ��
```java
java -jar server-0.0.1-SNAPSHOT.jar
```
### 2������collectorģ��
```java
java -jar collector-0.0.1-SNAPSHOT.jar
```
### 3����collector�����һ���ֶ������ɼ��Ľӿڣ����Լ����Ƿ������ɼ�����

http://localhost:8080/api/collector/collect

### 4����ѯ�ӿ�

��ѯ�ض�ָ���������Ӧ�����ݣ�����metric=cpu.used.percent

http://localhost:8081/api/metric/query?endpoint=my-computer&start_ts=1718700358999&end_ts=1718700358999&metric=cpu.used.percent

��ѯ����ָ���������Ӧ������

http://localhost:8081/api/metric/query?endpoint=my-computer&start_ts=1718700358999&end_ts=1718700358999

# ��ҵ��
## ��Ŀ����
��Ŀ����ҵһ�Ļ����Ͻ������䣬����ʵ������־�仯��ʵʱ������־��Ϣ�Ĵ洢��
��Ŀ��������ģ��collector��serverģ�飬����collectorģ�鸺��λ�������ļ�cfg.json�����ݣ��������������̼߳�⵽�����ļ���ָ����־�ı仯��Ϣ�����б仯��������������־��Ϣ���͸�Log��API�ӿ�upload��
serverģ�鱣��collectorģ���ϴ�����־��Ϣ��ʵ�����ִ洢���ԣ�local_file��Mysql�������Ҷ�Mysql�洢��ʽ����־��Ϣ���в�ѯ��ʵ����query�ӿڶ���־������Ϣ�Ĳ�ѯ��
����springboot��Ŀ���л�����jdk17��Mysql5.7.19��Redis5.0�½��С�
## ��Ŀ����
ͨ������õ�jar�����ֱ���������jar��������δ����Docker�У�
### 1���������ļ����޸���־��Ϣ�Ĵ洢��ʽ
��ʱ��ʵ��local_file��mysql���ִ洢��ʽ
### 2������serverģ��
```java
java -jar server-0.0.2-SNAPSHOT.jar
```
### 3������collectorģ��
```java
java -jar collector-0.0.2-SNAPSHOT.jar
```
### 4����ѯ�ӿ�
��ʹ��mysql�洢��ʽʱ������ʹ�ò�ѯ�ӿڡ�
��ѯ��Ӧ�����Ͷ�Ӧ��־�ļ���������Ϣ��
http://localhost:8081/api/log/query?hostname=my-computer&file='path/to/log'
### 5���ϱ��ӿ�
�����ֶ��ϱ�һ����־��Ϣ��
http://localhost:8081/api/log/upload?hostname=my-computer&file="path/to/log"&logs="content"&logStorage="local_file/mysql"


