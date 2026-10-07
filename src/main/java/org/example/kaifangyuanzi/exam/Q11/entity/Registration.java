package org.example.kaifangyuanzi.exam.Q11.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 报名记录实体类
 */
@Data
public class Registration {
    /** 报名记录id */
    private Long id;
    /** 活动id */
    private Long eventId;
    /** 用户id */
    private Long userId;

    /** 报名时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime;
}
