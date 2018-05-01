package com.quora.dao;

import com.quora.module.User;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.annotation.MapperScan;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/26 18:21
 * @version: 1.0
 */
@Mapper
public interface UserDAO {

    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    //#{headUrl}: 其中的headUrl是User类中的成员变量名, 不要写成head_url
    /*@Insert({"insert into " + TABLE_NAME + " ( "+ INSERT_FIELDS+  " ) "+
            " values (#{name}, #{password}, #{salt}, #{headUrl})"})*/
    int addUser(User user);

    //@Select({"select " + SELECT_FIELDS + " from " + TABLE_NAME + " where id = " + "#{id}"})
    User selectById(int id);

    //@Update({"update " + TABLE_NAME + "set password = #{password} where id = #{id}"})
    int updatePassword(@Param("id") int id, @Param("password") String password); //id位于user中, mybatis自己会寻找

    //@Delete({"delete from " + TABLE_NAME + " where id = #{id}"})
    int deleteById(int id);

    //@Select({"select " + SELECT_FIELDS + " from " + TABLE_NAME + " where name = #{name}"})
    User selectByName(@Param("name") String name);
}
