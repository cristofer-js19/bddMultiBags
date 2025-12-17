package br.unicamp.inf321.vv;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ProductReviewStepDefinitions {
    String productReviewUrl = "/api/v1/auth/products/{id}/reviews";
    private final CucumberEngine cucumberEngine;

    public ProductReviewStepDefinitions(CucumberEngine cucumberEngine) {
        this.cucumberEngine = cucumberEngine;
    }

    @When("he selects the option to create a review of the product {string}")
    public void userSelectsTheOptionToCreateProductReviews(String productId, Map<String, String> table) {
        String requestUrl = productReviewUrl.replace("{id}", productId);
        String token = cucumberEngine.getFromNotes("token");
        int customerId = cucumberEngine.getFromNotes("customerId");
        Map<String, Object> body = Map.of(
                "customerId", customerId,
                "description", table.get("description"),
                "language", table.get("language"),
                "rating", table.get("rating")
        );
        cucumberEngine.setResponse(cucumberEngine.getRequest()
                .when().header("Authorization", "Bearer " + token)
                .body(body)
                .post(requestUrl));
    }

    @Then("the product review should be created successfully")
    public void productReviewsShouldBeCreatedSuccessfully() {
        cucumberEngine.getResponse().then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body(matchesJsonSchemaInClasspath("br/unicamp/inf321/vv/ProductReviewSchema.json"));
    }

    @Then("the product review creation should return an error")
    public void productReviewsCreationShouldReturnAnErrorWhenProductHasBeenAlreadyReviewed() {
        cucumberEngine.getResponse().then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(matchesJsonSchemaInClasspath("br/unicamp/inf321/vv/ErrorSchema.json"))
                .body("message", is(equalTo("You have evaluated this product")))
                .body("error", is(equalTo("Bad Request")));
    }

    @When("he selects the option to update a review of the product {string}")
    public void userSelectsTheOptionToUpdateSomeProductReview(String productId, Map<String, String> table) {
        int reviewId = cucumberEngine.getFromNotes("reviewId");
        String requestUrl = productReviewUrl.replace("{id}", productId) + "/" + reviewId;
        String token = cucumberEngine.getFromNotes("token");
        Map<String, Object> body = Map.of(
                "customerId", cucumberEngine.getFromNotes("customerId"),
                "description", table.get("description"),
                "language", table.get("language"),
                "rating", table.get("rating")
        );
        cucumberEngine.setResponse(cucumberEngine.getRequest()
                .when().header("Authorization", "Bearer " + token)
                .body(body)
                .put(requestUrl));
    }

    @Then("the product review should be updated successfully")
    public void productReviewsShouldBeUpdatedSuccessfully() {
        cucumberEngine.getResponse().then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchemaInClasspath("br/unicamp/inf321/vv/ProductReviewSchema.json"));
    }

    @When("gets the reviewId")
    public void userGetsTheReviewId() {
        int customerId = cucumberEngine.getFromNotes("customerId");
        JsonPath response = cucumberEngine.getResponse().jsonPath();

        response.getList("$", Map.class).forEach(i -> {
            Map<String, Object> customer = (Map<String, Object>) i.get("customer");

            if (customerId == (int) customer.get("id")) {
                int reviewId = (int) i.get("id");
                cucumberEngine.addToNotes("reviewId", reviewId);
            }
        });
    }

    @When("he selects the option to delete a review of the product {string}")
    public void userSelectsTheOptionToDeleteProductReviews(String productId) {
        int reviewId = cucumberEngine.getFromNotes("reviewId");
        String requestUrl = productReviewUrl.replace("{id}", productId) + "/" + reviewId;
        String token = cucumberEngine.getFromNotes("token");
        cucumberEngine.setResponse(cucumberEngine.getRequest()
                .when().header("Authorization", "Bearer " + token)
                .delete(requestUrl));
    }

    @Then("the product review should be deleted successfully")
    public void productReviewsShouldBeDeletedSuccessfully() {
        cucumberEngine.getResponse().then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @When("he gets an invalid reviewId")
    public void userGetsAnInvalidReviewId() {
        cucumberEngine.addToNotes("reviewId", 1000);
    }

    @Then("the product review deletion should throw an error")
    public void productReviewsDeletionShouldThrowAnError() {
        cucumberEngine.getResponse().then().log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(matchesJsonSchemaInClasspath("br/unicamp/inf321/vv/ErrorSchema.json"))
                .body("message", is(equalTo("Product review with id 1000 does not exist")))
                .body("error", is(equalTo("Not Found")));
    }

}
