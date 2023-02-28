import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class PatchChangeUserDataTest {
    private Random random;
    private User user;
    private UserCredentials userCredentials;
    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp() {
        random = new Random();
        user = random.getRandomUser();
        Response response = UserHelper.postCreateUser(user);
        accessToken = response.then().extract().path("accessToken").toString().substring(6).trim();
        refreshToken = response.then().extract().path("refreshToken").toString().trim();
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void changeUserDataTest() {
        userCredentials = new UserCredentials(user);
        Response response = UserHelper.postLoginUser(userCredentials);
        response.then().assertThat().statusCode(SC_OK);
        UserNewData newData = new UserNewData("newemailchange837@gmail.com", "newName");
        response = UserHelper.patchChangeUserData(accessToken, newData);
        response.then().assertThat().statusCode(SC_OK).and().body("success", is(true));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changeUserDataWithoutAuthTest() {
        userCredentials = new UserCredentials(user);
        UserNewData newData = new UserNewData("newemailchange83755@gmail.com", "newName");
        Response response = UserHelper.patchChangeUserData(newData);
        response.then().assertThat().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserHelper.deleteUser(accessToken);
        }
    }
}
