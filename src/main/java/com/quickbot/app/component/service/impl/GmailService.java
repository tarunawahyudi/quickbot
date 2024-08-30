package com.quickbot.app.component.service.impl;

import com.quickbot.app.component.model.entities.User;
import com.quickbot.app.component.service.EmailService;
import com.quickbot.app.core.config.ApplicationConfig;
import com.quickbot.app.core.config.ApplicationProperties;
import com.quickbot.app.core.config.ApplicationResource;
import com.quickbot.app.core.constant.SpecialCharacter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
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
    private final ApplicationResource applicationResource;

    @Override
    public void registration(User user) {

        try {
            Faker faker = config.getFaker();
            WebDriver driver = config.getWebDriver();
            String url = properties.getUrl();
            driver.get(url);
            setCookie(driver);

            driver.findElement(By.id("firstName")).sendKeys(user.getFirstName());
            driver.findElement(By.id("lastName")).sendKeys(user.getLastName());

            this.next(driver);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("day")));

            driver.findElement(By.id("day")).sendKeys(String.valueOf(user.getUserInformation().getDay()));
            select(driver, "month", String.valueOf(user.getUserInformation().getMonth()));
            driver.findElement(By.id("year")).sendKeys(String.valueOf(user.getUserInformation().getYear()));
            select(driver, "gender", String.valueOf(faker.number().numberBetween(1, 4)));

            this.next(driver);
            String email = chooseEmailOption(driver);
            email = email.concat(SpecialCharacter.UNDERSCORE).concat(properties.getPrefix());
            log.info("email is: {}", email);

            this.next(driver);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Passwd")));

            driver.findElement(By.name("Passwd")).sendKeys(user.getPassword());
            driver.findElement(By.name("PasswdAgain")).sendKeys(user.getPassword());

            this.next(driver);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                applicationResource.getProperties("button.next")
            )));

            clickSkipButton(driver);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                applicationResource.getProperties("button.next")
            )));

            clickIAgreeButton(driver);
            log.info("done");

        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage());
        }
    }

    private void select(WebDriver driver, String selector, String option) {
        WebElement element = driver.findElement(By.id(selector));
        Select select = new Select(element);
        select.selectByValue(option);
    }

    private void next(WebDriver driver) throws Exception {
        driver.findElement(By.cssSelector(
            applicationResource.getProperties("button.next")
        )).click();
    }

    private void clickSkipButton(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement skipButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='Skip']]")
            ));
            skipButton.click();
        } catch (TimeoutException e) {
            log.error("Tidak dapat menemukan tombol Skip: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Kesalahan saat mengklik tombol Skip: {}", e.getMessage());
        }
    }

    private void clickIAgreeButton(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement agreeButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='I agree']]")
            ));
            agreeButton.click();
        } catch (TimeoutException e) {
            log.error("Tidak dapat menemukan tombol 'I agree': {}", e.getMessage());
        } catch (Exception e) {
            log.error("Kesalahan saat mengklik tombol 'I agree': {}", e.getMessage());
        }
    }

    private String chooseEmailOption(WebDriver driver) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement emailRadioOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(applicationResource.getProperties("email.select"))
            ));

            emailRadioOption.click();

            String attribute = emailRadioOption.getAttribute("data-value");
            log.info("success selected email: {}", attribute);

            return attribute;
        } catch (TimeoutException e) {
            log.error("email radio option is not found: {}", e.getMessage());
            throw new TimeoutException(e);
        } catch (Exception e) {
            log.error("some error {}", e.getMessage());
            throw new Exception(e);
        }
    }

    private void choice(WebDriver driver) {

    }

    private void setCookie(WebDriver driver) {
        Faker faker = config.getFaker();

        String cookieName = "cookieName";
        String cookieValue = "cookieValue";

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
