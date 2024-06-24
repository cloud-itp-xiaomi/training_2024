package org.xiaom.yhl.server.server.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * ClassName: GlobalExceptionHandlerTest
 * Package: org.xiaom.yhl.server.server.config
 * Description:
 *
 * @Author 杨瀚林
 * @Create 2024/6/10 22:52
 * @Version 1.0
 */
public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testHandleException() {
        Exception exception = new Exception("Test exception");
        WebRequest webRequest = mock(WebRequest.class);

        ResponseEntity<?> responseEntity = exceptionHandler.handleException(exception);
        assertEquals(500, responseEntity.getStatusCodeValue());
        assertEquals("Test exception", ((HashMap<String, Object>) responseEntity.getBody()).get("message"));
    }
}
