import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.Random;
import user.User;
import user.UserHelper;
import static org.apache.http.HttpStatus.*;

public class PostCreateUserTest {
    Random random;
    private String accessToken;

    @Before
    public void setUp() {
        random = new Random();
    }

    @Test
    @DisplayName("Создание пользователя")
    public void createUserTest() {
        User user = random.getRandomUser();
        Response response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_OK);
        accessToken = response.then().extract().path("accessToken").toString().substring(6).trim();
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createDoubleUserTest() {
        User user = random.getRandomUser();
        Response response = UserHelper.postCreateUser(user);
        accessToken = response.then().extract().path("accessToken").toString().substring(6).trim();
        response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя с пустым email")
    public void createUserWithoutEmailTest() {
        User user = random.getWithoutEmailUser();
        Response response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameTest() {
        User user = random.getWithoutNameUser();
        Response response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordTest() {
        User user = random.getWithoutPassword();
        Response response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без данных")
    public void createUserWithoutDataTest() {
        User user = random.getEmptyUser();
        Response response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserHelper.deleteUser(accessToken);
        }
    }
}
