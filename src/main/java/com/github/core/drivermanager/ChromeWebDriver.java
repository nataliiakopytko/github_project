package com.github.core.drivermanager;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;

@Component
@Profile("chrome")
public class ChromeWebDriver implements DriverProvider {
    private static final Logger logger = LoggerFactory.getLogger(ChromeWebDriver.class);
    private WebDriver driver;

    @Override
    @Bean(destroyMethod = "quit")
    public WebDriver getDriver() {
        if (Objects.isNull(driver)) {
            logger.info("======Setting up Chrome browser======");
            WebDriverManager.getInstance(CHROME).setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            driver = new ChromeDriver(options);
        }
        return driver;
    }

    @Override
    public void closeDriver() {
        if (driver != null) {
            logger.info("======Closing Chrome browser======");
            driver.close();
            driver.quit();
            driver = null;
            logger.info("The Chrome browser has been closed.");
        }
    }
    @Override
    public boolean isDriverExist(){
        return Objects.nonNull(driver);
    }
}