Feature: Create Maintenance Request

  Scenario: Submit Maintenance Request and Create Maintenance Order
    When site engineer "Name engineer" submit maintenance request in BuildIT for a "Plant 1" from "2019-05-05" to "2030-06-06" with the issue "Failure in Plant"
    Then we query the new created maintenance request into rentit
    And we expect that the status is pending

  Scenario: Submit Maintenance Request will bad formatted input
    When site engineer "Guy" submit maintenance request in BuildIt for a "plant1" with issue "Malfunction"
    Then we expect a error response because there's no information about period of maintenance

