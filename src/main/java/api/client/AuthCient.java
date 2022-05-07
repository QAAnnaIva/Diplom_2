package api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static praktikum.EndPoints.LOGIN;
import static praktikum.EndPoints.REGISTER;

public class AuthCient {

    @Step("Get response for register user")
    public ValidatableResponse register(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(REGISTER)
                .then();
    }

    @Step("Get response for login")
    public ValidatableResponse login(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(LOGIN)
                .then();
    }
}
