Feature: Query completed maintenance order

  Scenario: Query a maintenance order with status completed
    When Site engineer request for the status of the maintenance request "1"
    Then the response should contain the status "COMPLETED"
