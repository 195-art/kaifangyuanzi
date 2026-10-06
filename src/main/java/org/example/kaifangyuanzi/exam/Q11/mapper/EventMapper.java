package org.example.kaifangyuanzi.exam.Q11.mapper;

import org.apache.ibatis.annotations.*;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EventMapper {
    String FILTER = " WHERE (#{keyword} IS NULL OR title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')) "
            + "AND (#{status} IS NULL OR (#{status} = '即将开始' AND event_time > NOW()) OR (#{status} = '已结束' AND event_time <= NOW())) ";

    @Select("SELECT * FROM event" + FILTER + "ORDER BY event_time DESC, id DESC LIMIT #{offset}, #{size}")
    List<Event> selectPage(@Param("keyword") String keyword, @Param("status") String status,
                           @Param("offset") long offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM event" + FILTER)
    long count(@Param("keyword") String keyword, @Param("status") String status);

    @Select("SELECT * FROM event WHERE id = #{id}")
    Event selectById(Long id);

    @Select("SELECT * FROM event WHERE id = #{id} FOR UPDATE")
    Event selectByIdForUpdate(Long id);

    @Select("SELECT MIN(event_time) FROM event WHERE event_time > NOW()")
    LocalDateTime nextEventTime();

    @Insert("INSERT INTO event (title, description, location, event_time, capacity, creator_id) "
            + "VALUES (#{title}, #{description}, #{location}, #{eventTime}, #{capacity}, #{creatorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Event event);

    @Update("UPDATE event SET title = #{title}, description = #{description}, location = #{location}, "
            + "event_time = #{eventTime}, capacity = #{capacity} WHERE id = #{id}")
    int update(Event event);

    @Delete("DELETE FROM event WHERE id = #{id}")
    int deleteById(Long id);
}
