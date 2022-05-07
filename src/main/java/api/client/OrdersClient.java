package api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static praktikum.EndPoints.*;

public class OrdersClient {

    @Step("Get response for order creation without authorization")
    public Response createANewOrder(Object body) {

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(ORDERS);
    }

    @Step("Get response for order creation with authorization")
    public Response createANewOrderWithAuthorization(Object body, String str) {

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .auth().oauth2(str)
                .when()
                .post(ORDERS);
    }

    @Step("Get response for list of orders, unauthorized")
    public Response listOfOrders() {

        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(ORDERS);
    }

    @Step("Get response for list of orders, authorized")
    public Response listOfOrdersAuthorized(String str) {

        return given()
                .header("Content-type", "application/json")
                .and()
                .auth().oauth2(str)
                .when()
                .get(ORDERS);
    }


}
