package org.example.kaifangyuanzi.exam.Q11.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动实体类
 */
@Data
public class Event {
    /** 活动id */
    private Long id;
    /** 活动标题 */
    private String title;
    /** 活动描述 */
    private String description;
    /** 举办地点 */
    private String location;

    /** 活动时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;

    /** 活动名额 */
    private Integer capacity;
    /** 创建人id */
    private Long creatorId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    /** 活动状态 */
    private String status;

    /**
     * 根据活动时间计算活动状态
     * @return
     */
    public String getStatus() {
        if (eventTime == null) return null;
        return eventTime.isAfter(LocalDateTime.now()) ? "即将开始" : "已结束";
    }
}
