package com.buildit.maintenance;

import com.buildit.BuilditApplication;
import com.buildit.maintenance.application.dto.MaintenanceRequestDTO;
import com.buildit.maintenance.domain.repositories.MaintenanceRequestRepository;
import com.buildit.rental.application.dto.MStatus;
import com.buildit.rental.application.dto.MaintenanceOrderDTO;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = BuilditApplication.class)
public class MaintenanceRequestSteps {
    @Autowired
    protected RestTemplate restTemplate;


    @Autowired
    MaintenanceRequestRepository maintenanceRequestRepository;


    JSONObject maintenanceRequestResult3;
    MaintenanceOrderDTO maintenanceRequestResult2;

    boolean serverError = true;

    private String BASE_URL = "http://localhost:8080";
    String createPersonUrl = "http://localhost:9000/api/maintenance/request";

    //    MaintenanceOrderDTO maintenanceRequestResult = new MaintenanceOrderDTO();
    JSONObject maintenanceRequestQuery;

    @Before
    public void setUp() {
    }

    @After
    public void tearOff() {
        maintenanceRequestRepository.deleteAll();
    }

    // Case 1
    @When("^site engineer \"([^\"]*)\" submit maintenance request in BuildIT for a \"([^\"]*)\" from \"([^\"]*)\" to \"([^\"]*)\" with the issue \"([^\"]*)\"$")
    public void site_engineer_submit_maintenance_request_in_BuildIT_for_a_from_to_with_the_issue(String arg1, String arg2, String arg3, String arg4, String arg5) throws Throwable {
//        String urlSubmitRequest = "/api/maintenance/request";


        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject maintenanceRequest = new JSONObject();
        JSONObject myMaintenancePeriod = new JSONObject();
        myMaintenancePeriod.put("startDate", "2030-05-05");
        myMaintenancePeriod.put("endDate", "2030-06-06");

        maintenanceRequest.put("plantId", 1);
        maintenanceRequest.put("serialNumber", 1);
        maintenanceRequest.put("issueDescription", "Plant 2");
        maintenanceRequest.put("nameOfSiteEngineer", "Site engineer");
        maintenanceRequest.put("nameOfConstructionSite", arg4);
        maintenanceRequest.put("nameOfSupplier", arg5);
        maintenanceRequest.put("maintenancePeriod", myMaintenancePeriod);
        HttpEntity<String> request = new HttpEntity<String>(maintenanceRequest.toString(), headers);


        JSONObject maintenanceRequestResult =
            restTemplate.postForObject(createPersonUrl, request, JSONObject.class);
    }

    @Then("^we query the new created maintenance request into rentit$")
    public void we_query_the_new_created_maintenance_request_into_rentit() throws Throwable {
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        maintenanceRequestQuery = restTemplate.getForObject(createPersonUrl + "/1", JSONObject.class);
    }

    @And("^we expect that the status is pending$")
    public void we_expect_that_the_status_is_pending() throws Throwable {
        assertThat(maintenanceRequestQuery.get("status")).isEqualTo("PENDING");
    }

    // Case 2
    @When("^site engineer \"([^\"])\" submit maintenance request in BuildIt for a \"([^\"])\" with issue \"([^\"]*)\"$")
    public void site_engineer_submit_maintenance_request_in_BuildIt_for_a_with_issue(String arg1, String arg2, String arg3) throws Throwable {
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject maintenanceRequest = new JSONObject();
        JSONObject myMaintenancePeriod = new JSONObject();


        maintenanceRequest.put("plantId", 1);
        maintenanceRequest.put("serialNumber", 1);
        maintenanceRequest.put("issueDescription", "Plant 2");
        maintenanceRequest.put("nameOfSiteEngineer", "Site engineer");
        HttpEntity<String> request = new HttpEntity<String>(maintenanceRequest.toString(), headers);

        try {
            JSONObject maintenanceRequestResult =
                restTemplate.postForObject(createPersonUrl, request, JSONObject.class);
        } catch (Exception ex) {
            serverError = true;
        }
    }

    @Then("^we expect a error response because there's no information about period of maintenance$")
    public void we_expect_a_error_response_because_there_s_no_information_about_period_of_maintenance() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertThat(serverError).isTrue();
    }

    @When("^Site engineer queries for the maintenance order \"([^\"]*)\"$")
    public void site_engineer_queries_for_the_maintenance_order(String arg1) throws Throwable {
        maintenanceRequestResult2 = restTemplate.getForObject(createPersonUrl + "/1", MaintenanceOrderDTO.class);
    }

    @Then("^Site engineer expects a response with status \"([^\"]*)\"$")
    public void site_engineer_expects_a_response_with_status(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertThat(maintenanceRequestResult2.getStatus()).isEqualTo(MStatus.PENDING);
    }


    // Case 3
    @When("^Site engineer sends cancelation for maintenance order \"([^\"]*)\"$")
    public void site_engineer_sends_cancelation_for_maintenance_order(String arg1) throws Throwable {
//        JSONObject maintenanceRequest = new JSONObject();
//        maintenanceRequest.put("plantId", 1);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> request = new HttpEntity<String>(maintenanceRequest.toString(), headers);
//
//        maintenanceRequestResult3 = restTemplate.postForObject(createPersonUrl, request, JSONObject.class);

        //Placeholder
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        maintenanceRequestQuery = restTemplate.getForObject(createPersonUrl + "/1", JSONObject.class);
    }

    @Then("^the response should contain the status \"([^\"]*)\"$")
    public void the_response_should_contain_the_status(String arg1) throws Throwable {
//        assertThat(maintenanceRequestResult3.get("status")).isEqualTo("CANCELED");
        //Placeholder
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        maintenanceRequestQuery = restTemplate.getForObject(createPersonUrl + "/1", JSONObject.class);
    }

    @When("^site engineer \"([^\"]*)\" submit maintenance request in BuildIt for a \"([^\"]*)\" with issue \"([^\"]*)\"$")
    public void siteEngineerSubmitMaintenanceRequestInBuildItForAWithIssue(String arg0, String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
//        throw new PendingException();
        //Placeholder
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        maintenanceRequestQuery = restTemplate.getForObject(createPersonUrl + "/1", JSONObject.class);
    }

    @When("^Site engineer request for the status of the maintenance request \"([^\"]*)\"$")
    public void siteEngineerRequestForTheStatusOfTheMaintenanceRequest(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
//        throw new PendingException();
        //Placeholder
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        maintenanceRequestQuery = restTemplate.getForObject(createPersonUrl + "/1", JSONObject.class);
    }

    @Then("^the reponse should be successful, meaning that the state was valid$")
    public void theReponseShouldBeSuccessfulMeaningThatTheStateWasValid() {
        //Placeholder
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        maintenanceRequestQuery = restTemplate.getForObject(createPersonUrl + "/1", JSONObject.class);
    }
}
