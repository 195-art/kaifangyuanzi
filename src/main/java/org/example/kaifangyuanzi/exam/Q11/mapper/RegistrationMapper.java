package org.example.kaifangyuanzi.exam.Q11.mapper;


import org.apache.ibatis.annotations.*;
import org.example.kaifangyuanzi.exam.Q11.entity.Registration;

import java.util.List;

@Mapper
public interface RegistrationMapper {

    @Insert("INSERT INTO registration (event_id, user_id) VALUES (#{eventId}, #{userId})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insert(Registration registration);

    @Delete("DELETE FROM registration WHERE event_id = #{eventId} AND user_id = #{userId}")
    int deleteByEventAndUser(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM registration WHERE event_id = #{eventId} AND user_id = #{userId}")
    int countByEventAndUser(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM registration WHERE event_id = #{eventId}")
    int countByEvent(@Param("eventId") Long eventId);

    @Select("SELECT event_id FROM registration WHERE user_id = #{userId}")
    List<Long> findEventIdsByUser(@Param("userId") Long userId);



}
