package ace.fw.graphql.gateway.federation;

import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.IOException;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/2 14:29
 * @description
 */
public interface SchemaLoader {
    /**
     * 获取SDL内容
     *
     * @return
     */
    TypeDefinitionRegistry load();
}
