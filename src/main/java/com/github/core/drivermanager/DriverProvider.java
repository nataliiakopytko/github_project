package com.github.core.drivermanager;

import org.openqa.selenium.WebDriver;

public interface DriverProvider {
    WebDriver getDriver();

    void closeDriver();
}
