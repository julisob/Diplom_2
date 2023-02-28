import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.Random;
import user.User;
import user.UserCredentials;
import user.UserHelper;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.core.IsEqual.equalTo;

public class PostLoginUserTest {
    Random random;
    private User user;
    private UserCredentials userCredentials;
    private String accessToken;

    @Before
    public void setUp() {
        random = new Random();
        user = random.getRandomUser();
        Response response = UserHelper.postCreateUser(user);
        accessToken = response.then().extract().path("accessToken").toString().substring(6).trim();
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    public void loginUserTest() {
        userCredentials = new UserCredentials(user);
        Response response = UserHelper.postLoginUser(userCredentials);
        response.then().assertThat().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    public void loginUserWrongCredentialsPasswordTest() {
        String wrongPassword = user.getPassword() + "1515";
        user.setPassword(wrongPassword);
        userCredentials = new UserCredentials(user);
        Response response = UserHelper.postLoginUser(userCredentials);
        response.then().assertThat().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным email")
    public void loginUserWrongCredentialsEmailTest() {
        String wrongEmail = "qw2b9" + user.getEmail();
        user.setEmail(wrongEmail);
        userCredentials = new UserCredentials(user);
        Response response = UserHelper.postLoginUser(userCredentials);
        response.then().assertThat().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void cleanUp() {
        UserHelper.deleteUser(accessToken);
    }
}
