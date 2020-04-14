package com.github.tests.stepdefs;

import com.github.ApplicationConfig;
import com.github.GithubTestExecutionListener;
import com.github.pages.LoginPage;
import com.github.pages.RepositoryPage;
import com.github.pages.WelcomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.testng.Assert;

@TestExecutionListeners(
        value = {GithubTestExecutionListener.class,
                DependencyInjectionTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@ContextConfiguration(classes = ApplicationConfig.class)
public class MyStepdefs {
    @Autowired
    private LoginPage loginPage;
    @Autowired
    private WelcomePage welcomePage;
    @Autowired
    private RepositoryPage repositoryPage;

    @Given("I open Github login page page")
    public void iOpenGithubLoginPagePage() {
        loginPage.openLoginPage();
        loginPage.waitForPageLoaded();
    }

    @When("I log in with {string} username and {string} password on Login page")
    public void iLogInWithUsernameAndPasswordOnLoginPage(String username, String password) {
        loginPage.setUsername(username);
        loginPage.setPassword(password);
        loginPage.getButtonSignIn().click();
        welcomePage.waitForPageLoaded();
    }

    @When("I click on {string} repository link on Welcome page")
    public void iClickOnRepositoryLinkOnWelcomePage(String repositoryName) {
        welcomePage.leftMenu.getLinkRepository(repositoryName).click();
        repositoryPage.waitForPageLoaded();
    }

    @Then("I check page header contains {string} repository name on Repository page")
    public void iCheckPageHeaderContainsRepositoryNameOnRepositoryPage(String repositoryName) {
        boolean contains = repositoryPage.getHeader().getText().contains(repositoryName);
        Assert.assertTrue(contains, String.format("Page header doesn't contain '%s' repository name", repositoryName));
    }
}