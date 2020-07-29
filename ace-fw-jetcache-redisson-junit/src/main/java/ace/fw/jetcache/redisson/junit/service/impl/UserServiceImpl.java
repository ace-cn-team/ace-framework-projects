package ace.fw.jetcache.redisson.junit.service.impl;

import ace.fw.jetcache.redisson.junit.model.bo.UserBo;
import ace.fw.jetcache.redisson.junit.model.request.FindByIdRequest;
import ace.fw.jetcache.redisson.junit.service.UserService;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/29 15:41
 * @description
 */
@Component
@Slf4j
public class UserServiceImpl implements UserService {

    private final static String ID_1 = "1";
    private final static String ID_2 = "2";

    private List<UserBo> userList = Arrays.asList(
            UserBo.builder()
                    .createTime(LocalDateTime.now())
                    .id(ID_1)
                    .nickName("1")
                    .state(1)
                    .version(1)
                    .build(),
            UserBo.builder()
                    .createTime(LocalDateTime.now())
                    .id(ID_2)
                    .nickName("2")
                    .state(1)
                    .version(1)
                    .build()
    );

    @Override
    public UserBo findById(@Valid FindByIdRequest request) {
        log.info("access findById");
        UserBo userBo = userList.stream().filter(p -> StringUtils.equalsIgnoreCase(request.getId(), p.getId())).findFirst().orElse(null);
        return this.cloneUserBo(userBo);
    }

    @Cached(name = "userCache.", key = "#args[0].id", localExpire = 60, expire = 60, cacheType = CacheType.LOCAL)
    @Override
    public UserBo findByIdFromLocalCacheOrDb(@Valid FindByIdRequest request) {
        log.info("access findByIdFromLocalCacheOrDb");
        return this.findById(FindByIdRequest.builder()
                .id(request.getId())
                .build());
    }

    @Cached(name = "userCache.", key = "args[0].id", localExpire = 60, expire = 60, cacheType = CacheType.LOCAL)
    @Override
    public UserBo setLocalCache(@Valid UserBo userBo) {
        return userBo;
    }

    @Cached(name = "userCache.", key = "args[0].id", localExpire = 60, expire = 60, cacheType = CacheType.LOCAL)
    @Override
    public UserBo findByIdFromLocalCache(@Valid FindByIdRequest request) {
        return null;
    }

    @Cached(name = "userCache.", key = "args[0].id", localExpire = 60, expire = 60, cacheType = CacheType.REMOTE)
    @Override
    public UserBo findByIdFromRemoteCacheOrDb(@Valid FindByIdRequest request) {
        log.info("access findByIdFromRemoteCacheOrDb");
        return this.findById(FindByIdRequest.builder()
                .id(request.getId())
                .build());
    }

    @Cached(name = "userCache.", key = "args[0].id", localExpire = 60, expire = 60, cacheType = CacheType.REMOTE)
    @Override
    public UserBo findByIdFromRemoteCache(@Valid FindByIdRequest request) {
        return null;
    }

    @Cached(name = "userCache.", key = "args[0].id", localExpire = 60, expire = 60, cacheType = CacheType.REMOTE)
    @Override
    public UserBo setRemoteCache(@Valid UserBo userBo) {
        return userBo;
    }

    @Cached(name = "userCache.", key = "args[0].id", localExpire = 60, expire = 60, cacheType = CacheType.BOTH)
    @Override
    public UserBo findByIdFromMultiCacheOrDb(@Valid FindByIdRequest request) {
        log.info("access findByIdFromMultiCacheOrDb");
        return this.findById(FindByIdRequest.builder()
                .id(request.getId())
                .build());
    }

    @Cached(name = "userCache.", key = "args[0].id", localExpire = 60, expire = 60, cacheType = CacheType.BOTH)
    @Override
    public UserBo findByIdFromMultiCache(@Valid FindByIdRequest request) {
        return null;
    }

    @Cached(name = "userCache.", key = "args[0].id", localExpire = 60, expire = 60, cacheType = CacheType.BOTH)
    @Override
    public UserBo setMultiCache(@Valid UserBo userBo) {
        return userBo;
    }

    @CacheInvalidate(name = "userCache.", key = "args[0].id")
    @Override
    public void removeCache(@Valid FindByIdRequest request) {

    }


    @Override
    public UserBo updateVersion() {
        UserBo userBo = userList.stream().findFirst().get();
        userBo.setVersion(userBo.getVersion() + 1);
        return cloneUserBo(userBo);
    }

    private UserBo cloneUserBo(UserBo userBo) {
        return UserBo.builder().version(userBo.getVersion())
                .state(userBo.getState())
                .nickName(userBo.getNickName())
                .id(userBo.getId())
                .createTime(userBo.getCreateTime())
                .build();
    }
}
