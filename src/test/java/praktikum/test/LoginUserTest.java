package praktikum.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.UserAuthorization;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static praktikum.EndPoints.LOGIN;
import static praktikum.LoginUser.USER_DOES_NOT_EXIST;
import static praktikum.LoginUser.USER_EXISTS;

public class LoginUserTest {

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

//логин под существующим пользователем
    @Test
    public void loginUser(){

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(USER_EXISTS)
                        .when()
                        .post(LOGIN);
        response.then().statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo("piglet@mail.ru"))
                .body("user.name",equalTo("Анна"))
                .body("accessToken",equalTo(response.getBody().as(UserAuthorization.class).getAccessToken()))
                .body("refreshToken",equalTo(response.getBody().as(UserAuthorization.class).getRefreshToken()));

    }

//логин с неверным логином и паролем
    @Test
    public void loginUserDoesNotExist(){

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(USER_DOES_NOT_EXIST)
                        .when()
                        .post(LOGIN);
        response.then().statusCode(HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

    }


}
