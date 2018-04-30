package com.quora.dao;

import com.quora.module.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/30 20:03
 * @version: 1.0
 */
@Mapper
public interface LoginTicketDAO {

    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, ticket, expired, status "; //数据库中的字段
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    /*@Insert({"insert into " + TABLE_NAME + " ( " + INSERT_FIELDS + ") " + " values ( " +
            "#{userId}, #{ticket}, #{expired}, #{status})"})*/
    int addLoginTicket(LoginTicket loginTicket);

    //@Select({"select " + INSERT_FIELDS + " from " + TABLE_NAME + " where ticket = #{ticket}"})
    LoginTicket selectByTicket(@Param("ticket") String ticket);

    //status: 0-有效, 1-无效
    //@Update({"update " + TABLE_NAME + " set status = #{status} where ticket = #{ticket}"})
    int updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
