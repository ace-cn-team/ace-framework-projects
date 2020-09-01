package ace.fw.restful.base.api.web.junit;

import ace.fw.json.JsonUtils;
import ace.fw.logic.common.util.AceUUIDUtils;
import ace.fw.restful.base.api.UserBaseApi;
import ace.fw.restful.base.api.model.request.base.FindRequest;
import ace.fw.restful.base.api.model.request.base.PagerRequest;
import ace.fw.restful.base.api.model.page.PageResult;
import ace.fw.restful.base.api.model.request.base.PageRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateForceRequest;
import ace.fw.restful.base.api.model.request.entity.EntityUpdateRequest;
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
@SpringBootTest(classes = JUnitApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class UserBaseApiTest {

    @Autowired
    private UserBaseApi userBaseApi;

    @Test
    public void test_0001_save() throws InterruptedException {

        User user = this.newUser();
        boolean isSuccess = userBaseApi.save(user).check();
        Assert.assertTrue(isSuccess);
        //User user1 = userBaseApi.getById(EntityGetById.<String>builder().id(user.getId()).build()).check();
        User user1 = userBaseApi.findById(user.getId()).check();
        Assert.assertNotNull(user1);
    }

    @Test
    public void test_0002_saveBatch() {
        List<User> users = Arrays.asList(newUser(), newUser());
        boolean isSuccess = userBaseApi.saveBatch(users).check();
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
                .level(null)
                .build();

        EntityUpdateRequest<User> request = EntityUpdateRequest.<User>builder()
                .entity(updateUser)
                .where(QueryUtils.where().in(User::getId, Arrays.asList(user1.getId(), user2.getId())))
                .build();

        boolean isSuccess = userBaseApi.update(request).check();
        Assert.assertTrue(isSuccess);
        List<User> updatedUsers = userBaseApi.getListById(users.stream().map(p -> p.getId()).collect(Collectors.toList())).check();
        log.info(JsonUtils.toJson(updatedUsers));
        updatedUsers.forEach(user -> {
            Assert.assertEquals("b", user.getName());
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

        boolean isSuccess = userBaseApi.updateById(updateUser).check();
        Assert.assertTrue(isSuccess);

        List<User> updatedUsers = userBaseApi.getListById(Arrays.asList(user1.getId())).check();
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


        boolean isSuccess = userBaseApi.updateBatchById(updateUsers).check();
        Assert.assertTrue(isSuccess);
        List<User> updatedUsers = userBaseApi.getListById(users.stream().map(p -> p.getId()).collect(Collectors.toList())).check();
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
        EntityUpdateForceRequest<User> request = EntityUpdateForceRequest.<User>builder()
                .entity(updateUser)
                .where(QueryUtils.where().in(User::getId, Arrays.asList(user2.getId())))
                .build();
        boolean isSuccess = userBaseApi.updateForce(request).check();
        Assert.assertTrue(isSuccess);
        List<User> updatedUsers = userBaseApi.getListById(users.stream().map(p -> p.getId()).collect(Collectors.toList())).check();
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


        boolean isSuccess = userBaseApi.updateByIdVersionAutoUpdate(updateUser).check();
        Assert.assertTrue(isSuccess);
        List<User> updatedUsers = userBaseApi.getListById(users.stream().map(p -> p.getId()).collect(Collectors.toList())).check();
        log.info(JsonUtils.toJson(updatedUsers));
        updatedUsers.forEach(user -> {
            Assert.assertTrue(user.getName().equals("b"));
            Assert.assertNotNull(user.getLevel());
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
                .orderBy(QueryUtils.orderBy().add(User::getCreateTime, true))
                .select(QueryUtils.select(User::getId, User::getName))
                .where(QueryUtils.where()
                        .ge(User::getLevel, minLevel)
                        .le(User::getLevel, maxLevel)
                        .ge(User::getCreateTime, AceLocalDateTimeUtils.MIN_MYSQL)
                )
                .pager(new PagerRequest(pageIndex, pageSize))
                .build();

        PageResult pageResult = userBaseApi.page(request).check();
        log.info(JsonUtils.toJson(pageResult));
        Assert.assertEquals(maxLevel - minLevel + 1, pageResult.getTotalCount());
        Assert.assertEquals(pageSize, pageResult.getData().size());
        Assert.assertEquals(2, pageResult.getTotalPage());


    }

    @Test
    public void test_0009_find() {
        List<User> savedUsers = Arrays.asList(this.saveUser(),
                this.saveUser(),
                this.saveUser(),
                this.saveUser(),
                this.saveUser()
        );
        List<User> users = userBaseApi.find(FindRequest.builder()
                .where(QueryUtils.where().in(User::getId, savedUsers.stream().map(p -> p.getId()).collect(Collectors.toList())))
                .build()
        ).check();
        log.info(JsonUtils.toJson(users));
        Assert.assertEquals(5, users.size());
    }

    @Test
    public void test_0010_count() {
        Integer total = userBaseApi.count(QueryUtils.where().isNotNull(User::getId)).check();
        Assert.assertEquals(total, total);
    }

    @Test
    public void test_0011_findOneByLevel() {
        this.saveUser();
        this.saveUser();
        this.saveUser();
        this.saveUser();
        List<User> users = userBaseApi.findOneByLevel(Long.MAX_VALUE).check();
        Assert.assertTrue(users.size() > 0);
        log.info(JsonUtils.toJson(users));

    }

    private void checkSaveUser(User... users) {
        List<User> userList = Arrays.asList(users);
        userList.forEach(user -> {
            //    User user1 = userBaseApi.getById(EntityGetById.<String>builder().id(user.getId()).build()).check();
            User user1 = userBaseApi.findById(user.getId()).check();
            Assert.assertNotNull(user1);
        });
    }

    private User saveUser() {
        User user = this.newUser();
        userBaseApi.save(user).check();
        return user;
    }

    private User saveUser(long level) {
        User user = this.newUser();
        user.setLevel(level);
        userBaseApi.save(user).check();
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
