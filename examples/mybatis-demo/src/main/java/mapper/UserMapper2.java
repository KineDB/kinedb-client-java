package mapper;

import org.apache.ibatis.annotations.Param;
import pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper2 {

    void createDatabase();

    void dropDatabase();

    void createTable();

    void dropTable();

    List<User> selectAll();

    User selectById(long id);


    //List<User> selectByCondition(@Param("id")long id, @Param("name")String name, @Param("age")int age);

    //List<User> selectByCondition(User user);

    List<User> selectByCondition(Map map);

    List<User> selectByConditionSingle(Map map);

    int insert(User user);

    int update(User user);

    int deleteById(long id);

    int deleteByIds(@Param("ids")List<Long> ids);

}
