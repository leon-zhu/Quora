package com.quora.dao;

import com.quora.module.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/26 18:21
 * @version: 1.0
 */
@Mapper
public interface QuestionDAO {

    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, user_id, created_date, comment_count "; //数据库中的字段
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    //#{headUrl}: 其中的headUrl是User类中的成员变量名, 不要写成head_url
    /*@Insert({"insert into " + TABLE_NAME + " ( "+ INSERT_FIELDS+  " ) "+
            " values (#{title}, #{content}, #{userId}, #{createdDate}, #{commentCount})"}) //#{..}是类中的字段*/
    int addQuestion(Question question);


    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    //@Delete({"delete from " + TABLE_NAME + " where id = #{id}"})
    int deleteById(int id);

    //@Select({"select " + SELECT_FIELDS + " from " + TABLE_NAME + " where id = #{id}"})
    Question getQuestionById(@Param("id") int id);

    @Update({"update " + TABLE_NAME + " set comment_count = #{commentCount} where id = #{id}"})
    int updateCommentCount(@Param("id") int id,
                           @Param("commentCount") int commentCount);
}
