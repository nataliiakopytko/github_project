Feature: GitHub testing

  Scenario: Login to Github
    Given I open Github login page page
    Then I check Login page is opened
    When I log in with "username" username and "password" password on Login page
    Then I check Welcome page is opened