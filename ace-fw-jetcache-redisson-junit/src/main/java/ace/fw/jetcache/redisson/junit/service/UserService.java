package ace.fw.jetcache.redisson.junit.service;

import ace.fw.jetcache.redisson.junit.model.bo.UserBo;
import ace.fw.jetcache.redisson.junit.model.request.FindByIdRequest;

import javax.validation.Valid;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/29 15:41
 * @description
 */
public interface UserService {
    UserBo findById(@Valid FindByIdRequest request);

    UserBo findByIdFromLocalCacheOrDb(@Valid FindByIdRequest request);

    UserBo findByIdFromRemoteCacheOrDb(@Valid FindByIdRequest request);

    UserBo findByIdFromMultiCacheOrDb(@Valid FindByIdRequest request);

    UserBo findByIdFromLocalCache(@Valid FindByIdRequest request);

    UserBo findByIdFromRemoteCache(@Valid FindByIdRequest request);

    UserBo findByIdFromMultiCache(@Valid FindByIdRequest request);

    void removeCache(@Valid FindByIdRequest request);

    UserBo setLocalCache(@Valid UserBo request);

    UserBo setRemoteCache(@Valid UserBo request);

    UserBo setMultiCache(@Valid UserBo request);

    UserBo updateVersion();
}
