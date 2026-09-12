package org.example.kaifangyuanzi;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan(value = "org.example.kaifangyuanzi.exam", annotationClass = Mapper.class)
public class KaifangyuanziApplication {

    public static void main(String[] args) {

        SpringApplication.run(KaifangyuanziApplication.class, args);
    }

}
