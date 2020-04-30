Feature: GitHub testing

  Scenario Outline: Search for repository on Github
    Given I open Github login page page
    Then I check Login page is opened
    When I log in with "username" username and "password" password on Login page
    Then I check Welcome page is opened
    When I click on "<repository_name>" repository link on Welcome page
    Then I check Repository page is opened
    Then I check page header contains "<repository_name>" repository name on Repository page
    Examples:
      | repository_name |
      | testRepository1  |