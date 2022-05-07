package praktikum.test;

import api.client.AuthClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static praktikum.LoginUser.USER_DOES_NOT_EXIST;
import static praktikum.LoginUser.USER_EXISTS;

public class LoginUserTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Login existed user")
    public void loginUser() {

        AuthClient authClient = new AuthClient();
        Response loginUserResponse = authClient.login(USER_EXISTS);
        loginUserResponse.then().statusCode(HTTP_OK).body("success", equalTo(true));

    }


    @Test
    @DisplayName("User doesn't exist")
    public void loginUserDoesNotExist() {

        AuthClient authClient = new AuthClient();
        Response userDoesNotExistResponse = authClient.login(USER_DOES_NOT_EXIST);
        userDoesNotExistResponse.then().statusCode(HTTP_UNAUTHORIZED).body("success", equalTo(false)).body("message", equalTo("email or password are incorrect"));

    }


}
