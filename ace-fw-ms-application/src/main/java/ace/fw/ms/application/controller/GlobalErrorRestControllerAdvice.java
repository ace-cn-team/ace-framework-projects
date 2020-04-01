package ace.fw.ms.application.controller;

import ace.fw.model.response.GenericResponse;
import ace.fw.ms.application.constant.AceWebApplicationBootstrapConstant;
import ace.fw.ms.application.support.handler.WebExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/16 10:55
 * @description
 */
@RestControllerAdvice
@RestController
@AllArgsConstructor
public class GlobalErrorRestControllerAdvice {

    private WebExceptionHandler webExceptionHandler;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public GenericResponse error(HttpServletRequest request, HttpServletResponse response,
                                 Exception ex) {
        return webExceptionHandler.execute(request, response, null, ex);
    }

    @RequestMapping(value = {AceWebApplicationBootstrapConstant.MVC_DEFAULT_ERROR_PATH}, method = {RequestMethod.GET})
    public GenericResponse errorAll(HttpServletRequest request, HttpServletResponse response) throws NoHandlerFoundException {
        Object obj = request.getAttribute("javax.servlet.error.request_uri");
        String errorUrl = obj == null ? null : obj.toString();

        NoHandlerFoundException ex = new NoHandlerFoundException(request.getMethod(), errorUrl, new HttpHeaders());
        return this.error(request, response, ex);
    }

    @RequestMapping(path = "/throw/runtime/exception")
    public GenericResponse throwRuntimeException() {
        throw new RuntimeException("1111");
    }
}
