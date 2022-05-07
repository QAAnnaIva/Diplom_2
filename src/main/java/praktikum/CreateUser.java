package praktikum;

import java.util.Random;

public class CreateUser {

    private String email;
    private String password;
    private String name;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public CreateUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // public static final CreateUser CREATE_USER = new CreateUser(getRandomString() + "@mail.ru", getRandomString(), getRandomString());
    public static final CreateUser CREATE_TEST_USER = new CreateUser("testUser2001@mail.ru", "5682pa3", "Анна Иванова");
    public static final CreateUser USER_EXISTS = new CreateUser("piglet@mail.ru", "12345678", "Анна");
    public static final CreateUser WITHOUTREQUIREDFIELD = new CreateUser("", "12345678", "Анна");
    public static final CreateUser WITHOUTPASSWORD = new CreateUser("somemail@mail.ru", "", "Анна");
    public static final CreateUser WITHOUTNAME = new CreateUser("somemail@mail.ru", "12345678", "");

   /* public static String getRandomString() {
        String RANDOM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder random = new StringBuilder();
        Random rnd = new Random();
        while (random.length() < 8) {
            int index = (int) (rnd.nextFloat() * RANDOM.length());
            random.append(RANDOM.charAt(index));
        }
        String randomStr = random.toString();
        return randomStr;

    }*/
}
