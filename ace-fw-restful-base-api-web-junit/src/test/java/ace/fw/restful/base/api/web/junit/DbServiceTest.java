package ace.fw.restful.base.api.web.junit;

import ace.fw.json.JsonUtils;
import ace.fw.logic.common.util.AceUUIDUtils;
import ace.fw.restful.base.api.model.request.base.PagerRequest;
import ace.fw.restful.base.api.model.page.PageResult;
import ace.fw.restful.base.api.model.request.base.PageRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateForceRequest;

import ace.fw.restful.base.api.model.request.base.WhereRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateRequest;
import ace.fw.restful.base.api.plugin.DbService;
import ace.fw.restful.base.api.util.QueryUtils;
import ace.fw.restful.base.api.web.junit.dal.entity.User;
import ace.fw.util.AceLocalDateTimeUtils;
import ace.fw.util.AceRandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/8/28 10:56
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JUnitApplication.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class DbServiceTest {
    @Autowired
    private DbService<User> userDbService;

    @Test
    public void test_0001_save() {
        User user = this.newUser();
        boolean isSuccess = userDbService.save(user);
        Assert.assertTrue(isSuccess);
        User user1 = userDbService.findById(user.getId());
        Assert.assertNotNull(user1);
    }

    @Test
    public void test_0002_saveBatch() {
        List<User> users = Arrays.asList(newUser(), newUser());
        boolean isSuccess = userDbService.saveBatch(users);
        Assert.assertTrue(isSuccess);

        this.checkSaveUser(users.toArray(new User[users.size()]));

    }

    @Test
    public void test_0003_update() {
        User user2 = this.saveUser();
        User user1 = this.saveUser();

        List<User> users = Arrays.asList(user2, user1);
        log.info(JsonUtils.toJson(users));
        User updateUser = User.builder()
                .name("b")
                .build();

        EntityUpdateRequest request = EntityUpdateRequest.<User>builder()
                .entity(updateUser)
                .where(new WhereRequest().in(User::getId, Arrays.asList(user1.getId(), user2.getId())))
                .build();
        boolean isSuccess = userDbService.update(request);
        Assert.assertTrue(isSuccess);
        List<User> updatedUsers = userDbService.findListById(users.stream().map(p -> p.getId()).collect(Collectors.toList()));
        log.info(JsonUtils.toJson(updatedUsers));
        updatedUsers.forEach(user -> {
            Assert.assertTrue(user.getName().equals("b"));
            Assert.assertNotNull(user.getLevel());
        });

    }

    @Test
    public void test_0004_updateById() {
        User user1 = this.saveUser();

        User updateUser = User.builder()
                .name("b")
                .id(user1.getId())
                .level(null)
                .build();

        boolean isSuccess = userDbService.updateById(updateUser);
        Assert.assertTrue(isSuccess);

        List<User> updatedUsers = userDbService.findListById(Arrays.asList(user1.getId()));
        log.info(JsonUtils.toJson(updatedUsers));
        updatedUsers.forEach(user -> {
            Assert.assertTrue(user.getName().equals("b"));
            Assert.assertNotNull(user.getLevel());
        });
    }

    @Test
    public void test_0005_updateBatchById() {
        User user2 = this.saveUser();
        User user1 = this.saveUser();

        List<User> users = Arrays.asList(user2, user1);
        log.info(JsonUtils.toJson(users));
        List<User> updateUsers = users.stream().map(user -> {
            return User.builder()
                    .id(user.getId())
                    .name("b")
                    .level(null)
                    .build();

        }).collect(Collectors.toList());


        boolean isSuccess = userDbService.updateBatchById(updateUsers);
        Assert.assertTrue(isSuccess);
        List<User> updatedUsers = userDbService.findListById(users.stream().map(p -> p.getId()).collect(Collectors.toList()));
        log.info(JsonUtils.toJson(updatedUsers));
        updatedUsers.forEach(user -> {
            Assert.assertTrue(user.getName().equals("b"));
            Assert.assertNotNull(user.getLevel());
        });

    }

    @Test
    public void test_0006_updateForce() {
        User user2 = this.saveUser();
        List<User> users = Arrays.asList(user2);
        log.info(JsonUtils.toJson(users));
        User updateUser = user2;
        updateUser.setName("b");
        updateUser.setLevel(null);
        EntityUpdateForceRequest request = EntityUpdateForceRequest.<User>builder()
                .entity(updateUser)
                .where(new WhereRequest().in(User::getId, Arrays.asList(user2.getId())))
                .build();
        boolean isSuccess = userDbService.updateForce(request);
        Assert.assertTrue(isSuccess);
        List<User> updatedUsers = userDbService.findListById(users.stream().map(p -> p.getId()).collect(Collectors.toList()));
        log.info(JsonUtils.toJson(updatedUsers));
        updatedUsers.forEach(user -> {
            Assert.assertTrue(user.getName().equals("b"));
            Assert.assertNull(user.getLevel());
        });
    }

    @Test
    public void test_0007_updateByIdVersionAutoUpdate() {
        User user2 = this.saveUser();
        long oldRowVersion = user2.getRowVersion().longValue();
        List<User> users = Arrays.asList(user2);
        log.info(JsonUtils.toJson(users));
        User updateUser = user2;
        updateUser.setName("b");
        updateUser.setLevel(null);


        boolean isSuccess = userDbService.updateByIdVersionAutoUpdate(updateUser);
        Assert.assertTrue(isSuccess);
        List<User> updatedUsers = userDbService.findListById(users.stream().map(p -> p.getId()).collect(Collectors.toList()));
        log.info(JsonUtils.toJson(updatedUsers));
        updatedUsers.forEach(user -> {
            Assert.assertTrue(user.getName().equals("b"));
            Assert.assertNull(user.getLevel());
            Assert.assertEquals(oldRowVersion + 1, user.getRowVersion().longValue());
        });
    }

    @Test
    public void test_0008_page() {
        List<User> savedUserList = new ArrayList<>(10);
        int saveUserCount = 20;
        for (int i = 0; i < saveUserCount; i++) {
            User savedUser = this.saveUser(i);
            savedUserList.add(savedUser);
        }

        Long minLevel = 5L;
        Long maxLevel = 8L;
        int pageIndex = 1;
        int pageSize = 2;

        PageRequest request = PageRequest.builder()
                .orderBy(QueryUtils.orderByAsc(User::getCreateTime))
                .select(QueryUtils.select(User::getId, User::getName))
                .where(QueryUtils.where().ge(User::getLevel, minLevel).le(User::getLevel, maxLevel)
                        .ge(User::getCreateTime, AceLocalDateTimeUtils.MIN_MYSQL)
                )
                .pager(new PagerRequest(pageIndex, pageSize))
                .build();

        PageResult pageResult = userDbService.page(request);
        Assert.assertEquals(maxLevel - minLevel + 1, pageResult.getTotalCount());
        Assert.assertEquals(pageSize, pageResult.getData().size());
        Assert.assertEquals(2, pageResult.getTotalPage());


    }

    private void checkSaveUser(User... users) {
        List<User> userList = Arrays.asList(users);
        userList.forEach(user -> {
            User user1 = userDbService.findById(user.getId());
            Assert.assertNotNull(user1);
        });
    }

    private User saveUser() {
        User user = this.newUser();
        userDbService.save(user);
        return user;
    }

    private User saveUser(long level) {
        User user = this.newUser();
        user.setLevel(level);
        userDbService.save(user);
        return user;
    }

    private User newUser() {
        User user = User.builder()
                .id(AceUUIDUtils.generateTimeUUIDShort32())
                .createTime(LocalDateTime.now())
                .level(RandomUtils.nextLong())
                .name(AceRandomUtils.randomNumber(6))
                .state(RandomUtils.nextInt())
                .rowVersion(1)
                .build();
        return user;
    }
}
