package br.unicamp.inf321.vv;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CucumberEngine {
    private Response response;
    private RequestSpecification request;

    private final Map<String, Object> notes = new HashMap<>();

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return this.response;
    }

    public void setRequest(RequestSpecification request) {
        this.request = request;
    }

    public RequestSpecification getRequest() {
        return given().spec(this.request);
    }

    public <T> void addToNotes(String key, T value) {
        this.notes.put(key, value);
    }

    public <T> T getFromNotes(String key) {
        return (T) this.notes.get(key);
    }
}
