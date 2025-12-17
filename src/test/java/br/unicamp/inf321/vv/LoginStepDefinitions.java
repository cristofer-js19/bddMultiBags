package br.unicamp.inf321.vv;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoginStepDefinitions {

    private final CucumberEngine cucumberEngine;

    public LoginStepDefinitions(CucumberEngine cucumberEngine) {
        this.cucumberEngine = cucumberEngine;
    }

    boolean isJWT(String jwt) {
        String[] jwtSplit = jwt.split("\\.");

        if (jwtSplit.length != 3)
            return false;
        try {
            String jsonFirstPart = new String(Base64.getDecoder().decode(jwtSplit[0]));
            JSONObject firstPart = new JSONObject(jsonFirstPart);

            if (!firstPart.has("alg")) {
                return false;
            }
        } catch (JSONException err){
            return false;
        }
        return true;
    }

    @Given("^(?:[A-Za-z\\s]+) is registered on the multibags online store$")
    public void isRegisteredOnTheMultibagsOnlineStore() {
        this.cucumberEngine.setRequest(given().log().all().baseUri("http://multibags.1dt.com.br")
                .contentType(ContentType.JSON.toString())
                .accept(ContentType.JSON.toString())
        );
    }

    @Given("^(?:[A-Za-z\\s]+) is logged in the multibags online store$")
    public void isLoggedInTheMultibagsOnlineStore(Map<String, String> table) {
        cucumberEngine.addToNotes("email", table.get("email"));
        cucumberEngine.addToNotes("password", table.get("password"));
        isRegisteredOnTheMultibagsOnlineStore();
        doesLogInWithValidCredentials(table);
        shouldBeLoggedSuccessfully();
    }

    @When("he logs in with his credentials")
    public void doesLogInWithValidCredentials(Map<String, String> table) {
        String username = table.get("email");
        String password = table.get("password");
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);
        cucumberEngine.setResponse(cucumberEngine.getRequest()
                .body(requestBody.toString())
                .when().post("/api/v1/customer/login"));
    }

    @Then("he should be logged in successfully")
    public void shouldBeLoggedSuccessfully() {
        String token = cucumberEngine.getResponse().then().log().all().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchemaInClasspath("br/unicamp/inf321/vv/LoginSchema.json"))
                .onFailMessage("token should not be empty")
                .body("token", not(blankOrNullString()))
                .extract().body().jsonPath().get("token");
        assertThat("should be a jwt token", isJWT(token), is(true));

        cucumberEngine.addToNotes("token", token);
        int customerId = cucumberEngine.getResponse().then().log().all()
                .body("id", not(blankOrNullString()))
                .extract().body().jsonPath().get("id");
        cucumberEngine.addToNotes("customerId", customerId);
    }
}
