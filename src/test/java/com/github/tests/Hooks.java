package com.github.tests;

import com.github.core.drivermanager.DriverProvider;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;


public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    @Autowired
    private DriverProvider driverProvider;

    @After(order = 20)
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
            attachFileToAllureReport(screenshotName, "Taking screenshot ", "SCREENSHOT");
        }
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    private void attachFileToAllureReport(String fullFileName, String message, String attachmentDescription) {
        try {
            final File absoluteFile = new File(fullFileName).getAbsoluteFile();
            logger.info(message + absoluteFile.getAbsolutePath());
            Allure.addAttachment(attachmentDescription, new FileInputStream(absoluteFile));
            Allure.addStreamAttachmentAsync(message, "ATT", () -> {
                try {
                    return new FileInputStream(absoluteFile);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException();
                }
            });
        } catch (FileNotFoundException e) {
            logger.error(String.format("CANNOT ATTACH '%s' to ALLURE REPORT", fullFileName));
            e.printStackTrace();
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
            ImageIO.write(fpScreenshot.getImage(), "PNG", createFileIfNotExist(fullFileName));
        } catch (IOException e) {
            throw new WebDriverException(e.getMessage());
        }
        return fullFileName;
    }

    public static File createFileIfNotExist(String fileName) {
        File file = new File(fileName).getAbsoluteFile();
        try {
            if (!file.exists()) {
                boolean isDirCreated = file.getParentFile().mkdirs();
                logger.info(String.format("Method mkdirs() returned '%s' for directory '%s'.", isDirCreated, file.getParentFile()));
                boolean isFileCreated = file.createNewFile();
                logger.info(String.format("Method createNewFile() returned '%s' for file '%s'.", isFileCreated, file));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return file;
    }
}
