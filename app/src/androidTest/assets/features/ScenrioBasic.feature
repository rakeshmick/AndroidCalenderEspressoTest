Feature: Create an event
  Create a new calender event for testing

  Scenario: Create calender event
    Given I have launched the Calendar App
    When It is not a Friday
    And Meeting is not repeated on successive days
    Then I want to book a meeting with the title “Recurring-Team Catch Up”
    And Set Meeting duration as 5 in the evening
    And I save the meeting
    Then I Check if the meeting is created as expected


