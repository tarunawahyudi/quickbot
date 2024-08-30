package com.quickbot.app.component.service;

import com.quickbot.app.component.model.entities.User;
import com.quickbot.app.component.model.entities.UserInformation;
import com.quickbot.app.component.model.enums.Gender;
import com.quickbot.app.core.config.ApplicationConfig;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
@AllArgsConstructor
public class InitializationService {

    private final EmailService emailService;
    private final ApplicationConfig applicationConfig;

    @PostConstruct
    public void init() throws Exception {

        Faker faker = applicationConfig.getFaker();

        UserInformation userInformation = UserInformation.builder()
                .day(faker.number().numberBetween(1, 31))
                .month(faker.number().numberBetween(1, 12))
                .year(faker.number().numberBetween(1990, 2005))
                .gender(Gender.getRandomGender())
                .build();

        User user = User.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .username(faker.internet().username())
                .password(faker.internet().password())
                .userInformation(userInformation)
                .build();

        emailService.registration(user);
    }
}
