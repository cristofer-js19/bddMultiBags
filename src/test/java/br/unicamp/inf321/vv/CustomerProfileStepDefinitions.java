package br.unicamp.inf321.vv;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class CustomerProfileStepDefinitions {

    private final CucumberEngine cucumberEngine;

    public CustomerProfileStepDefinitions(CucumberEngine cucumberEngine) {
        this.cucumberEngine = cucumberEngine;
    }

    @When("he selects the option to check his profile")
    public void heSelectsTheOptionToCheckHisProfile() {
        String token = cucumberEngine.getFromNotes("token");
        cucumberEngine.setResponse(cucumberEngine.getRequest()
                .when().header("Authorization", "Bearer " + token)
                .get("/api/v1/auth/customers/profile"));
    }

    @Then("his personal information should be shown successfully")
    public void hisPersonalInformationShouldBeShownSuccessfully() {
        String email = cucumberEngine.getFromNotes("email");
        String customerId = cucumberEngine.getResponse().then().log().all().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchemaInClasspath("br/unicamp/inf321/vv/CustomerProfileSchema.json"))
                .onFailMessage("id should be a valid number")
                .body("id", is(greaterThan(0)))
                .body("emailAddress", is(equalTo(email)))
                .extract().jsonPath().getString("id");
        cucumberEngine.addToNotes("customerId", customerId);
    }
}
