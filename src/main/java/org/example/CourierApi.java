package org.example;

import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CourierApi {

    private String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    public CourierApi(String baseUri) {
        this.BASE_URI = baseUri;
        this.requestSpec = given().baseUri(this.BASE_URI);
    }

    public Response createCourier(Courier courier) {
        String json = new Gson().toJson(courier);
        return given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    private RequestSpecification requestSpec;

    // Конструктор, принимающий базовый URL


    // Метод для выполнения POST-запроса на вход в систему
    public Response loginCourier(LoginRequest loginRequest) {
        return given(requestSpec)
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().response();
    }

    public Response deleteCourier(String id) {
        return given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body("{\"id\":\"" + id + "\"}")
                .when()
                .delete("/api/v1/courier/" + id);
    }

}


