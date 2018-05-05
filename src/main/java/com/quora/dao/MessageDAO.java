package com.quora.dao;

import com.quora.module.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/5 14:43
 * @version: 1.0
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id "; //数据库中的字段
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into " + TABLE_NAME + "( " + INSERT_FIELDS + ") " +
            "values ( #{fromId}, #{toId}, #{content}, #{createdDate}, #{hasRead}, #{conversationId})"})
    int addMessage(Message message);

    @Select({"select " + SELECT_FIELDS + " from " + TABLE_NAME +
            " where conversation_id = #{conversationId} order by created_date" +
            " limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Select({"select " + INSERT_FIELDS + ", count(*) as id from " + TABLE_NAME +
            " where from_id = #{userId} or to_id = #{userId} group by conversation_id" +
            " order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Select({"select count(*) from " + TABLE_NAME +
            " where has_read = 0 and to_id = #{userId} and conversation_id = #{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId,
                                   @Param("conversationId") String conversationId);

    @Update({"update " + TABLE_NAME + " set has_read = 1 " +
            "where to_id = #{userId} and conversation_id = #{conversationId}"})
    int updateConversationUnreadCount(@Param("userId") int userId,
                                      @Param("conversationId") String conversationId);
}
