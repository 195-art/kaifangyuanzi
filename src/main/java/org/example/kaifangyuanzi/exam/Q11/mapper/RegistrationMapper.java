package org.example.kaifangyuanzi.exam.Q11.mapper;

import org.apache.ibatis.annotations.*;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.example.kaifangyuanzi.exam.Q11.entity.Registration;
import java.util.List;

@Mapper
public interface RegistrationMapper {

    /**
     * 新增报名记录
     * @param registration
     * @return
     */
    @Insert("INSERT INTO registration (event_id, user_id) VALUES (#{eventId}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Registration registration);

    /**
     * 根据活动和用户删除报名记录
     * @param eventId
     * @param userId
     * @return
     */
    @Delete("DELETE FROM registration WHERE event_id = #{eventId} AND user_id = #{userId}")
    int deleteByEventAndUser(@Param("eventId") Long eventId, @Param("userId") Long userId);

    /**
     * 根据活动删除报名记录
     * @param eventId
     * @return
     */
    @Delete("DELETE FROM registration WHERE event_id = #{eventId}")
    int deleteByEvent(Long eventId);

    /**
     * 统计某用户在某个活动的报名数量
     * @param eventId
     * @param userId
     * @return
     */
    @Select("SELECT COUNT(*) FROM registration WHERE event_id = #{eventId} AND user_id = #{userId}")
    int countByEventAndUser(@Param("eventId") Long eventId, @Param("userId") Long userId);

    /**
     * 统计某活动的报名人数
     * @param eventId
     * @return
     */
    @Select("SELECT COUNT(*) FROM registration WHERE event_id = #{eventId}")
    int countByEvent(@Param("eventId") Long eventId);

    /**
     * 查询用户报名的所有活动
     * @param userId
     * @return
     */
    @Select("SELECT e.* FROM event e JOIN registration r ON r.event_id = e.id WHERE r.user_id = #{userId} ORDER BY e.event_time DESC, e.id DESC")
    List<Event> findEventsByUser(Long userId);
}
