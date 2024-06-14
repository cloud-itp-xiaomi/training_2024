package com.example.mi1.service.impl;

import com.example.mi1.dao.ESGeneral.ESGeneralDao;
import com.example.mi1.dao.constant.ESConst;
import com.example.mi1.domain.po.Log;
import com.example.mi1.domain.vo.LogQueryVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ElasticsearchStorageTest {

    @Mock
    private ESGeneralDao esGeneralDao;

    @InjectMocks
    private ElasticsearchStorage elasticsearchStorage;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * @Author: 段蓉蓉
     *  @Date: 2024/6/10 10:26
     * 编写一个 JUnit 测试方法，用于测试 elasticsearchStorageStorage.save 方法的行为。
     */
    @Test
    public void testSave() throws IOException {
        Log log = new Log();
        log.setLog("Test log message");
        doNothing().when(esGeneralDao).putData(any(), anyString(), anyString());
        elasticsearchStorage.save(log);
        verify(esGeneralDao, times(1)).putData(eq(log), eq(ESConst.Index.LOG.value()), anyString());
    }

    /**
     * @Author: 段蓉蓉
     *  @Date: 2024/6/10 10:26
     * 编写一个 JUnit 测试方法，用于测试 elasticsearchStorageStorage.load 方法的行为。
     */
    @Test
    public void testLoad() {
        String hostname = "testHost";
        String file = "testFile";
        int currentPage = 1;
        int pageSize = 10;

        List<Map<String, Object>> mockResults = new ArrayList<>();
        when(esGeneralDao.compoundQuery(eq(ESConst.Index.LOG.value()), anyList(), eq(currentPage - 1), eq(pageSize)))
                .thenReturn(mockResults);

        LogQueryVO result = elasticsearchStorage.load(hostname, file, currentPage, pageSize);

        assertNotNull(result);
        assertEquals(0, result.getTotalNum());
        assertEquals(0, result.getPageNum());
        assertEquals(0, result.getLogs().size());

        verify(esGeneralDao, times(1)).compoundQuery(eq(ESConst.Index.LOG.value()), anyList(), eq(currentPage - 1), eq(pageSize));
    }
}
