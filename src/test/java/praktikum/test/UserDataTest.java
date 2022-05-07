package praktikum.test;

import api.client.AuthClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.LoginUser;
import praktikum.PatchUser;
import praktikum.UserAuthorization;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;


public class UserDataTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    //с авторизацией
    @Test
    @DisplayName("Change user data with authorization")
    public void authorizedUser() {

        AuthClient authClient = new AuthClient();
        Response loginUserResponse = authClient.login(LoginUser.USER_EXISTS);
        loginUserResponse.then().statusCode(HTTP_OK).body("success", equalTo(true));
        String str = loginUserResponse.getBody().as(UserAuthorization.class).getAccessToken().substring(7);
        System.out.println(str);

        Response changeData = authClient.userUpdateAuthorized(PatchUser.USER_PATCH, str);
        changeData.then().statusCode(HTTP_OK).body("success", equalTo(true))
                .body("user.email", equalTo("piglet11@mail.ru"))
                .body("user.name", equalTo("Анна Иванова"));

        Response backToTheOriginData = authClient.userUpdateAuthorized(PatchUser.USER_EXISTS, str);
        backToTheOriginData.then().statusCode(HTTP_OK).body("success", equalTo(true))
                .body("user.email", equalTo("piglet@mail.ru"))
                .body("user.name", equalTo("Анна"));

    }

    //без авторизации
    @Test
    @DisplayName("Change user data without authorization - impossible to change the user data")
    public void unAuthorizedUser() {

        AuthClient authClient = new AuthClient();
        Response loginUserResponse = authClient.login(LoginUser.USER_EXISTS);
        loginUserResponse.then().statusCode(HTTP_OK).body("success", equalTo(true));

        Response unAuthorizedUserResponse = authClient.userUpdate(PatchUser.USER_PATCH);
        unAuthorizedUserResponse.then().statusCode(HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

    }


}
