# ��ҵһ
## ��Ŀ����
��Ŀ��������ģ��collector��serverģ�飬����collectorģ�鸺��ʱ�ɼ�������������Ϣ�������͸�API�ӿ�upload��
serverģ��ʵ��upload�ӿ�������collectorģ���ϴ�����Ϣ�����洢��Mysql���ݿ��У�ͬʱ��Ҫ�����µ�ʮ�����ݱ��浽Redis�У���ʵʱ�����������ݡ�
����springboot��Ŀ���л�����jdk17��Mysql5.7.19��Redis5.0�½��С�
## ��Ŀ����
����DockerHub��ʱ�޷���ȡ��δ��ʱ����docker��ʹ�ã�
ͨ������õ�jar�����ֱ���������jar����
### 1������severģ��
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

