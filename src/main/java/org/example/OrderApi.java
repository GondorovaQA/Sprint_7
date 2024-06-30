package org.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderApi {

    private RequestSpecification requestSpec;

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


}

