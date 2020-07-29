package ace.fw.jetcache.redisson.junit;

import ace.fw.jetcache.redisson.junit.model.bo.UserBo;
import ace.fw.jetcache.redisson.junit.model.request.FindByIdRequest;
import ace.fw.jetcache.redisson.junit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class CheckAllBizTest {

    private final static String TEST_MOBILE = "15099975787";
    @Autowired
    private UserService userService;

    @Test
    public void testAllBiz() {
        FindByIdRequest request = FindByIdRequest.builder()
                .id("1")
                .build();
        this.clearPreCache(request);
        log.info("clearPreCache success");

        this.testLocalCache(request);
        log.info("testLocalCache success");

        this.removeLocalCache(request);
        log.info("removeLocalCache success");
    }

    private void removeLocalCache(FindByIdRequest request) {
        userService.removeCache(request);

        UserBo userBoLocalCache = userService.findByIdFromLocalCache(request);

        if (userBoLocalCache != null) {
            throw new RuntimeException("删除本地缓存失败");
        }
    }

    private void testLocalCache(FindByIdRequest request) {
        UserBo userBoDb = userService.findById(request);

        userService.setLocalCache(userBoDb);

        UserBo userBoLocalCache = userService.findByIdFromLocalCache(request);

        if (userBoLocalCache == null) {
            throw new RuntimeException("本地缓存测试失败,本地缓存没有值");
        }

        UserBo userBoDbNewVersion = userService.updateVersion();

        if (userBoLocalCache.getVersion() >= userBoDbNewVersion.getVersion()) {
            throw new RuntimeException("本地缓存测试失败,测试版本号不通过");
        }
    }

    private void clearPreCache(FindByIdRequest request) {
        userService.removeCache(request);
    }

}
