import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class PostCreateOrderTest {
    Order order;
    private OrderHelper orderHelper;
    private String accessToken;
    Random random;
    User user;
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
        System.out.println(accessToken);
        //refreshToken = response.then().extract().path("refreshToken").toString().trim();
    }

    @Step
    public Order newOrder() {
        var order = new Order();
        order.addIngredient(INGREDIENT1);
        order.addIngredient(INGREDIENT2);
        return order;
    }

    @Step
    public Order newWrongOrder() {
        order = new Order();
        order.addIngredient(INGREDIENT1);
        order.addIngredient(WRONG_INGREDIENT);
        return order;
    }

    @Test
    public void createOrderTest() {
        order = newOrder();
        Response response = orderHelper.postCreateOrderToken(accessToken, order);
        response.then().log().all().assertThat().statusCode(SC_OK);
    }

    @Test
    public void createOrderWithoutAuthTest() {
        order = newOrder();
        Response response = OrderHelper.postCreateOrder(order);
        response.then().assertThat().statusCode(SC_OK);
    }

    @Test
    public void createOrderWithoutIngredientsTest() {
        Order order = new Order();
        Response response = OrderHelper.postCreateOrder(order);
        response.then().assertThat().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
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
