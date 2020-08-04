package ace.fw.logic.common.junit;

import ace.fw.logic.common.junit.model.request.FindByIdRequest;
import ace.fw.logic.common.junit.service.ClassLogAspectService;
import ace.fw.logic.common.junit.service.MethodLogAspectService;
import ace.fw.logic.common.junit.service.ThrowableHandlerAspectService;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/6/28 14:32
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JUnitApplication.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class TestThrowableHandlerMethodInterceptor {

    private final static String TEST_MOBILE = "15099975787";
    @Autowired
    private ThrowableHandlerAspectService throwableHandlerAspectService;

    FindByIdRequest request = FindByIdRequest.builder()
            .id("1")
            .build();

    @Test
    public void test_0001_testThrowException() {
        throwableHandlerAspectService.testThrowException(request);
    }

    @Test
    public void test_0002_testThrowBusiness() {
        throwableHandlerAspectService.testThrowBusiness();
    }

    @Test
    public void test_0002_testThrowValidationException() throws BindException {
        throwableHandlerAspectService.testThrowValidationException(request);
    }
}
