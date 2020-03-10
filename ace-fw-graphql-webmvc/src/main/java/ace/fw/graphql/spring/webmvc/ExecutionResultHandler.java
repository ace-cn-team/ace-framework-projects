package ace.fw.graphql.spring.webmvc;

import graphql.ExecutionResult;

import java.util.concurrent.CompletableFuture;
/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:02
 * @description
 */
public interface ExecutionResultHandler {

    Object handleExecutionResult(CompletableFuture<ExecutionResult> executionResultCF);
}
