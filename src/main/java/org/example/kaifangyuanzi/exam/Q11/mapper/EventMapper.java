package org.example.kaifangyuanzi.exam.Q11.mapper;

import org.apache.ibatis.annotations.*;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;

import java.util.List;

@Mapper
public interface EventMapper {

    // keyword：按标题模糊搜索；status：按"即将开始/已结束"过滤（用当前时间跟活动时间比）
    @Select("SELECT *, CASE WHEN event_time > NOW() THEN '即将开始' ELSE '已结束' END AS status " +
            "FROM event " +
            "WHERE (#{keyword} IS NULL OR title LIKE CONCAT('%', #{keyword}, '%')) " +
            "AND (#{status} IS NULL OR (#{status} = '即将开始' AND event_time > NOW()) OR (#{status} = '已结束' AND event_time < NOW())) " +
            "ORDER BY event_time DESC LIMIT #{offset}, #{size}")
    List<Event> selectPage(@Param("keyword") String keyword, @Param("status") String status,
                           @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM event " +
            "WHERE (#{keyword} IS NULL OR title LIKE CONCAT('%', #{keyword}, '%')) " +
            "AND (#{status} IS NULL OR (#{status} = '即将开始' AND event_time > NOW()) OR (#{status} = '已结束' AND event_time < NOW()))")
    long count(@Param("keyword") String keyword, @Param("status") String status);

    @Select("SELECT * FROM event WHERE id = #{id}")
    Event selectById(Long id);

    @Insert("INSERT INTO event (title, description, location, event_time, capacity, creator_id) " +
            "VALUES (#{title}, #{description}, #{location}, #{eventTime}, #{capacity}, #{creatorId})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insert(Event event);

    @Update("UPDATE event SET title = #{title}, description = #{description}, location = #{location}, " +
            "event_time = #{eventTime}, capacity = #{capacity} WHERE id = #{id}")
    int update(Event event);

    @Delete("DELETE FROM event WHERE id = #{id}")
    int deleteById(Long id);

}
