import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.ColorRequest;
import org.example.OrderResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTests {

    private final List<String> colors;
    private final boolean expectTrackInResponse;

    @Parameters(name = "{index}: Create order with colors ({0}) -> Track in response: {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK"), true},
                {Arrays.asList("GREY"), true},
                {Arrays.asList("BLACK", "GREY"), true},
                {Arrays.asList(), false}
        });
    }

    public OrderCreationTests(List<String> colors, boolean expectTrackInResponse) {
        this.colors = colors;
        this.expectTrackInResponse = expectTrackInResponse;
    }

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Step
    public void testCreateOrderWithColor() {
        ColorRequest request = new ColorRequest(colors);
        OrderResponse expectedResponse = new OrderResponse(expectTrackInResponse ? "some-track-id" : null);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
