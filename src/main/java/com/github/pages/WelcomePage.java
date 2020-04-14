package com.github.pages;

import com.github.core.PropertiesLoader;
import com.github.core.TimeOutConstants;
import com.github.pages.components.LeftMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WelcomePage extends BasePage {
    private static final String PAGE_URL = PropertiesLoader.getBaseGithubUrl();

    @Autowired
    public LeftMenu leftMenu;

    @Override
    public boolean verify() {
        return browser.getDriver().getCurrentUrl().equals(PAGE_URL);
    }

    @Override
    public void waitForPageLoaded() {
        waiter.waitForElementDisplayed(leftMenu.getInputRepositories(), TimeOutConstants.DEFAULT_TIMEOUT_5_000_MS);
    }
}