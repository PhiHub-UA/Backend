
Feature: Check for old appointments 

    Scenario: User checks his appointments

        Given User is on the check appointments page
        When User is logged in as a patient with name "jose_fino" and password "josefino123"
        And User clicks the Check Appointments button
        Then User should see his appointments

