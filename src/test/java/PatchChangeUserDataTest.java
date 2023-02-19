import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class PatchChangeUserDataTest {
    Random random;
    User user;
    UserCredentials userCredentials;
    String accessToken;
    String refreshToken;

    @Before
    public void setUp() {
        random = new Random();
        user = random.getRandomUser();
        Response response = UserHelper.postCreateUser(user);
        accessToken = response.then().extract().path("accessToken").toString().substring(6).trim();
        refreshToken = response.then().extract().path("refreshToken").toString().trim();
    }

    @Test
    public void changeUserDataTest() {
        userCredentials = new UserCredentials(user);
        Response response = UserHelper.postLoginUser(userCredentials);
        response.then().assertThat().statusCode(SC_OK);
        UserNewData newData = new UserNewData("newemailchange837@gmail.com", "newName");
        response = UserHelper.patchChangeUserData(accessToken, newData);
        response.then().assertThat().statusCode(SC_OK);
    }

    @Test
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
