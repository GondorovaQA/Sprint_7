import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.example.ColorRequest;
import org.example.OrderApi;
import org.example.OrderResponse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class OrderCreationTests {

    private final List<String> colors;
    private final boolean expectTrackInResponse;
    private OrderApi orderApi;

    @Parameters(name = "{index}: Создание заказа с цветами ({0}) -> Отслеживание в ответе: {1}")
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

    @Before
    public void setUp() {
        orderApi = new OrderApi();
    }

    @Test
    @Step
    public void testCreateOrderWithColor() {
        ColorRequest request = new ColorRequest(colors);
        OrderResponse expectedResponse = new OrderResponse(expectTrackInResponse ? "some-track-id" : null);
        OrderResponse actualResponse = orderApi.sendRequest("/api/v1/orders", request, OrderResponse.class);
        assertThat(actualResponse.getTrack(), notNullValue());
    }
}


