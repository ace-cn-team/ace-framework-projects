package ace.fw.logic.common.junit.service.impl;

import ace.fw.logic.common.aop.Interceptor.log.annotations.LogAspect;
import ace.fw.logic.common.junit.model.bo.UserBo;
import ace.fw.logic.common.junit.model.request.FindByIdRequest;
import ace.fw.logic.common.junit.service.MethodLogAspectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class MethodMethodLogAspectServiceImpl implements MethodLogAspectService {

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

    @LogAspect
    @Override
    public UserBo testInputOutput(@Valid FindByIdRequest request) {
        UserBo userBo = userList.stream().filter(p -> StringUtils.equalsIgnoreCase(request.getId(), p.getId())).findFirst().orElse(null);
        return this.cloneUserBo(userBo);
    }

    @LogAspect
    @Override
    public void testInputNoOutput(@Valid FindByIdRequest request) {
        return;
    }

    @LogAspect
    @Override
    public UserBo testNoInputHasOutput() {
        UserBo userBo = userList.stream().findFirst().get();
        return this.cloneUserBo(userBo);
    }

    @LogAspect
    @Override
    public void testNoInputOutput() {

    }

    @LogAspect
    @Override
    public UserBo testInputThrowable(@Valid FindByIdRequest request) {
        int i = 1 / (1 - 1);
        return null;
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
