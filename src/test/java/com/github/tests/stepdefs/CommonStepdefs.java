package com.github.tests.stepdefs;

import com.github.GithubTestExecutionListener;
import com.github.pages.LoginPage;
import com.github.pages.RepositoryPage;
import com.github.pages.WelcomePage;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.testng.Assert;

@TestExecutionListeners(
        value = {GithubTestExecutionListener.class,
                DirtiesContextTestExecutionListener.class,
                DependencyInjectionTestExecutionListener.class})
public class CommonStepdefs {
    @Autowired
    private LoginPage loginPage;
    @Autowired
    private WelcomePage welcomePage;
    @Autowired
    private RepositoryPage repositoryPage;

    @Then("I check Login page is opened")
    public void iCheckLoginPageIs() {
        Assert.assertTrue(loginPage.verify(), "Login page is not opened");
    }

    @Then("I check Welcome page is opened")
    public void iCheckWelcomePageIs() {
        Assert.assertTrue(welcomePage.verify(), "Welcome page is not opened");
    }

    @Then("I check Repository page is opened")
    public void iCheckRepositoryPageIs() {
        Assert.assertTrue(repositoryPage.verify(), "Repository page is not opened");
    }
}