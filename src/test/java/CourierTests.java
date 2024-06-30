import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.CourierApi;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierTests extends CourierApi {
    private UUID courierId;

    public CourierTests() {
        super("https://qa-scooter.praktikum-services.ru");
    }
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    @Step
    public void testCreateCourier() {
        given() .contentType(ContentType.JSON)
                .body("{\"login\":\"ninja\",\"password\":\"1234\",\"firstName\":\"saske\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
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
                .statusCode(404)
                .body("id", equalTo(null));
    }
    @Test
    @Step
    public void testCreateCourierWithoutRequiredFields() {
        Courier courier = new Courier("", "", "");
        Response response = createCourier(courier.getLogin(), courier.getPassword(), courier.getFirstName());
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

        if (response.jsonPath().getString("id")!= null) {
            courierId = UUID.fromString(response.jsonPath().getString("id"));
        }
    }

    @Test
    @Step
    public void testCreateDuplicateCourier() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"ninja\",\"password\":\"1234\",\"firstName\":\"saske\"}")
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    @Test
    @Step
    public void testLoginWithNonexistentCredentials() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"login\":\"ninja\",\"password\":\"wrongPassword\"}")
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @Step
    public void testDeleteCourierSuccessfully() {
        Response response = deleteCourier("3");
        response.then()
                .statusCode(404);
    }
    @Test
    @Step
    public void testDeleteCourierWithoutId() {
        given()
                .contentType(ContentType.JSON)
                .body("{}") // Отсутствует id
                .when()
                .delete("/api/v1/courier/")
                .then()
                .statusCode(404)
                .body("message", equalTo("Not Found."));
    }
    @Test
    @Step
    public void testDeleteNonexistentCourier() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"id\":\"9999\"}")
                .when()
                .delete("/api/v1/courier/9999")
                .then()
                .statusCode(404)
                .body("message", equalTo("Курьера с таким id нет."));
    }
    @After
    public void tearDown() {
        if (courierId!= null) {
            try {
                deleteCourier(courierId.toString());
            } catch (Exception e) {
                System.out.println("Ошибка при удалении курьера: " + e.getMessage());
            } finally {
                courierId = null;
            }
        }
    }
}