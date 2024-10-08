package com.skhu.moodfriend.global.config;

import com.skhu.moodfriend.app.domain.store.Objects;
import com.skhu.moodfriend.app.domain.store.ObjectStore;
import com.skhu.moodfriend.app.repository.ObjectStoreRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ObjectInit {

    private final ObjectStoreRepository objectStoreRepository;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            for (Objects objects : Objects.values()) {
                objectStoreRepository.findByObject(objects)
                        .orElseGet(() -> objectStoreRepository.save(ObjectStore.builder()
                                .object(objects)
                                .build()));
            }
        };
    }
}
