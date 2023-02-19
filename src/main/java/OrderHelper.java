import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderHelper extends Helper {
    public static Response postCreateOrder(Order order) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(POST_CREATE_ORDER);
    }

    public static Response postCreateOrderToken(String accessToken, Order order) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/json")
                .body(order)
                .log()
                .all()
                .when()
                .post(POST_CREATE_ORDER);
    }

    public static Response getUserOrders(String accessToken) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get(GET_USER_ORDERS);
    }

    public boolean getUsersOrdersToken(String accessToken) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/json")
                .when()
                .get(GET_USER_ORDERS)
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("success");
    }

    public String getUsersOrders() {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(GET_USER_ORDERS)
                .then().log().all()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");
    }
}
