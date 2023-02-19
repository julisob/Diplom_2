import io.restassured.response.Response;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;

public class PostCreateUserTest {
    Random random;

    @Before
    public void setUp() {
        random = new Random();
    }

    @Test
    public void createUserTest() {
        User user = random.getRandomUser();
        Response response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_OK);
        String accessToken = response.then().extract().path("accessToken").toString().substring(6).trim();
        UserHelper.deleteUser(accessToken);
    }

    @Test
    public void createDoubleUserTest() {
        User user = random.getRandomUser();
        Response response = UserHelper.postCreateUser(user);
        String accessToken = response.then().extract().path("accessToken").toString().substring(6).trim();
        response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("User already exists"));
        UserHelper.deleteUser(accessToken);
    }

    @Test
    public void createUserWithoutEmailTest() {
        User user = random.getWithoutEmailUser();
        Response response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutNameTest() {
        User user = random.getWithoutNameUser();
        Response response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutPasswordTest() {
        User user = random.getWithoutPassword();
        Response response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutDataTest() {
        User user = random.getEmptyUser();
        Response response = UserHelper.postCreateUser(user);
        response.then().assertThat().statusCode(SC_FORBIDDEN).and().body("message", equalTo("Email, password and name are required fields"));
    }
}
