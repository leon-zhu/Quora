package com.quora.dao;

import com.quora.module.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/5 10:23
 * @version: 1.0
 */
@Mapper
public interface CommentDAO {

    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, entity_id, entity_type, created_date, status "; //数据库中的字段
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;


    @Insert({"insert into ", TABLE_NAME, " (", INSERT_FIELDS,") values ( #{content}, #{userId}, " +
            "#{entityId}, #{entityType},#{createdDate}," + "#{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where entity_id = #{entityId} and entity_type = #{entityType} order by created_date desc"})
    List<Comment> getCommentsByEntity(@Param("entityId") int entityId,
                                      @Param("entityType") int entityType);

    @Select({"select count(*) from ", TABLE_NAME,
            " where entity_id = #{entityId} and entity_type = #{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,
                        @Param("entityType") int entityType);
}
