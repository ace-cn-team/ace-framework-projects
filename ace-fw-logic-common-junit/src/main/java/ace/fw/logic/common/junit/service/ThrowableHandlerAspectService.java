package ace.fw.logic.common.junit.service;

import ace.fw.logic.common.junit.model.bo.UserBo;
import ace.fw.logic.common.junit.model.request.FindByIdRequest;
import ace.fw.model.response.GenericResponseExt;
import org.springframework.validation.BindException;

import javax.validation.Valid;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/29 15:41
 * @description
 */

public interface ThrowableHandlerAspectService {

    GenericResponseExt<UserBo> testThrowException(@Valid FindByIdRequest request);

    GenericResponseExt testThrowBusiness();

    GenericResponseExt testThrowValidationException(@Valid FindByIdRequest request) throws BindException;

}
