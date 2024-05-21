
Feature: Make an appointment

    Scenario: Make an appointment with a doctor
        Given I am on Phihub Patient Website
        And I am logged in as a patient
        When I click on the Make an appointment button
        And I select which speciality I want to make an appointment in
        And I select which doctor I want to make an appointment with
        And I select the date and time I want to make an appointment
        And I click on the confirm button
        Then I should see a confirmation message



