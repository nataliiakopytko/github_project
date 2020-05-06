package com.github.tests.hooks;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.*;
import com.github.core.PropertiesLoader;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;

import java.text.SimpleDateFormat;
import java.util.*;

public class TestRailHooks {
    private final static String endPoint = PropertiesLoader.getProperty(PropertiesLoader.pathToTestrailPropertyFile, "api.url");
    private final static String username = PropertiesLoader.getProperty(PropertiesLoader.pathToTestrailPropertyFile, "api.user");
    private final static String password = PropertiesLoader.getProperty(PropertiesLoader.pathToTestrailPropertyFile, "api.password");
    private final static String newRun = PropertiesLoader.getProperty(PropertiesLoader.pathToTestrailPropertyFile, "is.new.run.allowed");

    private static final int projectId = 7;
    private static final int suiteId = 12;

    private static TestRail testRail;
    private static Project project;
    private static Suite suite;
    private static Case testCase;
    private static List<CaseField> caseFields;
    private static List<Case> testRailCases;
    private static Run run;

    private static final boolean hasTestRailCreationAllowed = Boolean.parseBoolean(newRun);

    static {
        if (hasTestRailCreationAllowed) {
            testRail = TestRail.builder(endPoint, username, password).applicationName("Github_Project_2").build();
            project = testRail.projects().get(projectId).execute();
            suite = testRail.suites().get(suiteId).execute();
            caseFields = testRail.caseFields().list().execute();
            testRailCases = testRail.cases().list(projectId, suiteId, caseFields).execute();
            run = testRail.runs().add(projectId, new Run().setSuiteId(suiteId).setName("Github Test Run " + getDate())).execute();
        }
    }

    @After(order = 3)
    public void connectScenarioFromCucumberWithTestRail(Scenario scenario) {
        if (hasTestRailCreationAllowed) {
            testCase = testRailCases.stream()
                    .filter(trc -> trc.getTitle().equalsIgnoreCase(scenario.getName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(scenario.getName() + "is not found in Test Rail"));
        }
    }

    @After(order = 2)
    public void setStatusToCase(Scenario scenario) {
        if (hasTestRailCreationAllowed) {
            List<ResultField> customResultFields = testRail.resultFields().list().execute();
            testRail.tests().list(run.getId()).execute()
                    .stream()
                    .filter(test -> test.getCaseId() == testCase.getId())
                    .findFirst()
                    .ifPresent(test -> testRail.results()
                            .addForCase(run.getId(), test.getCaseId(), new Result().setStatusId(getStatusMap().get(scenario.getStatus())),
                                    customResultFields).execute());
        }
    }

    private Map<Status, Integer> getStatusMap() {
        Map<Status, Integer> map = new HashMap<>();
        map.put(Status.PASSED, 1);
        map.put(Status.UNDEFINED, 2);
        map.put(Status.SKIPPED, 3);
        map.put(Status.PENDING, 4);
        map.put(Status.FAILED, 5);
        return map;
    }

    private static String getDate() {
        String dateFormatPattern = "dd-MM-yyyy HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        date = cal.getTime();
        return dateFormat.format(date);
    }
}