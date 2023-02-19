import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserHelper extends Helper {
    public static Response postCreateUser(User user) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(POST_CREATE_USER);
    }

    public static void deleteUser(String accessToken) {
        RestAssured.baseURI = BASE_URL;
        given()
                .header("Authorization", accessToken)
                .when()
                .delete(DELETE_USER);
    }

    public static Response postLoginUser(UserCredentials userCredentials) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userCredentials)
                .when()
                .post(POST_LOGIN_USER);
    }

    public static Response postLogoutUser(String refreshToken) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Authorization", refreshToken)
                .when()
                .post(POST_LOGOUT_USER);
    }

    public static Response patchChangeUserData(String accessToken, UserNewData newUserData) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .and()
                .body(newUserData)
                .when()
                .patch(PATCH_CHANGE_USER_DATA);
    }

    public static Response patchChangeUserData(UserNewData newUserData) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(newUserData)
                .when()
                .patch(PATCH_CHANGE_USER_DATA);
    }

    public static Response getUserInfo(String accessToken) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Authorization", "bearer " + accessToken)
                .when()
                .delete(GET_USER_INFO);
    }
}
