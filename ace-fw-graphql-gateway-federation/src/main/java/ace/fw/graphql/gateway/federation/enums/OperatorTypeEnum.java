package ace.fw.graphql.gateway.federation.enums;

import ace.fw.graphql.gateway.federation.constant.GraphQLConstants;
import lombok.Getter;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/4/7 9:58
 * @description GraphQL的操作类型
 */
public enum OperatorTypeEnum {

    QUERY(GraphQLConstants.OPERATOR_TYPE_QUERY, "Query 操作类型"),
    MUTATION(GraphQLConstants.OPERATOR_TYPE_MUTATION, "Mutation 操作类型");
    @Getter
    private String code;
    @Getter
    private String desc;

    OperatorTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
