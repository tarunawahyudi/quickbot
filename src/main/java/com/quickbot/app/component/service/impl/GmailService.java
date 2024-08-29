package com.quickbot.app.component.service.impl;

import com.quickbot.app.component.model.User;
import com.quickbot.app.component.service.EmailService;
import com.quickbot.app.core.config.ApplicationConfig;
import com.quickbot.app.core.config.ApplicationProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@AllArgsConstructor
@Service
public class GmailService implements EmailService {

    private final ApplicationProperties properties;
    private final ApplicationConfig config;

    @Override
    public void registration(User user) {
        Faker faker = config.getFaker();
        WebDriver driver = config.getWebDriver();
        String url = properties.getUrl();

        try {

            driver.get(url);
            setCookie(driver);

            driver.findElement(By.id("firstName")).sendKeys(user.getFirstName());
            driver.findElement(By.id("lastName")).sendKeys(user.getLastName());

            this.next(driver, 10, "gender");

            driver.findElement(By.id("day")).sendKeys(String.valueOf(user.getUserInformation().getDay()));
            selectDropdown(driver, "month", String.valueOf(user.getUserInformation().getMonth()));

            driver.findElement(By.id("year")).sendKeys(String.valueOf(user.getUserInformation().getYear()));
            selectDropdown(driver, "gender", String.valueOf(faker.number().numberBetween(1, 4)));

            this.next(driver, 5, "Pilih alamat Gmail Anda");
            this.selectRadioOption(driver);

        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage());
        }
    }

    private void selectDropdown(WebDriver driver, String selector, String option) {
        WebElement element = driver.findElement(By.id(selector));
        Select select = new Select(element);
        select.selectByValue(option);
    }

    private void next(WebDriver driver, long duration, String presence) {
        driver.findElement(By.cssSelector("[jsname='LgbsSe']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(duration));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(presence)));
    }

    private void selectRadioOption(WebDriver driver) {

    }

    private void setCookie(WebDriver driver) {
        Faker faker = config.getFaker();

        String cookieName = faker.lorem().word();
        String cookieValue = faker.lorem().sentence();

        Cookie cookie = new Cookie.Builder(cookieName, cookieValue)
                .domain(properties.getDomain())
                .isSecure(true)
                .path(properties.getPath())
                .build();

        driver.manage().addCookie(cookie);

        Cookie addedCookie = driver.manage().getCookieNamed(cookieName);
        log.info("Cookie added: {}", addedCookie);
    }
}
