package com.github.tests.hooks;

import com.github.core.ScreenshotAttachment;
import com.github.core.drivermanager.DriverProvider;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    @Autowired
    private DriverProvider driverProvider;

    @Autowired
    private ScreenshotAttachment screenshotAttachment;

    @After(order = 10)
    public void embedScreenshot(Scenario scenario) {
        String projectFolder = "target/screenshots";
        embedScreenshotFromFile(scenario, projectFolder);
    }

    @After(order = 5)
    public void tearDown() {
        logger.info("---Running hook After---");
        driverProvider.closeDriver();
    }

    private void embedScreenshotFromFile(Scenario scenario, String projectFolder) {
        if (scenario.isFailed() && driverProvider.isDriverExist()) {
            String cucumberReportPrefix = "\\..\\..\\..\\..\\..\\..\\";
            String screenshotName = saveScreenshot(scenario, projectFolder);
            String relativeScreenshotPath = cucumberReportPrefix + screenshotName;
            String html = "<html><body><a href=\">" + relativeScreenshotPath + "\">" + screenshotName + "</a></body></html>";
            scenario.embed(html.getBytes(), "text/html", "Final screenshot");
            screenshotAttachment.attachFileToAllureReport(screenshotName, "Taking screenshot ", "SCREENSHOT");
        }
    }

    private String saveScreenshot(Scenario scenario, String projectFolder) {
        Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
                .takeScreenshot(driverProvider.getDriver());
        String scenarioName = scenario.getName() + String.format(" (Line %s)", scenario.getLine());
        String fileName = scenarioName.replaceAll("[\\\\/:*?\"<>|]", "");
        ;
        String fullFileName = Paths.get(String.format("%s%s.png",
                projectFolder + File.separator + File.separator, fileName)).toString();
        try {
            ImageIO.write(fpScreenshot.getImage(), "PNG", screenshotAttachment.createFileIfNotExist(fullFileName));
        } catch (IOException e) {
            throw new WebDriverException(e.getMessage());
        }
        return fullFileName;
    }
}