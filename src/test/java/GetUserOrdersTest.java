import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.OrderHelper;
import org.junit.After;
import org.junit.Test;
import user.Random;
import user.User;
import user.UserHelper;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class GetUserOrdersTest {
    private String accessToken;
    private final OrderHelper orderHelper = new OrderHelper();

    @Test
    @DisplayName("Получение заказов пользователя с авторизацией")
    public void getOrdersAuthorized() {
        Random random = new Random();
        User user = random.getRandomUser();
        Response response = UserHelper.postCreateUser(user);
        accessToken = response.then().extract().path("accessToken").toString().substring(6).trim();
        response = orderHelper.getUsersOrdersToken(accessToken);
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    public void getOrdersNotAuthorized() {
        Response response = orderHelper.getUsersOrders();
        response.then()
                .assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserHelper.deleteUser(accessToken);
        }
    }
}
