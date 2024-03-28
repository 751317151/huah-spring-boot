package com.huah.huahspringbootweb;

import com.huah.test.config.EnableRegisterConfigMarker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRegisterConfigMarker
public class HuahSpringBootWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuahSpringBootWebApplication.class, args);
    }

}
