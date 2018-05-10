package com.quora.dao;

import com.quora.module.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/10 19:43
 * @version: 1.0
 */
@Mapper
public interface FeedDAO {

    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " created_date, user_id, data, type "; //数据库中的字段
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;


//    @Insert({"insert into ", TABLE_NAME , " (", INSERT_FIELDS,") values ( #{createdDate}, #{userId}, " +
//            "#{data}, #{type})"})
    int addFeed(Feed feed);

    @Select({"select " + SELECT_FIELDS + " from " + TABLE_NAME + " where id = #{id}"})
    Feed getFeedById(@Param("id") int id);

    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("offset") int offset,
                               @Param("count") int count);

}
