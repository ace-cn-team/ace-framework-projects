package ace.fw.logic.common.junit;

import ace.fw.logic.common.junit.model.request.FindByIdRequest;
import ace.fw.logic.common.junit.service.ClassLogAspectService;
import ace.fw.logic.common.junit.service.MethodLogAspectService;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
public class TestLogMethodInterceptor {

    private final static String TEST_MOBILE = "15099975787";
    @Autowired
    private MethodLogAspectService methodLogAspectService;
    @Autowired
    private ClassLogAspectService classLogAspectService;
    FindByIdRequest request = FindByIdRequest.builder()
            .id("1")
            .build();

    @Test
    public void test_0001_classLogAspect() {
        classLogAspectService.findById(request);
    }

    @Test
    public void test_0002_classSystemMethodLogAspect() throws InterruptedException {
        classLogAspectService.toString();
        classLogAspectService.equals(true);
        classLogAspectService.hashCode();
    }

    @Test
    public void test_0003_methodLogAspect_testInputNoOutput() {
        methodLogAspectService.testInputNoOutput(request);
    }

    @Test
    public void test_0004_methodLogAspect_testInputOutput() {
        methodLogAspectService.testInputOutput(request);
    }

    @Test
    public void test_0005_methodLogAspect_testNoInputHasOutput() {
        methodLogAspectService.testNoInputHasOutput();
    }

    @Test
    public void test_0006_methodLogAspect_testNoInputOutput() {
        methodLogAspectService.testNoInputOutput();
    }

    @Test
    public void test_0007_methodLogAspect_testInputThrowable() {
        try {
            methodLogAspectService.testInputThrowable(request);
        } catch (Exception ex) {

        }
        try {
            Thread.sleep(500);
        } catch (Throwable ex) {
        } finally {

        }

    }
}
