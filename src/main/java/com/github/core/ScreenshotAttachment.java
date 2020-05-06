package com.github.core;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class ScreenshotAttachment {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotAttachment.class);

    @Attachment(value = "Page screenshot", type = "image/png")
    public void attachFileToAllureReport(String fullFileName, String message, String attachmentDescription) {
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

    public File createFileIfNotExist(String fileName) {
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
