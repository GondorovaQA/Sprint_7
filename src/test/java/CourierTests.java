import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierTests {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
         // Замените на порт вашего сервера
    }

    @Test
    @Step
    public void testCreateCourier() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"ninja\",\"password\":\"1234\",\"firstName\":\"saske\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409) // Ожидаемый статус код при успешном создании
                .body("ok", equalTo(null));
    }
    @Test
    @Step
    public void testLoginCourier() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"ninja\",\"password\":\"1234\"}")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404) // Ожидаемый статус код при успешном логине
                .body("id", equalTo(null));
    }

}
