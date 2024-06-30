import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class OrderCreationTests {

    private final String[] colors;
    private final boolean expectTrackInResponse;

    @Parameters(name = "{index}: Create order with colors ({0}) -> Track in response: {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}, true},
                {new String[]{"GREY"}, true},
                {new String[]{"BLACK", "GREY"}, true},
                {new String[]{}, false}
        });
    }

    public OrderCreationTests(String[] colors, boolean expectTrackInResponse) {
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
        String colorsJson = String.join(",", colors);
        given()
                .contentType(ContentType.JSON)
                .body("{\"color\": \"" + colorsJson + "\"}")
                .when()
                .post("/api/v1/order")
                .then()
                .statusCode(201 )
                .body("track", equalTo(null));
    }
}

