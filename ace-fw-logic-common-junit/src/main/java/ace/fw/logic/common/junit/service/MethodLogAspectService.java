package ace.fw.logic.common.junit.service;

import ace.fw.logic.common.junit.model.bo.UserBo;
import ace.fw.logic.common.junit.model.request.FindByIdRequest;

import javax.validation.Valid;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/29 15:41
 * @description
 */

public interface MethodLogAspectService {

    UserBo testInputOutput(@Valid FindByIdRequest request);

    void testInputNoOutput(@Valid FindByIdRequest request);

    UserBo testNoInputHasOutput();

    void testNoInputOutput();

    UserBo testInputThrowable(@Valid FindByIdRequest request);
}
