
Feature: Check for old appointments 

    Scenario: User checks his appointments

        Given User is logged in
        When User clicks the Check Old Appointments button
        Then User should see his appointments

