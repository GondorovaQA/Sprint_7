package org.example;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderApi {
    private RequestSpecification requestSpec;
    private Gson gson = new Gson();

    public OrderApi() {
        this.requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://qa-scooter.praktikum-services.ru")
                .setContentType(ContentType.JSON)
                .build();
    }

    public Response getAllOrders() {
        return given(requestSpec)
                .when()
                .get("/api/v1/orders")
                .then()
                .extract().response();
    }

    public <T> T sendRequest(String endpoint, Object body, Class<T> responseType) {
        Response response = given(requestSpec)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(endpoint);
        return responseType.cast(gson.fromJson(response.asString(), responseType));
    }
}

