# 大作业

## 详情

- 作业1：实现⼀个主机的**CPU利⽤率**和**内存利用率**的采集系统。
- 作业2：扩展作业1，⽀持**采集主机的日志**。

## 架构

<img src="C:\Users\91475\AppData\Roaming\Typora\typora-user-images\image-20240614115556315.png" alt="image-20240614115556315" style="zoom:50%;" />

<img src="C:\Users\91475\AppData\Roaming\Typora\typora-user-images\image-20240614115609675.png" alt="image-20240614115609675" style="zoom:50%;" />

## 目录结构

<img src="C:\Users\91475\AppData\Roaming\Typora\typora-user-images\image-20240614115815759.png" alt="image-20240614115815759" style="zoom:67%;" />

- collector为采集器，有日志采集和利用率采集。
- mi-1是server端。
- vue为前端界面，进行总体控制。

## 运行

1. 进入collector文件夹，运行 docker build -t collector . 来构建镜像。（DockerFile已经写好了，jar包在jar目录下。）

<img src="C:\Users\91475\AppData\Roaming\Typora\typora-user-images\image-20240614120011220.png" alt="image-20240614120011220" style="zoom:67%;" />

2. 进入vue文件夹，运行 npm run dev 来启动前端。

3. 进入mi-1文件夹，启动Mi1Application.java。

全部运行以后，从前端操作，启动容器开启收集，可以实时查看信息，更换存储方式。

![30e282f5ce5593a41b9423334a43997](G:\wx_file\WeChat Files\wxid_nc1nv9xzcxja22\FileStorage\Temp\30e282f5ce5593a41b9423334a43997.png)

## 实现原理

### collector

<img src="C:\Users\91475\AppData\Roaming\Typora\typora-user-images\image-20240614120604947.png" alt="image-20240614120604947" style="zoom:67%;" />

分成三部分：

- log：日志部分，分为日志写入和日志监听两部分。日志写入对日志进行每秒100次的写入。
- rest：rest部分简单的封装了一下，是用restTemplate进行接口调用。
- utilization：利用率采集部分，每分钟对利用率进行采集。

#### log

日志部分分为日志写入和日志监听两部分。日志写入对日志进行每秒100次的写入。日志监听又分为监听和上传两个线程，监听负责对日志进行监听，上传负责对日志进行定时上传（这里为了防止日志过长，对日志进行了切分上传）。

监听的核心代码：

```java
private void watchLogFile() {
    try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
        Path logDir = Paths.get(logFilePath).getParent();
        logDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        while (watching) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    Path modifiedFile = (Path) event.context();
                    if (modifiedFile.toString().equals(fileName)) {
                        logQueue.offer(true);
                    }
                }
            }
            key.reset();
        }
    } catch (IOException e) {
        if (watching) {
            e.printStackTrace();
        }
    }
}
```

上传的核心代码：

