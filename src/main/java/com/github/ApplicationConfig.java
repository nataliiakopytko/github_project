package com.github;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;

@Configuration
@ComponentScan(basePackages = {"com.github"})
@PropertySource(value = {"application.properties"})
@DirtiesContext
public class ApplicationConfig {
}
