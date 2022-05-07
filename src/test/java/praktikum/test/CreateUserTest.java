package praktikum.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.UserAuthorization;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static praktikum.CreateUser.*;
import static praktikum.EndPoints.*;
import static praktikum.PatchUser.USER_EXISTS;

public class CreateUserTest {



    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    //создание уникального пользователя
    @Test
    public void createNewUser() {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(CREATE_TEST_USER)
                        .when()
                        .post(REGISTER);
        response.then().statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(CREATE_TEST_USER.getEmail().toLowerCase(Locale.ROOT)))
                .body("user.name", equalTo(CREATE_TEST_USER.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
      /*  String password = CREATE_TEST_USER.getPassword();
        String name = CREATE_TEST_USER.getName();
        String email = CREATE_TEST_USER.getEmail();

        System.out.println(CREATE_TEST_USER.getName());
        System.out.println(CREATE_TEST_USER.getPassword());
        System.out.println(CREATE_TEST_USER.getEmail().toLowerCase(Locale.ROOT));*/

    }

    //создание пользователя, который уже зарегистрирован
    @Test
    public void userAlreadyExists() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(CREATE_TEST_USER)
                        .when()
                        .post(REGISTER);
        response.then().statusCode(HTTP_OK)
                .body("success", equalTo(true));


        Response userAlreadyExists =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(USER_EXISTS)
                        .when()
                        .post(REGISTER);
        userAlreadyExists.then().statusCode(HTTP_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("User already exists"));

    }

    //не заполнено одно из обязательных полей - email
    @Test
    public void userWithoutEmail() {


        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(CREATE_TEST_USER)
                        .when()
                        .post(REGISTER);
        response.then().statusCode(HTTP_OK)
                .body("success", equalTo(true));


        Response withoutEmail =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(WITHOUTREQUIREDFIELD)
                        .when()
                        .post(REGISTER);
        withoutEmail.then().statusCode(HTTP_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));

    }

    //не заполнено одно из обязательных полей - password
    @Test
    public void userWithoutPassword() {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(CREATE_TEST_USER)
                        .when()
                        .post(REGISTER);
        response.then().statusCode(HTTP_OK)
                .body("success", equalTo(true));

        Response withoutPassword =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(WITHOUTPASSWORD)
                        .when()
                        .post(REGISTER);
        withoutPassword.then().statusCode(HTTP_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));

    }

    //не заполнено одно из обязательных полей - name
    @Test
    public void userWithoutName() {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(CREATE_TEST_USER)
                        .when()
                        .post(REGISTER);
        response.then().statusCode(HTTP_OK)
                .body("success", equalTo(true));

        Response withoutName =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(WITHOUTNAME)
                        .when()
                        .post(REGISTER);
        withoutName.then().statusCode(HTTP_FORBIDDEN).body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields"));
    }


    @After
    public void deleteUser() {

Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(CREATE_TEST_USER)
                        .when()
                        .post(LOGIN);
        response.then().body("accessToken", equalTo(response.getBody().as(UserAuthorization.class).getAccessToken()));
        String str = response.getBody().as(UserAuthorization.class).getAccessToken().substring(7);
        System.out.println(str);

        Response deleteCreatedUser =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(CREATE_TEST_USER)
                        .when()
                        .auth().oauth2(str)
                        .delete(DELETE);
        deleteCreatedUser.then().statusCode(HTTP_ACCEPTED).body("success", equalTo(true));
    }


}
