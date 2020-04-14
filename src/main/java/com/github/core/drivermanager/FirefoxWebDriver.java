package com.github.core.drivermanager;

import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Profile("firefox")
public class FirefoxWebDriver implements DriverProvider {
    private static final Logger logger = LoggerFactory.getLogger(FirefoxWebDriver.class);
    private WebDriver driver;

    @Override
    @Bean(destroyMethod = "quit")
    public WebDriver getDriver() {
        if (Objects.isNull(driver)) {
            logger.info("======Setting up Firefox browser======");
            WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
            driver = new FirefoxDriver();
        }
        return driver;
    }

    @Override
    public void closeDriver() {
        if (driver != null) {
            logger.info("======Closing Firefox browser======");
            try {
                driver.close();
                driver.quit();
            } catch (Exception e) {
                logger.warn(e.getMessage());
            } finally {
                driver = null;
                logger.info("The Firefox browser has been closed.");
            }
        }
    }
}