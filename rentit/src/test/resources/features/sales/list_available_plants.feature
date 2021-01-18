Feature: List available plants in period of time
  Scenario: GET a detail of plant entry
    Given the default test case
    When I request "GET /api/sales/plants/1?startDate=2020-01-01&endDate=2020-12-01"
    Then the response status code should be 200
    And the "available" property should equal "true"
    And the "totalPrice" property should equal "10"
