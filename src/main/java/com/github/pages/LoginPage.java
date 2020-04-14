package com.github.pages;

import com.github.core.PropertiesLoader;
import com.github.core.TimeOutConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class LoginPage extends BasePage {
    private static final String PAGE_URL = PropertiesLoader.getBaseGithubUrl() + "login";

    private static final String INPUT_USENNAME = "//input[@id='login_field']";
    private static final String INPUT_PASSWORD = "//input[@id='password']";
    private static final String BUTTON_SIGN_IN = "//input[@type='submit']";

    private WebElement getInputUsername() {
        return browser.getDriver().findElement(byInputUsername());
    }

    private WebElement getInputPassword() {
        return browser.getDriver().findElement(byInputPassword());
    }

    public WebElement getButtonSignIn() {
        return browser.getDriver().findElement(byButtonSignIn());
    }

    public void setUsername(String username) {
        getInputUsername().sendKeys(PropertiesLoader.getProperty(PropertiesLoader.pathToGithubPropertyFile, username));
    }

    public void setPassword(String password) {
        getInputPassword().sendKeys(PropertiesLoader.getProperty(PropertiesLoader.pathToGithubPropertyFile, password));
    }

    public void openLoginPage() {
        browser.getDriver().navigate().to(PAGE_URL);
    }

    @Override
    public boolean verify() {
        return getInputUsername().isDisplayed();
    }

    @Override
    public void waitForPageLoaded() {
        waiter.waitForElementDisplayed(getInputUsername(), TimeOutConstants.DEFAULT_TIMEOUT_5_000_MS);
    }

    private By byInputUsername() {
        return By.xpath(INPUT_USENNAME);
    }

    private By byInputPassword() {
        return By.xpath(INPUT_PASSWORD);
    }

    private By byButtonSignIn() {
        return By.xpath(BUTTON_SIGN_IN);
    }

}
