package org.example.kaifangyuanzi.exam.Q11.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Event {
    private Long id;
    private String title;
    private String description;
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;

    private Integer capacity;
    private Long creatorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String status;

    public String getStatus() {
        if (eventTime == null) return null;
        return eventTime.isAfter(LocalDateTime.now()) ? "即将开始" : "已结束";
    }
}
