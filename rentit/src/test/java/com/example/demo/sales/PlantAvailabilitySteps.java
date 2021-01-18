package com.example.demo.sales;

import com.example.demo.DemoApplication;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = DemoApplication.class)
@Sql(scripts = "/plants-dataset.sql")
@WebAppConfiguration
public class PlantAvailabilitySteps {

    private String BASE_URL = "http://localhost:8080";
    public int statusCode = 200;

    @Given("^the default test case$")
    public void the_default_test_case() throws Throwable {
        throw new PendingException();
    }

    @When("^I request \"([^\"]*)\"$")
    public void i_request(String arg1) throws Throwable {
        RestTemplate restTemplate = new RestTemplate();

        JSONObject requests = restTemplate.getForObject(BASE_URL + "/api/sales/plants?name=Plant&startDate=2020-01-01&endDate=2020-12-01", JSONObject.class);
    }

    @Then("^the response status code should be (\\d+)$")
    public void the_response_status_code_should_be(int arg1) throws Throwable {
        assertThat(statusCode).isEqualTo(200);
    }

    @Then("^the \"([^\"]*)\" property should be an array$")
    public void the_property_should_be_an_array(String arg1) throws Throwable {
        throw new PendingException();
    }

    @Then("^the \"([^\"]*)\" property should contain (\\d+) items$")
    public void the_property_should_contain_items(String arg1, int arg2) throws Throwable {
        throw new PendingException();
    }

    @Then("^the \"([^\"]*)\" property should equal \"([^\"]*)\"$")
    public void the_property_should_equal(String arg1, String arg2) throws Throwable {
        throw new PendingException();
    }
}
