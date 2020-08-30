package ace.fw.restful.base.api.model.where;

import ace.fw.restful.base.api.model.entity.EntityPropertyFunction;

import java.util.List;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/30 8:59
 * @description
 */
public interface EntityWhere<TWhere extends EntityWhere, TCondition extends Condition> extends Where<TWhere, TCondition> {
    <T, R> TWhere eq(EntityPropertyFunction<T, R> entityPropertyFunction, Object value);

    <T, R> TWhere ne(EntityPropertyFunction<T, R> entityPropertyFunction, Object value);

    <T, R> TWhere like(EntityPropertyFunction<T, R> entityPropertyFunction, Object value);

    <T, R> TWhere notLike(EntityPropertyFunction<T, R> entityPropertyFunction, Object value);

    <T, R> TWhere likeRight(EntityPropertyFunction<T, R> entityPropertyFunction, Object value);

    <T, R> TWhere gt(EntityPropertyFunction<T, R> entityPropertyFunction, Object value);

    <T, R> TWhere ge(EntityPropertyFunction<T, R> entityPropertyFunction, Object value);

    <T, R> TWhere lt(EntityPropertyFunction<T, R> entityPropertyFunction, Object value);

    <T, R> TWhere le(EntityPropertyFunction<T, R> entityPropertyFunction, Object value);

    <T, R> TWhere isNull(EntityPropertyFunction<T, R> entityPropertyFunction);

    <T, R> TWhere isNotNull(EntityPropertyFunction<T, R> entityPropertyFunction);

    <T, R> TWhere between(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values);

    <T, R> TWhere notBetween(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values);

    <T, R> TWhere in(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values);

    <T, R> TWhere notIn(EntityPropertyFunction<T, R> entityPropertyFunction, List<Object> values);
}
