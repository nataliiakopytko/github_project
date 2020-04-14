package com.github.pages.components;

import com.github.core.drivermanager.DriverProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class LeftMenu {
    @Autowired
    private DriverProvider browser;

    private static final String LINK_REPOSITORY = "//ul[@data-filterable-for='dashboard-repos-filter-left']//a[contains(@data-hovercard-type,'repository')]";
    private static final String INPUT_REPOSITORIES = "//input[@id='dashboard-repos-filter-left']";

    public WebElement getLinkRepository(String repositoryName) {
        return browser.getDriver().findElements(byLinkRepositories()).stream()
                .filter(webElement -> webElement.getText().contains(repositoryName))
                .findFirst()
                .orElseThrow(() -> new AssertionError(String.format("Repository with name '%s' is not found", repositoryName)));
    }

    public WebElement getInputRepositories() {
        return browser.getDriver().findElement(byInputRepositories());
    }

    private By byLinkRepositories() {
        return By.xpath(LINK_REPOSITORY);
    }

    private By byInputRepositories() {
        return By.xpath(INPUT_REPOSITORIES);
    }
}