import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.core.IsEqual.equalTo;

public class PostLoginUserTest {
    Random random;
    User user;
    UserCredentials userCredentials;
    String accessToken;

    @Before
    public void setUp() {
        random = new Random();
        user = random.getRandomUser();
        Response response = UserHelper.postCreateUser(user);
        accessToken = response.then().extract().path("accessToken").toString().substring(6).trim();
    }

    @Test
    public void loginUserTest() {
        userCredentials = new UserCredentials(user);
        Response response = UserHelper.postLoginUser(userCredentials);
        response.then().assertThat().statusCode(SC_OK);
    }

    @Test
    public void loginUserWrongCredentialsPasswordTest() {
        String wrongPassword = user.getPassword() + "1515";
        user.setPassword(wrongPassword);
        userCredentials = new UserCredentials(user);
        Response response = UserHelper.postLoginUser(userCredentials);
        response.then().assertThat().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
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
