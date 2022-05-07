package praktikum;

public class PatchUser {

    private String email;
    private String password;
    private String name;

    public PatchUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static final PatchUser USER_PATCH = new PatchUser("piglet11@mail.ru", "123456798", "Анна Иванова");
    public static final PatchUser USER_EXISTS = new PatchUser("piglet@mail.ru", "12345678", "Анна");
}
