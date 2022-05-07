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
import static praktikum.EndPoints.USER;
import static praktikum.PatchUser.USER_EXISTS;
import static praktikum.PatchUser.USER_PATCH;

public class UserDataTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }


    //с авторизацией
    @Test
    public void authorizedUser() {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(USER_EXISTS)
                        .when()
                        .post(LOGIN);

        response.then().body("accessToken", equalTo(response.getBody().as(UserAuthorization.class).getAccessToken()));
        String str = response.getBody().as(UserAuthorization.class).getAccessToken().substring(7);
        System.out.println(str);


        Response changeData =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(USER_PATCH)
                        .when()
                        .auth().oauth2(str)
                        .patch(USER);
        changeData.then().statusCode(HTTP_OK)
                .body("user.email", equalTo("piglet11@mail.ru"))
                .body("user.name", equalTo("Анна Иванова"));

        Response backToTheOriginData =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(USER_EXISTS)
                        .when()
                        .auth().oauth2(str)
                        .patch(USER);
        backToTheOriginData.then().statusCode(HTTP_OK)
                .body("user.email", equalTo("piglet@mail.ru"))
                .body("user.name", equalTo("Анна"));


    }


    //без авторизации
    @Test
    public void unAuthorizedUser() {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(USER_EXISTS)
                        .when()
                        .post(LOGIN);

        response.then().body("accessToken", equalTo(response.getBody().as(UserAuthorization.class).getAccessToken()));

        Response unauthorized =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(USER_PATCH)
                        .when()
                        .patch(USER);
        unauthorized.then().statusCode(HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

    }


}
