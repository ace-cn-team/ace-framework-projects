package ace.fw.graphql.spring.webmvc.components;

import ace.fw.graphql.spring.webmvc.ExecutionResultHandler;
import ace.fw.json.JsonPlugin;
import graphql.ExecutionResult;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:02
 * @description
 */
@Data
@Accessors(chain = true)
public class DefaultExecutionResultHandler implements ExecutionResultHandler {

    JsonPlugin jsonPlugin;

    @Override
    public Object handleExecutionResult(CompletableFuture<ExecutionResult> executionResultCF) {
        return executionResultCF.thenApply(ExecutionResult::toSpecification);
    }
}
