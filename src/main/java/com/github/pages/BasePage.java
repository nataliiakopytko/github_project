package com.github.pages;

import com.github.core.DriverWaiter;
import com.github.core.drivermanager.DriverProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BasePage {
    @Autowired
    public DriverProvider browser;
    @Autowired
    public DriverWaiter waiter;

    public abstract boolean verify();

    public abstract void waitForPageLoaded();
}