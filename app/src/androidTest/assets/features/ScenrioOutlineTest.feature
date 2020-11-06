Feature: Create an event with outline
  Create a new calender event for testing

  Scenario Outline: Create calender event outline
    Given I have launched the Calendar App
    When It is not a <day>
    And Meeting is not repeated on successive days
    Then I want to book a meeting with the title “Recurring-Team Catch Up”
    And Set Meeting duration as <hours> in the evening
    And I save the meeting
    Then I Check if the meeting is created as expected

 Examples:
    |day|hours|
    |Friday| 3|
    |Saturday | 5 |
