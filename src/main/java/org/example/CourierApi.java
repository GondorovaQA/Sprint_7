package org.example;


import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CourierApi {
    protected RequestSpecification requestSpec;

    public CourierApi(String baseUri) {
        this.requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .build();
    }
@Step
public Response createCourier(String login, String password, String firstName) {return given(requestSpec)
            .body("{\"login\":\"" + login + "\",\"password\":\"" + password + "\",\"firstName\":\"" + firstName + "\"}")  .when()
            .post("/api/v1/courier");
    }

    public Response loginCourier(String login, String password) {
        return given(requestSpec)
                .body("{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("/api/v1/courier/login");
    }

    public Response deleteCourier(String id) {
        return given(requestSpec)
                .body("{\"id\":\"" + id + "\"}")
                .when()
                .delete("/api/v1/courier/" + id);
    }

}
