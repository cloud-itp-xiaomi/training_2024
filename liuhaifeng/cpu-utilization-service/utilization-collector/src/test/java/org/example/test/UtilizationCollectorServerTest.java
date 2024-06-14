package org.example.test;

import org.example.fegin.clients.UtilizationClient;
import org.example.fegin.pojo.Result;
import org.example.fegin.pojo.vo.UtilizationQueryVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 利用率采集服务测试类
 *
 * @author liuhaifeng
 * @date 2024/06/07/19:50
 */
@SpringBootTest
public class UtilizationCollectorServerTest {

    @Autowired
    private UtilizationClient utilizationClient;

    @Test
    public void QueryUtilizationTest() {
        Result<List<UtilizationQueryVO>> cpuUtilization = utilizationClient.query("my-computer", null, 1717228800L, 1718106896L);
        System.out.println(cpuUtilization);
    }
}
