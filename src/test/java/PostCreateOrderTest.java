import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Order;
import order.OrderHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.Random;
import user.User;
import user.UserHelper;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class PostCreateOrderTest {
    private Order order;
    private OrderHelper orderHelper;
    private String accessToken;
    private Random random;
    private User user;
    private final String INGREDIENT1 = "61c0c5a71d1f82001bdaaa6d";
    private final String INGREDIENT2 = "61c0c5a71d1f82001bdaaa6f";
    private final String WRONG_INGREDIENT = "61c0c5a71d1f82001bdaaa";

    @Before
    public void setUp() {
        orderHelper = new OrderHelper();
        random = new Random();
        user = random.getRandomUser();
        Response response = UserHelper.postCreateUser(user);
        accessToken = response.then().extract().path("accessToken").toString().substring(6).trim();
    }

    @Step("Создание заказа")
    public Order newOrder() {
        var order = new Order();
        order.addIngredient(INGREDIENT1);
        order.addIngredient(INGREDIENT2);
        return order;
    }

    @Step("Создание заказа с неправильным ингредиентом")
    public Order newWrongOrder() {
        order = new Order();
        order.addIngredient(INGREDIENT1);
        order.addIngredient(WRONG_INGREDIENT);
        return order;
    }

    @Test
    @DisplayName("Создание заказа")
    public void createOrderTest() {
        order = newOrder();
        Response response = orderHelper.postCreateOrderToken(accessToken, order);
        response.then().log().all().assertThat().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthTest() {
        order = newOrder();
        Response response = OrderHelper.postCreateOrder(order);
        response.then().assertThat().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Order order = new Order();
        Response response = OrderHelper.postCreateOrder(order);
        response.then().assertThat().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным ингредиентом")
    public void createOrderWithWrongIngredientIdTest() {
        order = newWrongOrder();
        Response response = OrderHelper.postCreateOrder(order);
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            new UserHelper().deleteUser(accessToken);
        }
    }
}
