package com.um;

import com.um.util.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@MapperScan("com.um.mapper")
@EntityScan(basePackages={"com.um.domain.po"})
@SpringBootApplication
@EnableAutoConfiguration
@Import(value={SpringContextUtil.class})
public class UrbanMineralApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrbanMineralApplication.class, args);
    }
}
