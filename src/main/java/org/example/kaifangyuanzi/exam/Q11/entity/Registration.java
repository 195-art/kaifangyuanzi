package org.example.kaifangyuanzi.exam.Q11.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Registration {
    private Long id;
    private Long eventId;
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registrationDate;
}
