package com.videos;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.videos.mapper")
@SpringBootApplication(scanBasePackages ={ "com.videos","org.n3r.idworker"},exclude={DruidDataSourceAutoConfigure.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
