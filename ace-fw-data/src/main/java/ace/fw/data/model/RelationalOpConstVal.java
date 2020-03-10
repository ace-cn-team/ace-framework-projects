package ace.fw.data.model;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 15:14
 * @description
 */
public interface RelationalOpConstVal {


    String EQ = "eq";
    String NE = "ne";
    String IN = "in";
    String LIKE = "like";
    String LIKE_LEFT = "like-left";
    String LIKE_RIGHT = "like-right";
    String GT = "gt";
    String GE = "ge";
    String LT = "lt";
    String LE = "le";
    String IS_NULL = "is-null";
    String IS_NOT_NULL = "is-not-null";

    String BETWEEN = "between";
    String NOT_BETWEEN = "not-between";
    String NOT_IN = "not-in";
    String NOT_LIKE = "not-like";

}
