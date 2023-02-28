package user;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserHelper extends Helper {
    @Step("Создание пользователя")
    public static Response postCreateUser(User user) {
        RestAssured.baseURI = Helper.BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(Helper.POST_CREATE_USER);
    }

    @Step("Удаление пользователя")
    public static void deleteUser(String accessToken) {
        RestAssured.baseURI = Helper.BASE_URL;
        given()
                .header("Authorization", accessToken)
                .when()
                .delete(Helper.DELETE_USER);
    }

    @Step("Авторизация пользователя")
    public static Response postLoginUser(UserCredentials userCredentials) {
        RestAssured.baseURI = Helper.BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userCredentials)
                .when()
                .post(Helper.POST_LOGIN_USER);
    }

    @Step("Разлогин пользователя")
    public static Response postLogoutUser(String refreshToken) {
        RestAssured.baseURI = Helper.BASE_URL;
        return given()
                .header("Authorization", refreshToken)
                .when()
                .post(Helper.POST_LOGOUT_USER);
    }

    @Step("Изменение данных пользователя")
    public static Response patchChangeUserData(String accessToken, UserNewData newUserData) {
        RestAssured.baseURI = Helper.BASE_URL;
        return given()
                .header("Authorization", "Bearer " + accessToken)
                .and()
                .body(newUserData)
                .when()
                .patch(Helper.PATCH_CHANGE_USER_DATA);
    }

    @Step("Изменение данных пользователя без авторизации")
    public static Response patchChangeUserData(UserNewData newUserData) {
        RestAssured.baseURI = Helper.BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(newUserData)
                .when()
                .patch(Helper.PATCH_CHANGE_USER_DATA);
    }

    @Step("Получение информации о пользователе")
    public static Response getUserInfo(String accessToken) {
        RestAssured.baseURI = Helper.BASE_URL;
        return given()
                .header("Authorization", "bearer " + accessToken)
                .when()
                .delete(Helper.GET_USER_INFO);
    }
}
