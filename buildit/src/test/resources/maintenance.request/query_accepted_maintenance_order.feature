Feature: Query accepted maintenance order


  Scenario: Query an existing and valid maintenance request for looking its acceptance
    When Site engineer queries for the maintenance order "1"
    Then Site engineer expects a response with status "ACCEPTED"

  Scenario: Request for cancelation of maintenance order in a correct state
    When Site engineer sends cancelation for maintenance order "1"
    Then the reponse should be successful, meaning that the state was valid
