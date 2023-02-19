import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

public class GetUserOrdersTest {
    String accessToken;
    OrderHelper orderHelper = new OrderHelper();

    @Test
    public void getOrdersAuthorized() {
        Random random = new Random();
        User user = random.getRandomUser();
        Response response = UserHelper.postCreateUser(user);
        accessToken = response.then().extract().path("accessToken").toString().substring(6).trim();
        boolean success = orderHelper.getUsersOrdersToken(accessToken);
        Assert.assertTrue(success);
        UserHelper.deleteUser(accessToken);
    }

    @Test
    public void getOrdersNotAuthorized() {
        String expected = orderHelper.getUsersOrders();
        Assert.assertEquals("You should be authorised", expected);
    }
}
