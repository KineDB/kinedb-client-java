import mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MyBatisTest {

    SqlSession sqlSession;
    UserMapper userMapper;

    @Before
    public void setUp() throws IOException {
        //1.get sqlSessionFactory
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //2.get sqlSession
        sqlSession = sqlSessionFactory.openSession();

        //3.get mapper proxy
        userMapper = sqlSession.getMapper(UserMapper.class);

        //userMapper.createDatabase();
        //userMapper.createTable();

        System.out.println("MyBatisTest setUp done.");
    }

    @After
    public void after() {
        //userMapper.dropTable();
        //userMapper.dropDatabase();
        sqlSession.close();
        System.out.println("MyBatisTest after done.");
    }



    /**
     * insert
     * @throws IOException
     */
    @Test
    public void testInsert() throws IOException {

        User user1 = new User();
        user1.setId(1L);
        user1.setName("name");
        user1.setAge(10);
        User user2 = new User();
        user2.setId(2L);
        user2.setName("name2");
        user2.setAge(12);

        int result1 = userMapper.insert(user1);
        System.out.println("testInsert result1 " + result1);

        int result2 = userMapper.insert(user2);
        System.out.println("testInsert result2 " + result2);

    }

    /**
     * query all
     * @throws IOException
     */
    @Test
    public void testSelectAll() {
        List<User> users = userMapper.selectAll();
        System.out.println("testSelectAll result \n" + users);

    }

    /**
     * query by id
     * @throws IOException
     */
    @Test
    public void testSelectById() throws IOException {
        long id=1;

        User user = userMapper.selectById(id);
        System.out.println("testSelectById result " + user);

    }

    /**
     * query by condition
     * @throws IOException
     */
    @Test
    public void testSelectByCondition() throws IOException {

        String name = "name";
        name = "%" + name +"%";

        Map map = new HashMap<>();
        map.put("name", name);

        List<User> users = userMapper.selectByCondition(map);
        System.out.println("testSelectByCondition results " + users);

        map.put("age", 10);
        List<User> users2 = userMapper.selectByCondition(map);
        System.out.println("testSelectByCondition results2 " + users2);
    }


    /**
     * choose when
     * @throws IOException
     */
    @Test
    public void testSelectByConditionSingle() throws IOException {
        int id = 1;
        String name = "name";
        int age = 10;

        Map map = new HashMap<>();
        map.put("id", id);

        List<User> users = userMapper.selectByConditionSingle(map);
        System.out.println("testSelectByConditionSingle1 " + users);

        map.clear();
        map.put("name", name);
        List<User> users2 = userMapper.selectByConditionSingle(map);
        System.out.println("testSelectByConditionSingle2 " + users2);

        map.clear();
        map.put("age", age);
        List<User> users3 = userMapper.selectByConditionSingle(map);
        System.out.println("testSelectByConditionSingle3 " + users3);
    }

    /**
     * TODO auto generator id and return
     * @throws IOException
     */
    @Test
    public void testInsert2() throws IOException {
        //参数
//        int status = 1;
//        String companyName = "google company";
//        String brandName = "google";
//        String description = "766";
//        int ordered = 10;
//
//        //封装对象
//        User user = new User();
//        user.setBrandName(brandName);
//        user.setStatus(status);
//        user.setCompanyName(companyName);
//        user.setDescription(description);
//        user.setOrdered(ordered);
//
//        userMapper.insert(user);
//        Integer id = user.getId();
//        System.out.println(id);

        //提交事务 增删改需要提交事务
        //sqlSession.commit();

    }

    /**
     * update
     * @throws IOException
     */
    @Test
    public void testUpdate() throws IOException {

        User user = new User();
        user.setId(1L);
        user.setName("name1");
        user.setAge(11);

        int result = userMapper.update(user);
        System.out.println("testInsert result " + result);
    }

    /**
     * delete by id
     * @throws IOException
     */
    @Test
    public void testDeleteById() throws IOException {

        long id = 2;

        int result = userMapper.deleteById(id);
        System.out.println("testDeleteById result " + result);
    }

    /**
     * delete by ids
     * @throws IOException
     */
    @Test
    public void testDeleteByIds() throws IOException {
        //参数
        List<Long> ids = Arrays.asList(1L, 2L);

        int results = userMapper.deleteByIds(ids);
        System.out.println("testDeleteByIds results " + results);
        //TODO
        //sqlSession.commit();

    }
}
