package com.example.mi1.service.impl;

import com.example.mi1.domain.po.Log;
import com.example.mi1.domain.vo.LogQueryVO;
import com.example.mi1.mapper.LogMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MySQLStorageTest {

    @Mock
    private LogMapper logMapper;

    @InjectMocks
    private MySQLStorage mySQLStorage;

    /**
     * @Author: 段蓉蓉
     * @Date: 2024/6/10 10:26
     * 在每个测试方法执行之前，确保所有用 @Mock 和 @Spy 注解标记的字段都已被初始化。这样，在测试方法中可以直接使用这些模拟对象，而不需要每次手动初始化它们
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * @Author: 段蓉蓉
     *  @Date: 2024/6/10 10:26
     * 编写一个 JUnit 测试方法，用于测试 mySQLStorage.save 方法的行为。
     * 测试方法应该使用 Mockito 框架来模拟 LogMapper 类的 insertLog 方法的行为。
     * 然后，使用 Mockito 的 verify 方法来验证 insertLog 方法是否被调用了一次，并传入了一个 Log 对象。
     * 最后，测试方法应该通过验证来确认 save 方法的行为符合预期。
     */
    @Test
    public void testSave() {
        Log log = new Log();
        log.setLog("Test log message");
        // 使用了 Mockito 框架中的 doNothing() 方法，这意味着当 logMapper 对象的 insertLog 方法被调用，并且传入任何 Log 对象时，不执行任何操作。
        // logMapper 是你测试类中的一个模拟对象（Mock），这样你可以控制其行为，而不实际调用真实的数据库插入操作。
        doNothing().when(logMapper).insertLog(any(Log.class));

        mySQLStorage.save(log);

        // 使用 Mockito 框架的 verify 方法来检查 logMapper 对象的 insertLog 方法是否被调用了一次，并且传入的参数是之前创建的 Log 对象。
        // verify 用于验证模拟对象的方法是否被期望的次数调用，times(1) 表示期望被调用一次。
        verify(logMapper, times(1)).insertLog(log);
    }


    /**
     * @Author: 段蓉蓉
     *  @Date: 2024/6/10 10:26
     * 编写一个 JUnit 测试方法，用于测试 mySQLStorage.load 方法的行为。
     */
    @Test
    public void testLoad() {
        String hostname = "testHost";
        String file = "testFile";
        int currentPage = 1;
        int pageSize = 10;

        List<Log> logList = new ArrayList<>();

        // 使用 Mockito 的 when 方法，当 logMapper 的 getAllLogs 方法被调用，并且参数为 hostname 和 file 时，返回 logList 列表。logMapper 是一个模拟对象。
        when(logMapper.getAllLogs(hostname, file)).thenReturn(logList);

        LogQueryVO result = mySQLStorage.load(hostname, file, currentPage, pageSize);

        // 使用 JUnit 的断言方法验证 result 是否符合预期
        assertNotNull(result);
        assertEquals(0, result.getTotalNum());
        assertEquals(0, result.getPageNum());
        assertEquals(0, result.getLogs().size());

        // 使用 Mockito 的 verify 方法验证 logMapper 的 getAllLogs 方法是否被调用一次，并且参数为 hostname 和 file。
        verify(logMapper, times(1)).getAllLogs(hostname, file);
    }
}
