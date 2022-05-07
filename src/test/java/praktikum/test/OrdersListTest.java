package praktikum.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.GeneralListOfOrders;
import praktikum.UserAuthorization;

import java.util.List;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static praktikum.EndPoints.*;
import static praktikum.PatchUser.USER_EXISTS;


public class OrdersListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    //неавторизованный пользователь
    @Test
    public void listOfOrdersUnauthorized() {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .when()
                        .get(ORDERS);

        response.then().statusCode(HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

    }

    //авторизованный пользователь
    @Test
    public void listOfOrdersAuthorized() {


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


        Response listOfOrders =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .auth().oauth2(str)
                        .get(ORDERS);
        listOfOrders.then().statusCode(HTTP_OK)

                .body("orders[56].id", equalTo(listOfOrders.getBody().as(GeneralListOfOrders.class).getOrders().get(56).id))
                .body("orders[56].status", equalTo(listOfOrders.getBody().as(GeneralListOfOrders.class).getOrders().get(56).status))
                .body("orders[56].ingredients", equalTo(listOfOrders.getBody().as(GeneralListOfOrders.class).getOrders().get(56).getIngredients()))
                .body("orders[56].createdAt", equalTo(listOfOrders.getBody().as(GeneralListOfOrders.class).getOrders().get(56).createdAt))
                .body("orders[56].updatedAt", equalTo(listOfOrders.getBody().as(GeneralListOfOrders.class).getOrders().get(56).updatedAt))
                .body("orders[56].number", equalTo(8817))
                .body("orders[56].name", equalTo("Био-марсианский бургер"))
                .body("orders[56].ingredients", equalTo(List.of("61c0c5a71d1f82001bdaaa71")))
                .body("totalToday", equalTo(listOfOrders.getBody().as(GeneralListOfOrders.class).totalToday))
                .body("total", equalTo(listOfOrders.getBody().as(GeneralListOfOrders.class).total))
                .body("success", equalTo(true));


    }

}
