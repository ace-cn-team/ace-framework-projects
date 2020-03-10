package ace.fw.ms.application.support.resolver;

import ace.fw.model.response.GenericResponse;
import ace.fw.ms.application.support.handler.WebExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author 陈志杭
 * @contract 279397942@qq.com
 * @create 2019/9/6 10:03
 * @description 异常解析工具
 */
@Slf4j
public class WebExceptionResolver extends AbstractHandlerMethodExceptionResolver {

    private HttpMessageConverter<Object> messageConverter;

    private WebExceptionHandler webExceptionHandler = new WebExceptionHandler();

    public WebExceptionResolver() {
    }

    public WebExceptionResolver(HttpMessageConverter<Object> messageConverter) {
        this.messageConverter = messageConverter;
    }

    public WebExceptionResolver(HttpMessageConverter<Object> messageConverter, WebExceptionHandler webExceptionHandler) {
        this.messageConverter = messageConverter;
        this.webExceptionHandler = webExceptionHandler;
    }

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
                                                           @Nullable HandlerMethod handlerMethod, Exception ex) {
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
        response.setStatus(200);
        GenericResponse outputValue = getResultWithException(request, response, handlerMethod, ex);
        if (Objects.isNull(outputValue)) {
            return null;
        }
        try {
            messageConverter.write(outputValue, MediaType.APPLICATION_JSON_UTF8, outputMessage);
            return new ModelAndView();
        } catch (Throwable e) {

            // Any other than the original exception is unintended here,
            // probably an accident (e.g. failed assertion or the like).
            log.error(String.format("Failed to invoke HandlerMethodException: {}, outputValue:{}", handlerMethod, outputValue), ex);
            // Continue with default processing of the original exception...
            return null;
        }
    }

    private GenericResponse getResultWithException(HttpServletRequest request, HttpServletResponse response,
                                                   @Nullable HandlerMethod handlerMethod, Exception ex) {
        return webExceptionHandler.execute(request, response, handlerMethod, ex);
    }

    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");
        return new ServletServerHttpResponse(response);
    }

    protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");
        return new ServletServerHttpRequest(servletRequest);
    }
}
