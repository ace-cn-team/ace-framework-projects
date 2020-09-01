package ace.fw.restful.base.api.web;


import ace.fw.restful.base.api.plugin.mybatisplus.MybatisPlusDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/2 11:02
 * @description 通用数据访问层控制器
 */
@Slf4j
@Validated
public abstract class AbstractMybatisController<T, IdType> extends AbstractController<T, IdType, MybatisPlusDbService<T, IdType>> {

}
