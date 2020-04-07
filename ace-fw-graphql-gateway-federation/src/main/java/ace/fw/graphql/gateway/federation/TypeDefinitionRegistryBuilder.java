package ace.fw.graphql.gateway.federation;

import ace.fw.graphql.gateway.federation.constant.GraphQLConstants;
import ace.fw.graphql.gateway.federation.enums.OperatorTypeEnum;
import graphql.language.*;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/2 18:01
 * @description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
class TypeDefinitionRegistryBuilder {
    private List<TypeDefinitionRegistry> typeDefinitionRegistries;

    public TypeDefinitionRegistry merge() {
        TypeDefinitionRegistry mergeTypeDefinitionRegistry = new TypeDefinitionRegistry();

        this.addNoOperatorTypes(mergeTypeDefinitionRegistry, typeDefinitionRegistries);

        this.addQueryOperatorType(mergeTypeDefinitionRegistry, typeDefinitionRegistries);

        this.addMutationOperatorType(mergeTypeDefinitionRegistry, typeDefinitionRegistries);

        return mergeTypeDefinitionRegistry;
    }

    private void addMutationOperatorType(TypeDefinitionRegistry mergeTypeDefinitionRegistry, List<TypeDefinitionRegistry> typeDefinitionRegistries) {
        this.addOperatorType(OperatorTypeEnum.MUTATION, mergeTypeDefinitionRegistry, typeDefinitionRegistries);

    }

    private void addQueryOperatorType(TypeDefinitionRegistry mergeTypeDefinitionRegistry, List<TypeDefinitionRegistry> typeDefinitionRegistries) {
        this.addOperatorType(OperatorTypeEnum.QUERY, mergeTypeDefinitionRegistry, typeDefinitionRegistries);
    }

    /**
     * 添加操作类型
     *
     * @param operatorTypeEnum                    操作类型枚举 {@link OperatorTypeEnum}
     * @param mergeDestTypeDefinitionRegistry     准备合并的目标TypeDefinitionRegistry
     * @param mergeSourceTypeDefinitionRegistries 准备合并的来源TypeDefinitionRegistry
     */
    private void addOperatorType(OperatorTypeEnum operatorTypeEnum, TypeDefinitionRegistry mergeDestTypeDefinitionRegistry, List<TypeDefinitionRegistry> mergeSourceTypeDefinitionRegistries) {
        // 获取对应operator type的所有 operator type definition
        List<TypeDefinition> operatorTypeDefinitions = mergeSourceTypeDefinitionRegistries
                .stream()
                .map(p -> {
                    return p.types().entrySet().stream()
                            .filter(typeDefinition -> operatorTypeEnum.getCode().equalsIgnoreCase(typeDefinition.getValue().getName()))
                            .findFirst()
                            .map(entry -> entry.getValue())
                            .orElse(null);
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        //复制所有query operator 的相关属性到一个query operator

        Map<String, String> additionalDataMap = new HashMap<>();
        List<Type> implementList = new ArrayList<>();
        List<FieldDefinition> fieldDefinitions = new ArrayList<>();
        List<Directive> directives = new ArrayList<>();
        operatorTypeDefinitions.stream()
                .forEach(operatorTypeDefinition -> {
                    ObjectTypeDefinition queryObjectTypeDefinition = (ObjectTypeDefinition) operatorTypeDefinition;
                    additionalDataMap.putAll(queryObjectTypeDefinition.getAdditionalData());
                    implementList.addAll(queryObjectTypeDefinition.getImplements());
                    fieldDefinitions.addAll(queryObjectTypeDefinition.getFieldDefinitions());
                    directives.addAll(queryObjectTypeDefinition.getDirectives());
                });
        ObjectTypeDefinition operatorType = ObjectTypeDefinition.newObjectTypeDefinition()
                .name(GraphQLConstants.OPERATOR_TYPE_QUERY)
                .additionalData(additionalDataMap)
                .implementz(implementList)
                .fieldDefinitions(fieldDefinitions)
                .directives(directives)
                .build();
        mergeDestTypeDefinitionRegistry.add(operatorType);
    }

    /**
     * 添加基本类型定义，除了操作类型，例如：Query,Mutation
     *
     * @param mergeTypeDefinitionRegistry
     * @param typeDefinitionRegistries
     */
    private void addNoOperatorTypes(TypeDefinitionRegistry mergeTypeDefinitionRegistry, List<TypeDefinitionRegistry> typeDefinitionRegistries) {
        List<String> skipOperatorTypeName = Arrays.asList(GraphQLConstants.OPERATOR_TYPE_QUERY, GraphQLConstants.OPERATOR_TYPE_MUTATION);

        typeDefinitionRegistries.stream()
                .forEach(typeDefinitionRegistry -> {
                    typeDefinitionRegistry.types()
                            .entrySet()
                            .stream()
                            .forEach(typeEntry -> {
                                String typeName = typeEntry.getKey();
                                boolean isSkip = skipOperatorTypeName
                                        .stream()
                                        .anyMatch(p -> p.equalsIgnoreCase(typeName));
                                if (isSkip) {
                                    return;
                                }
                                mergeTypeDefinitionRegistry.add(typeEntry.getValue());
                            });
                });
    }
}
