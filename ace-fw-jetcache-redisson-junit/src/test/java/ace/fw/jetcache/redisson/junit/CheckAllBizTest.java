package ace.fw.jetcache.redisson.junit;

import ace.fw.jetcache.redisson.junit.model.bo.UserBo;
import ace.fw.jetcache.redisson.junit.model.request.FindByIdRequest;
import ace.fw.jetcache.redisson.junit.service.UserService;
import com.alicp.jetcache.anno.CacheRefresh;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

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
public class CheckAllBizTest {

    private final static String TEST_MOBILE = "15099975787";
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private UserService userService;
    FindByIdRequest request = FindByIdRequest.builder()
            .id("1")
            .build();

    @Test
    public void test_0001_clearPreCache() {
        userService.removeCache(request);
        log.info("clearPreCache success");
    }

    @Test
    public void test_0002_localCache() {
        UserBo userBoDb = userService.findById(request);

        userService.setLocalCache(request, userBoDb);

        UserBo userBoLocalCache = userService.findByIdFromLocalCache(request);

        if (userBoLocalCache == null) {
            throw new RuntimeException("本地缓存测试失败,本地缓存没有值");
        }

        UserBo userBoDbNewVersion = userService.updateVersion();

        if (userBoLocalCache.getVersion() >= userBoDbNewVersion.getVersion()) {
            throw new RuntimeException("本地缓存测试失败,测试版本号不通过");
        }
        log.info("testLocalCache success");
    }

    @Test
    public void test_0003_removeLocalCache() {
        userService.removeCache(request);

        UserBo userBoLocalCache = userService.findByIdFromLocalCache(request);

        if (userBoLocalCache != null) {
            throw new RuntimeException("删除本地缓存失败");
        }

        log.info("removeLocalCache success");
    }

    @Test
    public void test_0004_bothCache() {
        UserBo userBoDb = userService.findByIdFromRemoteCacheOrDb(request);

        UserBo userBoLocalCache = userService.findByIdFromLocalCache(request);

        if (userBoLocalCache == null) {
            throw new RuntimeException("本地缓存查询测试失败");
        }

        UserBo userBoRemoteCache = userService.findByIdFromRemoteCache(request);

        if (userBoRemoteCache == null) {
            throw new RuntimeException("远程缓存查询测试失败");
        }

        log.info("testBothCache success");
    }

    @Test
    public void test_10001_redissonClient() {
        UserBo userBoDb = userService.findByIdFromRemoteCacheOrDb(request);
        RBucket<UserBo> rBucket = redissonClient.getBucket("test");
        rBucket.set(userBoDb, 60, TimeUnit.SECONDS);

        UserBo userBo1 = rBucket.get();

        Assert.assertNotNull(userBo1);
    }
}
