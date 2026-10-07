package org.example.kaifangyuanzi.exam.Q11.mapper;

import org.apache.ibatis.annotations.*;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EventMapper {
    //活动列表分页查询的动态过滤条件
    String FILTER = " WHERE (#{keyword} IS NULL OR title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')) "
            + "AND (#{status} IS NULL OR (#{status} = '即将开始' AND event_time > NOW()) OR (#{status} = '已结束' AND event_time <= NOW())) ";

    /**
     * 分页查询活动
     * @param keyword
     * @param status
     * @param offset
     * @param size
     * @return
     */
    @Select("SELECT * FROM event" + FILTER + "ORDER BY event_time DESC, id DESC LIMIT #{offset}, #{size}")
    List<Event> selectPage(@Param("keyword") String keyword, @Param("status") String status,
                           @Param("offset") long offset, @Param("size") int size);

    /**
     * 统计符合条件的活动数量
     * @param keyword
     * @param status
     * @return
     */
    @Select("SELECT COUNT(*) FROM event" + FILTER)
    long count(@Param("keyword") String keyword, @Param("status") String status);

    /**
     * 根据id查询活动
     * @param id
     * @return
     */
    @Select("SELECT * FROM event WHERE id = #{id}")
    Event selectById(Long id);

    /**
     * 根据id查询活动并加锁
     * @param id
     * @return
     */
    @Select("SELECT * FROM event WHERE id = #{id} FOR UPDATE")
    Event selectByIdForUpdate(Long id);

    /**
     * 查询下一个活动的开始时间
     * @return
     */
    @Select("SELECT MIN(event_time) FROM event WHERE event_time > NOW()")
    LocalDateTime nextEventTime();

    /**
     * 新增活动
     * @param event
     * @return
     */
    @Insert("INSERT INTO event (title, description, location, event_time, capacity, creator_id) "
            + "VALUES (#{title}, #{description}, #{location}, #{eventTime}, #{capacity}, #{creatorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Event event);

    /**
     * 修改活动
     * @param event
     * @return
     */
    @Update("UPDATE event SET title = #{title}, description = #{description}, location = #{location}, "
            + "event_time = #{eventTime}, capacity = #{capacity} WHERE id = #{id}")
    int update(Event event);

    /**
     * 根据id删除活动
     * @param id
     * @return
     */
    @Delete("DELETE FROM event WHERE id = #{id}")
    int deleteById(Long id);
}