```java
private void collectLogs() {
    if (logQueue.isEmpty()) {
        return;
    }

    try (RandomAccessFile raf = new RandomAccessFile(logFilePath, "r")) {
        raf.seek(lastReadPosition);
        String line;
        InetAddress localHost = InetAddress.getLocalHost();
        String hostName = localHost.getHostName();
        Log log = new Log();
        log.setHostname(hostName);
        log.setFile(logFilePath);
        log.setTimestamp(Instant.now().getEpochSecond());

        StringBuilder content = new StringBuilder();
        while ((line = raf.readLine()) != null) {
            content.append(line);
            if (content.length() >= 1024 * 1024) {
                log.setLog(content.toString());
                dataSender.sendLogData(log);
                content.delete(0, content.length());
            }
        }
        log.setLog(content.toString());
        dataSender.sendLogData(log);
        lastReadPosition = raf.getFilePointer();
        configReader.updateLastReadPosition(lastReadPosition);
        logQueue.clear();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

#### utilization

CPU利用率采集核心代码：

```java
public Float getCpuUtilization() {
    try {
        String[] values = statPointer.readLine().substring(STAT_FILE_HEADER.length()).split(" ");
        long idleTime = Long.parseUnsignedLong(values[3]);
        long totalTime = 0L;
        for (String value : values) {
            totalTime += Long.parseUnsignedLong(value);
        }
        Double cpuUtilization = (1 - ((double) idleTime) / totalTime) * 100;
        statPointer.seek(0);
        return Float.valueOf(percentFormatter.format(cpuUtilization));
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}
```

内存利用率采集核心代码：

```java
public Float getMemUtilization() throws IOException {
    String line;
    Map<String, Long> memInfoMap = new HashMap<>();
    while ((line = memPointer.readLine()) != null) {
        if (line.startsWith("MemTotal:") || line.startsWith("MemFree:")
                || line.startsWith("Buffers:") || line.startsWith("Cached:")) {
            String[] parts = line.split("\\s+");
            memInfoMap.put(parts[0], Long.parseLong(parts[1]));
        }
    }
    long memTotal = memInfoMap.getOrDefault("MemTotal:", 0L);
    long memFree = memInfoMap.getOrDefault("MemFree:", 0L);
    long buffers = memInfoMap.getOrDefault("Buffers:", 0L);
    long cached = memInfoMap.getOrDefault("Cached:", 0L);
    memPointer.seek(0);
    Float memUtilization = (1 - ((float) memTotal - (float) memFree - (float) buffers - (float) cached) / (float) memTotal) * 100;
    return Float.valueOf(percentFormatter.format(memUtilization));
}
```

### server

server主要是提供接口给collector来收集利用率和日志进行存储，以及提供给前端查询接口的工作，目录结构如下：

<img src="C:\Users\91475\AppData\Roaming\Typora\typora-user-images\image-20240614121227404.png" alt="image-20240614121227404" style="zoom:67%;" />

### 存储方式

利用率的存储方式统一使用的MySQL进行存储。日志的存储设置了两种方式：MySQL存储和ES存储，使用的简单工厂模式结合接口，可以在运行过程中对存储方式进行切换。定义了Storage接口有save和load两种方法，分别有ElasticStorage和MySQLStorage对其进行了实现。

这里是存储日志的核心代码：

```java
public interface Storage {
    void save(Log logs);

    LogQueryVO load(String hostname, String file, Integer currentPage, Integer pageSize);
}

public class ElasticsearchStorage implements Storage {
    private final ESGeneralDao esGeneralDao;

    @Autowired
    public ElasticsearchStorage(ESGeneralDao esGeneralDao) {
        this.esGeneralDao = esGeneralDao;
    }

    @Override
    public void save(Log logs) {
        try {
            esGeneralDao.putData(logs, ESConst.Index.LOG.value(),String.valueOf(IdGeneratorUtils.standAloneSnowFlake()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LogQueryVO load(String hostname, String file, Integer currentPage, Integer pageSize) {
        List<CompoundQuery> queries = new ArrayList<>();
        if (!hostname.equals("")) {
            queries.add(new CompoundQuery(CompoundType.MUST, QueryBuilders.termQuery("hostname", hostname)));
        }
        if (!file.equals("")) {
            queries.add(new CompoundQuery(CompoundType.MUST, QueryBuilders.termQuery("file", file)));
        }
        List<Map<String, Object>> maps = esGeneralDao.compoundQuery(ESConst.Index.LOG.value(), queries, currentPage - 1 , pageSize);
        maps.sort(Comparator.comparingLong(map -> ((Integer) map.get("timestamp")).longValue()));
        List<String> logs = new ArrayList<>();
        for (Map<String, Object> map : maps){
            logs.add((String) map.get("log"));
        }
        return LogQueryVO.builder().logs(logs).pageNum((int) Math.ceil(((double) maps.size() / pageSize))).totalNum(maps.size()).build();
    }
}
public class MySQLStorage implements Storage {
    private final LogMapper logMapper;

    @Autowired
    public MySQLStorage(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Override
    public void save(Log logs) {
        try {
            logMapper.insertLog(logs);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public LogQueryVO load(String hostname, String file, Integer currentPage, Integer pageSize) {
        try {
            List<Log> results = logMapper.getAllLogs(hostname, file);
            List<String> logs = new ArrayList<>();
            for (Log item : results){
                logs.add(item.getLog());
            }
            return LogQueryVO.builder().logs(logs).pageNum((int) Math.ceil(((double) results.size() / pageSize))).totalNum(results.size()).build();
        } catch (Exception e) {
            log.info("查询日志--MySQL数据库失败!");
        }
        return null;
    }
}
```

这里是存储模式切换核心代码：

```java
public class StorageClient {
    private String type;
    private final StorageFactory storageFactory;
    private Storage currentStorage;

    @Autowired
    public StorageClient(StorageFactory storageFactory,  @Value("${log.storage.type}") String type){
        this.storageFactory = storageFactory;
        this.currentStorage = storageFactory.getStorage(type); // 初始化存储
    }

    @Bean
    @Scope("prototype")
    public Storage storage() {
        return currentStorage;
    }

    public void setType(String type) {
        this.type = type;
        this.currentStorage = storageFactory.getStorage(type);
    }

    public String getType() {
        return type;
    }
}
```

```java
public class StorageFactory {
    private final MySQLStorage mySQLStorage;
    private final ElasticsearchStorage elasticsearchStorage;

    @Autowired
    public StorageFactory(MySQLStorage mySQLStorage, ElasticsearchStorage elasticsearchStorage) {
        this.mySQLStorage = mySQLStorage;
        this.elasticsearchStorage = elasticsearchStorage;
    }

    public Storage getStorage(String storageType) {
        switch (storageType.toLowerCase()) {
            case "mysql":
                return mySQLStorage;
            case "elasticsearch":
                return elasticsearchStorage;
            default:
                throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
    }
}
```