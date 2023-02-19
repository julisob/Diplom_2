import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class Random {

    @Step("Random user")
    public static User getRandomUser() {
        Faker faker = new Faker();
        return new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
    }

    @Step("Empty user")
    public static User getEmptyUser() {
        return new User();
    }

    @Step("User without email")
    public static User getWithoutEmailUser() {
        Faker faker = new Faker();
        return new User("", faker.internet().password(), faker.name().firstName());
    }

    @Step("User without password")
    public static User getWithoutPassword() {
        Faker faker = new Faker();
        return new User(faker.internet().emailAddress(), "", faker.name().firstName());
    }

    @Step("User without name")
    public static User getWithoutNameUser() {
        Faker faker = new Faker();
        return new User(faker.internet().emailAddress(), faker.internet().password(), "");
    }
}

