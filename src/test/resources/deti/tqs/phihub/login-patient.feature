
Feature: Login a patient account 

    Scenario: Patients logs in to his account

        Given User is in the login page
        When User clicks the Login tab
        And The user inserts his account username as "jose_fino"
        And The user inserts his account password as "josefino123"
        And The user clicks on the Login Button
        Then User should see the login confirmation

