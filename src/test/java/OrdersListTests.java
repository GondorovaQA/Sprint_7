import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.OrderApi;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
public class OrdersListTests {

    private static OrderApi orderApi;

    @BeforeClass
    public static void setup() {
        orderApi = new OrderApi();
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void testGetAllOrders() {
        Response response = orderApi.getAllOrders();
        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders[0].id", notNullValue());
    }

    @Test
    @Step
    public void testGetOrdersByCourierId() {
        Response response = get("/api/v1/orders?courierId=1");
        response.then()
                .statusCode(404);

        Assert.assertNull(response.jsonPath().getString("orders"));
        if (response.jsonPath().getString("orders")!= null) {
            Assert.assertNull(response.jsonPath().getString("orders[0].courierId"));
        } else {
            System.out.println("Список заказов равен null.");
        }
    }

    @Test
    @Step
    public void testGetOrdersByNearestStation() {
        Response response = get("/api/v1/orders?nearestStation=[\"1\", \"2\"]");
        response.then()
                .statusCode(200)
                .body("availableStations", notNullValue())
                .body("availableStations[0].number", is("1"));
    }

    @Test
    public void testGetOrdersWithLimitAndPage() {
        Response response = get("/api/v1/orders?page=0&limit=10");
        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", is(10));
    }

}

