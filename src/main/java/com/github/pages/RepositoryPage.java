package com.github.pages;

import com.github.core.PropertiesLoader;
import com.github.core.TimeOutConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RepositoryPage extends BasePage {
    private static final String PAGE_URL = PropertiesLoader.getBaseGithubUrl() + "%s/%s";
    private static final String HEADER_MASK = "^(\\w+)\\/(\\w+)$";

    private static final String HEADER_TEXT_XPATH = "//div[contains(@class,'pagehead')]//h1";

    public WebElement getHeader() {
        return browser.getDriver().findElement(byHeader());
    }

    @Override
    public boolean verify() {
        Pattern pattern = Pattern.compile(HEADER_MASK, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(getHeader().getText().replace("\n", ""));
        m.matches();
        String userName = m.group(1);
        String repositoryName = m.group(2);
        String pageUrl = String.format(PAGE_URL, userName, repositoryName);
        return browser.getDriver().getCurrentUrl().equals(pageUrl);
    }

    @Override
    public void waitForPageLoaded() {
        waiter.waitForElementDisplayed(getHeader(), TimeOutConstants.DEFAULT_TIMEOUT_5_000_MS);
    }

    private By byHeader() {
        return By.xpath(HEADER_TEXT_XPATH);
    }
}