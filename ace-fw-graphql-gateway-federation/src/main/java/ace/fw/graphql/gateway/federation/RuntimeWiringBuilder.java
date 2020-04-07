package ace.fw.graphql.gateway.federation;

import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/7 12:22
 * @description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuntimeWiringBuilder {

    private List<TypeDefinitionRegistry> typeDefinitionRegistries;

    public RuntimeWiring build() {
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .build();

        return runtimeWiring;
    }
}
