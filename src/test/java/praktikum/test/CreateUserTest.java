package praktikum.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static praktikum.CreateUser.*;
import static praktikum.EndPoints.REGISTER;

public class CreateUserTest {


    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

//создание уникального пользователя
    @Test
    public void createNewUser(){

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(CREATE_USER)
                        .when()
                        .post(REGISTER);
        response.then().statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(CREATE_USER.getEmail().toLowerCase(Locale.ROOT)))
                .body("user.name",equalTo(CREATE_USER.getName()))
                .body("accessToken",notNullValue())
                .body("refreshToken",notNullValue());


    }

//создание пользователя, который уже зарегистрирован
    @Test
    public void userAlreadyExists(){

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(USER_EXISTS)
                        .when()
                        .post(REGISTER);
        response.then().statusCode(HTTP_FORBIDDEN).body("success", equalTo(false)).body("message",equalTo("User already exists"));

    }

//не заполнено одно из обязательных полей - email
    @Test
    public void userWithoutEmail(){

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(WITHOUTREQUIREDFIELD)
                        .when()
                        .post(REGISTER);
        response.then().statusCode(HTTP_FORBIDDEN).body("success", equalTo(false)).body("message",equalTo("Email, password and name are required fields"));

    }

//не заполнено одно из обязательных полей - password
    @Test
    public void userWithoutPassword(){

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(WITHOUTPASSWORD)
                        .when()
                        .post(REGISTER);
        response.then().statusCode(HTTP_FORBIDDEN).body("success", equalTo(false)).body("message",equalTo("Email, password and name are required fields"));

    }

//не заполнено одно из обязательных полей - name
    @Test
    public void userWithoutName(){

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(WITHOUTNAME)
                        .when()
                        .post(REGISTER);
        response.then().statusCode(HTTP_FORBIDDEN).body("success", equalTo(false)).body("message",equalTo("Email, password and name are required fields"));

    }



}
