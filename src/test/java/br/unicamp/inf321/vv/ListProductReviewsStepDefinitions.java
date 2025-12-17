package br.unicamp.inf321.vv;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpStatus;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ListProductReviewsStepDefinitions {
    String listProductReviewsUrl = "/api/v1/products/{id}/reviews";
    private final CucumberEngine cucumberEngine;

    public ListProductReviewsStepDefinitions(CucumberEngine cucumberEngine) {
        this.cucumberEngine = cucumberEngine;
    }

    @When("he selects the option to visualize reviews of the product {string}")
    public void heSelectsTheOptionToVisualizeProductReviews(String productId) {
        String requestUrl = listProductReviewsUrl.replace("{id}", productId);
        String token = cucumberEngine.getFromNotes("token");
        cucumberEngine.setResponse(cucumberEngine.getRequest()
                .when().header("Authorization", "Bearer " + token)
                .get(requestUrl));
    }

    @Then("the product reviews should be shown successfully")
    public void shouldShowProductReviewsListSuccessfully() {
        cucumberEngine.getResponse().then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchemaInClasspath("br/unicamp/inf321/vv/ListProductReviewsSchema.json"));
    }

    @Then("the product reviews should not be shown successfully")
    public void shouldNotShowProductReviewsList() {
        cucumberEngine.getResponse().then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(matchesJsonSchemaInClasspath("br/unicamp/inf321/vv/ErrorSchema.json"))
                .body("error", is(equalTo("Not Found")))
                .body("message", is(equalTo("Product id 1000 does not exists")));
    }
}
