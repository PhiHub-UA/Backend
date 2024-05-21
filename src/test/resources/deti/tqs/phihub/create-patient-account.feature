
Feature: Create a patient account 

    Scenario: Patients create his account

        Given User is in the website
        When User clicks the Login button
        And The user inserts his name as "Josefino"
        And The user inserts his username as "jose_fino"
        And The user inserts his password as "josefino123"
        And The user inserts his phone as 919828737
        And The user inserts his email as "jose@fino.com"
        And The user inserts his age as 37
        And The user clicks on the Register button
        Then User should see the register confirmation

