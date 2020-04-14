package com.github.tests;

import com.github.core.drivermanager.DriverProvider;
import io.cucumber.java.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    @Autowired
    private DriverProvider driverProvider;

    @After
    public void tearDown() {
        logger.info("---Running hook After---");
        driverProvider.closeDriver();
    }
}
