package org.example.kaifangyuanzi.exam.Q11.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 分页查询结果
 */
@Data
public class PageResult<T> {
    /** 当前页数据 */
    private List<T> records;
    /** 总记录数 */
    private long total;
    /** 当前页码 */
    private int page;
    /** 每页数量 */
    private int size;
    /** 缓存过期时间（不参与序列化） */
    @JsonIgnore
    private LocalDateTime expiresAt;
}
