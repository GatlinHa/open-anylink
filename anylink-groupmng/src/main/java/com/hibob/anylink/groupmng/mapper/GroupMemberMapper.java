package com.hibob.anylink.groupmng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hibob.anylink.groupmng.entity.GroupMember;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface GroupMemberMapper extends BaseMapper<GroupMember> {

    @Select("select * from anylink_group_member " +
            " where group_id in (SELECT group_id FROM anylink_group_member WHERE account = #{account}) AND in_status = 0" +
            " and (account = #{searchKey} or nick_name like CONCAT('%', #{searchKey}, '%'))")
    List<GroupMember> searchGroupMember(String account, String searchKey);

    /**
     * 批量插入数据，如果联合主键已存在则更新
     * @param memberList
     * @return
     */
    @Insert("<script>" +
            " INSERT INTO anylink_group_member (group_id, account, nick_name, role, muted_mode, in_status)" +
            " values" +
            " <foreach item='item' index='index' collection='memberList' separator=', '> " +
            " (#{item.groupId}, #{item.account}, #{item.nickName}, #{item.role}, #{item.mutedMode}, #{item.inStatus})" +
            " </foreach> " +
            " as new" +
            " ON DUPLICATE KEY UPDATE " +
            " nick_name = new.nick_name, " +
            " role = new.role, " +
            " muted_mode = new.muted_mode, " +
            " in_status = new.in_status " +
            "</script>")
    int batchInsertOrUpdate(List<Map<String, Object>> memberList);

}
