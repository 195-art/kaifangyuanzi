package org.example.kaifangyuanzi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.kaifangyuanzi.exam.Q10.mapper")
public class KaifangyuanziApplication {

    public static void main(String[] args) {

        SpringApplication.run(KaifangyuanziApplication.class, args);
    }

}
