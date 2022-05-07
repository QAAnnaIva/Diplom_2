package praktikum.test;

import api.client.AuthClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.CreateUser;
import praktikum.UserAuthorization;

import java.util.Locale;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static praktikum.CreateUser.*;

public class CreateUserTest {


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Create a new user")
    public void createNewUser() {

        AuthClient authClient = new AuthClient();
        Response createNewUserResponse = authClient.register(CreateUser.CREATE_TEST_USER);
        createNewUserResponse.then().statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(CREATE_TEST_USER.getEmail().toLowerCase(Locale.ROOT)))
                .body("user.name", equalTo("Анна Иванова"))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());

    }

    //создание пользователя, который уже зарегистрирован
    @Test
    @DisplayName("UserAlreadyExists")
    public void userAlreadyExists() {
        AuthClient authClient = new AuthClient();
        Response createNewUserResponse = authClient.register(CreateUser.CREATE_TEST_USER);
        createNewUserResponse.then().statusCode(HTTP_OK)
                .body("success", equalTo(true));

        Response userAlreadyExists = authClient.register(CreateUser.USER_EXISTS);
        userAlreadyExists.then().statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("success", equalTo(false)).body("message", equalTo("User already exists"));

    }

    //не заполнено одно из обязательных полей - email
    @Test
    @DisplayName("UserWithoutEmail")
    public void userWithoutEmail() {

        AuthClient authClient = new AuthClient();
        Response createNewUserResponse = authClient.register(CreateUser.CREATE_TEST_USER);
        createNewUserResponse.then().statusCode(HTTP_OK)
                .body("success", equalTo(true));

        Response withoutEmail = authClient.register(CreateUser.WITHOUTREQUIREDFIELD);
        withoutEmail.then().statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));

    }

    //не заполнено одно из обязательных полей - password
    @Test
    @DisplayName("UserWithoutPassword")
    public void userWithoutPassword() {

        AuthClient authClient = new AuthClient();
        Response createNewUserResponse = authClient.register(CreateUser.CREATE_TEST_USER);
        createNewUserResponse.then().statusCode(HTTP_OK)
                .body("success", equalTo(true));

        Response withoutPassword = authClient.register(WITHOUTPASSWORD);
        withoutPassword.then().statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    //не заполнено одно из обязательных полей - name
    @Test
    @DisplayName("UserWithoutName")
    public void userWithoutName() {

        AuthClient authClient = new AuthClient();
        Response createNewUserResponse = authClient.register(CreateUser.CREATE_TEST_USER);
        createNewUserResponse.then().statusCode(HTTP_OK)
                .body("success", equalTo(true));

        Response withoutName = authClient.register(WITHOUTPASSWORD);
        withoutName.then().statusCode(HTTP_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }


    @After
    public void deleteUser() {

        AuthClient authClient = new AuthClient();
        Response loginResponse = authClient.login(CreateUser.CREATE_TEST_USER);
        loginResponse.then().statusCode(HTTP_OK)
                .body("accessToken", equalTo(loginResponse.getBody().as(UserAuthorization.class).getAccessToken()));
        String str = loginResponse.getBody().as(UserAuthorization.class).getAccessToken().substring(7);
        System.out.println(str);

        Response deleteResponse = authClient.userDelete(CreateUser.CREATE_TEST_USER, str);
        deleteResponse.then().statusCode(HTTP_ACCEPTED)
                .body("success", equalTo(true));
    }


}
