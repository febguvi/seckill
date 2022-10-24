package com.gunjo.skilldemo;

import org.mybatis.spring.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 指定要变成接口实现类所在的包，然后包下面的所有接口在编译之后都会生成相应的实现类
@MapperScan("com.gunjo.skilldemo.mapper")
public class SkillDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillDemoApplication.class, args);
    }

}
