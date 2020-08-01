//package ace.fw.dal.sharding.algorithm;
//
//import com.doubo.common.interfaces.StringHashCoding;
//import com.sibu.mall.common.exception.BusinessException;
//import org.apache.commons.lang3.StringUtils;
//
///**
// * @author Caspar
// * @contract 279397942@qq.com
// * @create 2020/4/9 17:53
// * @description
// */
//public  class ShardingTBAlgorithms {
//
//   private static StringHashCoding tableHashCoding;
//
//    public static StringHashCoding getTableHashCoding() {
//        return tableHashCoding;
//    }
//
//    public static void setTableHashCoding(StringHashCoding tableHashCoding) {
//        ShardingTBAlgorithms.tableHashCoding = tableHashCoding;
//    }
//
//    /**
//     * 通过负载一致性算法 获取真实节点数
//     * @param identity
//     * @return
//     */
//    public static Integer getRealNode(Object identity){
//        String s = String.valueOf(identity);
//        if(StringUtils.isBlank(s)){
//            throw new BusinessException("通过分表负载均衡一致性算法获取真实节点数时，identity 不能为null 或空字符串!");
//        }
//        return tableHashCoding.hashFor(s);
//    }
//
//    /**
//     * 生成2位 32进制定位符
//     * @param identity 生成定位符的标识 例如：memberId 或其他
//     * @return
//     */
//    public static String getLocatorStr(String identity){
//        String locatorStr = Integer.toString(ShardingTBAlgorithms.getRealNode(identity), 32).toUpperCase();
//        if (locatorStr.length() == 1) {//不足两位 补0
//            locatorStr = "0" + locatorStr;
//        }
//        return locatorStr;
//    }
//
//    /**
//     * 通过定位符字符串 转为真实节点数
//     * @param locatorStr
//     * @return
//     */
//    public static Integer transferRealNodeByLocatorStr(String locatorStr){
//        if(StringUtils.isBlank(locatorStr)){
//            throw new RuntimeException("通过 locatorStr 转为真实节点时不能为空!");
//        }
//        //要考虑 不足两位时，补位的左边 补位的0
//        String localIndex = locatorStr.substring(locatorStr.length() - 2);
//        String left = localIndex.substring(0, 1);
//        String right = localIndex.substring(1);
//
//        if("0".equals(left)){
//            localIndex = right;
//        }
//        return Integer.parseInt(localIndex,32);
//    }
//}
