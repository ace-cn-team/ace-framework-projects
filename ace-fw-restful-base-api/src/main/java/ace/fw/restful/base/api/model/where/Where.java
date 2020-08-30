package ace.fw.restful.base.api.model.where;


import ace.fw.restful.base.api.model.where.impl.Condition;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/30 8:57
 * @description
 */
public interface Where<TWhere extends Where> {

    List<Condition> getConditions();

    TWhere eq(String propertyName, Object value);

    TWhere ne(String propertyName, Object value);

    TWhere like(String propertyName, Object value);

    TWhere notLike(String propertyName, Object value);

    TWhere likeRight(String propertyName, Object value);

    TWhere gt(String propertyName, Object value);

    TWhere ge(String propertyName, Object value);

    TWhere lt(String propertyName, Object value);

    TWhere le(String propertyName, Object value);

    TWhere isNull(String propertyName, Object value);

    TWhere isNotNull(String propertyName, Object value);

    TWhere between(String propertyName, List<Object> values);

    TWhere notBetween(String propertyName, List<Object> values);

    TWhere in(String propertyName, List<Object> values);

    TWhere notIn(String propertyName, List<Object> values);

    TWhere and();

    TWhere or();

    default TWhere self() {
        return (TWhere) this;
    }
}
