package order;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import user.Helper;
import static io.restassured.RestAssured.given;

public class OrderHelper extends Helper {
    @Step("Создание заказа")
    public static Response postCreateOrder(Order order) {
        RestAssured.baseURI = Helper.BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(Helper.POST_CREATE_ORDER);
    }

    @Step("Создание заказа с авторизацией")
    public static Response postCreateOrderToken(String accessToken, Order order) {
        RestAssured.baseURI = Helper.BASE_URL;
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/json")
                .body(order)
                .log()
                .all()
                .when()
                .post(Helper.POST_CREATE_ORDER);
    }

    @Step("Получение заказов конкретного пользователя")
    public static Response getUsersOrdersToken(String accessToken) {
        RestAssured.baseURI = Helper.BASE_URL;
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/json")
                .when()
                .get(Helper.GET_USER_ORDERS);
    }

    @Step("Получение заказов конкретного пользователя без авторизации")
    public Response getUsersOrders() {
        RestAssured.baseURI = Helper.BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(Helper.GET_USER_ORDERS);
    }
}
