package com.hibob.anylink.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hibob.anylink.chat.entity.Session;
import com.hibob.anylink.chat.typeHandler.LongListTypeHandler;
import com.hibob.anylink.chat.typeHandler.StringListTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface SessionMapper extends BaseMapper<Session> {

    @Insert("<script>" +
            " INSERT INTO anylink_chat_session (account, session_id, remote_id, session_type)" +
            " values" +
            " (#{account}, #{sessionId}, #{remoteId}, #{sessionType})" +
            " ON DUPLICATE KEY UPDATE closed = false " +
            "</script>")
    int insertOrUpdate(String account, String sessionId, String remoteId, int sessionType);

    /**
     * 查询account下的session列表，这里引用Left join的目的：单聊时查到对方的已读消息Id(remote_read)
     * @param account
     * @return
     */
    @Select("SELECT t1.*, IFNULL(t2.read_msg_id, 0) AS remote_read FROM anylink_chat_session t1 " +
            " LEFT JOIN anylink_chat_session t2 ON t2.account = t1.remote_id AND t2.remote_id = t1.account and t2.session_type = 2 " +
            " WHERE t1.closed = false AND t1.account = #{account} " +
            " ORDER BY t1.read_time desc")
    @Results({
            @Result(property = "joinTime", column = "join_time", typeHandler = StringListTypeHandler.class),
            @Result(property = "leaveTime", column = "leave_time", typeHandler = StringListTypeHandler.class),
            @Result(property = "delMsgIds", column = "del_msg_ids", typeHandler = LongListTypeHandler.class)
    })
    List<Session> selectSessionListForChat(String account);

    /**
     * 查询指定的session信息，不论这个session是不是closed=true状态
     * @param account
     * @param sessionId
     * @return
     */
    @Select("SELECT t1.*, IFNULL(t2.read_msg_id, 0) AS remote_read FROM anylink_chat_session t1 " +
            " LEFT JOIN anylink_chat_session t2 ON t2.account = t1.remote_id AND t2.remote_id = t1.account and t2.session_type = 2 " +
            " WHERE t1.account = #{account} AND t1.session_id = #{sessionId}")
    @Results({
            @Result(property = "joinTime", column = "join_time", typeHandler = StringListTypeHandler.class),
            @Result(property = "leaveTime", column = "leave_time", typeHandler = StringListTypeHandler.class),
            @Result(property = "delMsgIds", column = "del_msg_ids", typeHandler = LongListTypeHandler.class)
    })
    Session selectSession(String account, String sessionId);

    /**
     * 批量插入数据，如果联合主键已存在则更新
     * @param sessionList
     * @return
     */
    @Insert("<script>" +
            " INSERT IGNORE INTO anylink_chat_session (account, session_id, remote_id, session_type)" +
            " values" +
            " <foreach item='item' index='index' collection='sessionList' separator=','> " +
            " (#{item.account}, #{item.sessionId}, #{item.remoteId}, #{item.sessionType})" +
            " </foreach>" +
            "</script>")
    int batchInsertOrUpdate(List<Map<String, Object>> sessionList);

    @Update("<script>" +
            " update anylink_chat_session set " +
            " join_time = JSON_ARRAY_APPEND(IFNULL(join_time, '[]'), '$', now()), " +
            " read_msg_id = #{lastMsgId}, " +
            " read_time = now() " +
            " where account = #{account} and session_id = #{sessionId} " +
            "</script>")
    int updateForJoin(String account, String sessionId, long lastMsgId);

    @Update("<script>" +
            " update anylink_chat_session set " +
            " del_msg_ids = " +
            "<foreach collection='delMsgIds' item='id' open=\"JSON_MERGE_PRESERVE(IFNULL(del_msg_ids, '[]'), JSON_ARRAY(\" separator=',' close=\"))\">" +
            " #{id}" +
            "</foreach> " +
            " where account = #{account} and session_id = #{sessionId} " +
            "</script>")
    int updateForDelMsg(String account, String sessionId, List<Long> delMsgIds);

    @Update("<script>" +
            " update anylink_chat_session set leave_time = CASE " +
            " <foreach item='item' index='index' collection='sessionList' separator=' '> " +
            " WHEN account = #{item.account} and session_id = #{item.sessionId} then JSON_ARRAY_APPEND(IFNULL(leave_time, '[]'), '$', now())" +
            " </foreach> " +
            " ELSE leave_time END" +
            "</script>")
    int batchUpdateForLeave(List<Map<String, Object>> sessionList);
}
