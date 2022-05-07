package api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static praktikum.EndPoints.*;

public class AuthClient {

    @Step("Get response for register user")
    public Response register(Object body) {

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(REGISTER);

    }

    @Step("Get response for login")
    public Response login(Object body) {

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(LOGIN);

    }

    @Step("Get response for updateUser without authorization")
    public Response userUpdate(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .patch(USER);

    }

    @Step("Get response for updateUser with authorization")
    public Response userUpdateAuthorized(Object body, String str) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .auth().oauth2(str)
                .when()
                .patch(USER);

    }

    @Step("Get response for delete user")
    public Response userDelete(Object body, String str) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .auth().oauth2(str)
                .when()
                .delete(DELETE);

    }


}
