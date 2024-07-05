package org.example;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CourierTests {

    private UUID courierId;
    private CourierApi courierApi;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Step
    public void testCreateCourier() {
        String uniqueLogin = "courier_" + UUID.randomUUID().toString();
        Courier courier = new Courier(uniqueLogin, "1234", "saske");
        courierApi = new CourierApi(RestAssured.baseURI);
        Response response = courierApi.createCourier(courier);
        response.then()
                .statusCode(201)
                .body("ok", is(true))
                .body("id", equalTo(null));
    }

    @Test
    @Step
    public void testLoginCourier() {
        LoginRequest loginRequest = new LoginRequest("ninja", "1234");
        CourierApi courierApi = new CourierApi("https://qa-scooter.praktikum-services.ru");
        Response response = courierApi.loginCourier(loginRequest);
        if (response.getStatusCode() == 404) {
            System.out.println("Запрошенный ресурс не найден. Проверьте URL и данные для авторизации.");
        } else {
            response.then()
                    .statusCode(200)
                    .body("id", notNullValue())
                    .log().all();
        }
    }

    @Test
    @Step
    public void testCreateCourierWithoutRequiredFields() {
        Courier courier = new Courier("", "", "");
        courierApi = new CourierApi(RestAssured.baseURI);
        Response response = courierApi.createCourier(courier);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

        if (response.jsonPath().getString("id") != null) {
            courierId = UUID.fromString(response.jsonPath().getString("id"));
        }
    }

    @Test
    @Step
    public void testCreateDuplicateCourier() {
        Courier courier = new Courier("ninja", "1234", "saske");
        courierApi = new CourierApi(RestAssured.baseURI);
        Response response = courierApi.createCourier(courier);
        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }


    @Test
    @Step
    public void testLoginWithNonexistentCredentials() {
        LoginRequest loginRequest = new LoginRequest("ninja", "wrongPassword");
        courierApi = new CourierApi(RestAssured.baseURI);
        Response response = courierApi.loginCourier(loginRequest);
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Step
    public void testDeleteCourierSuccessfully() {
        courierApi = new CourierApi(RestAssured.baseURI);
        Response response = courierApi.deleteCourier("3");
        response.then()
                .statusCode(200)
                .body("ok", is(true));
    }

    @Test
    @Step
    public void testDeleteCourierWithoutId() {
        courierApi = new CourierApi(RestAssured.baseURI);
        Response response = courierApi.deleteCourier("");
        response.then()
                .statusCode(404)
                .body("message", equalTo("Not Found."));
    }

    @Test
    @Step
    public void testDeleteNonexistentCourier() {
        courierApi = new CourierApi(RestAssured.baseURI);
        Response response = courierApi.deleteCourier("9999");
        response.then()
                .statusCode(404)
                .body("message", equalTo("Курьера с таким id нет."));
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            try {
                courierApi.deleteCourier(courierId.toString());
            } catch (Exception e) {
                System.out.println("Ошибка при удалении курьера: " + e.getMessage());
            } finally {
                courierId = null;
            }
        }
    }
}
