package com.quickbot.app.core.config;

import net.datafaker.Faker;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class ApplicationConfig {

    @Bean
    public WebDriver getWebDriver() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    @Bean
    public Faker getFaker() {
        return new Faker(Locale.forLanguageTag("id-ID"));
    }
}
