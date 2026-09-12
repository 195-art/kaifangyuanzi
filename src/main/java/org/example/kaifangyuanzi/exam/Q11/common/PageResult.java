package org.example.kaifangyuanzi.exam.Q11.common;


import lombok.Data;

import java.util.List;

@Data
public class PageResult <T>{
    private List<T> records;
    private long total;
    private int page;
    private int size;
}
