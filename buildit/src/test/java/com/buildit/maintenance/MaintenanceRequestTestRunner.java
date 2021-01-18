package com.buildit.maintenance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber"},
    features = "classpath:maintenance.request",
    glue = "com.buildit.maintenance")
public class MaintenanceRequestTestRunner {
}
