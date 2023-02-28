package user;

public class UserCredentials {
    public String email;
    public String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserCredentials(User user) {
        this.email = user.email;
        this.password = user.password;
    }
}
