package com.skhu.moodfriend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MoodfriendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoodfriendApplication.class, args);
    }

}
