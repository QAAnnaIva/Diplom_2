package praktikum;

public class LoginUser {

    private String email;
    private String password;


    public LoginUser(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public static final LoginUser USER_EXISTS = new LoginUser("piglet@mail.ru","12345678");
    public static final LoginUser USER_DOES_NOT_EXIST = new LoginUser("new1user2@mail.ru","password345");
}
