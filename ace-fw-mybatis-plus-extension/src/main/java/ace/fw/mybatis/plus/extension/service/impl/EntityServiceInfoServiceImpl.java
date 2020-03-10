package ace.fw.mybatis.plus.extension.service.impl;

import ace.fw.data.model.EntityInfo;
import ace.fw.data.model.entity.Entity;
import ace.fw.mybatis.plus.extension.model.EntityServiceInfo;
import ace.fw.mybatis.plus.extension.service.EntityServiceInfoService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/1/6 17:46
 * @description
 */
@Data
@Accessors(chain = true)
public class EntityServiceInfoServiceImpl implements EntityServiceInfoService {

    private Map<String, EntityServiceInfo<Entity>> entityServiceInfoMap;

    @Override
    public EntityServiceInfo<Entity> getById(String id) {
        return entityServiceInfoMap.get(id);
    }

    @Override
    public List<EntityInfo> getEntityInfos() {
        return entityServiceInfoMap.entrySet()
                .stream()
                .map(entry -> entry.getValue().getEntityInfo())
                .collect(Collectors.toList());
    }

    @Override
    public <T extends Entity> EntityInfo getByEntityClass(Class<T> entityClass) {
        return entityServiceInfoMap.entrySet()
                .stream()
                .map(p -> p.getValue())
                .map(p -> p.getEntityInfo())
                .filter(p -> StringUtils.equals(p.getId(), entityClass.getSimpleName()))
                .findFirst()
                .orElse(null);

    }
}
