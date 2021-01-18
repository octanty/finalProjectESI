Feature: List available plants in period of time
  Scenario: GET a collection of entry plants
    Given the default test case
    When I request "GET /api/sales/plants?startDate=2020-01-01&endDate=2020-12-01"
    Then the response status code should be 200
    And the "_embedded.plantInventoryItemDToes" property should be an array
    And the "_embedded.programmers" property should contain 4 items
    And the "_embedded.programmers.0.serialNumber" property should equal "A1"
