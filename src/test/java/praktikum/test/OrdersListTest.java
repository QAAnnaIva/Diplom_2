package praktikum.test;

import api.client.AuthClient;
import api.client.OrdersClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.GeneralListOfOrders;
import praktikum.LoginUser;
import praktikum.UserAuthorization;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;


public class OrdersListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    //неавторизованный пользователь
    @Test
    @DisplayName("List of orders without authorization - impossible to see the list")
    public void listOfOrdersUnauthorized() {

        OrdersClient ordersClient = new OrdersClient();
        Response orderListWithoutAuthorization = ordersClient.listOfOrders();
        orderListWithoutAuthorization.then().statusCode(HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

    }

    //авторизованный пользователь
    @Test
    @DisplayName("List of orders with authorization")
    public void listOfOrdersAuthorized() {

        AuthClient authClient = new AuthClient();
        Response loginUserResponse = authClient.login(LoginUser.USER_EXISTS);
        loginUserResponse.then().statusCode(HTTP_OK).body("success", equalTo(true));
        String str = loginUserResponse.getBody().as(UserAuthorization.class).getAccessToken().substring(7);
        System.out.println(str);

        OrdersClient ordersClient = new OrdersClient();
        Response orderListWithAuthorization = ordersClient.listOfOrdersAuthorized(str);
        orderListWithAuthorization.then().statusCode(HTTP_OK)
                .body("orders[0]._id", equalTo(orderListWithAuthorization.getBody().as(GeneralListOfOrders.class).getOrders().get(0).id))
                .body("orders[0].status", equalTo(orderListWithAuthorization.getBody().as(GeneralListOfOrders.class).getOrders().get(0).status))
                .body("orders[0].ingredients", equalTo(orderListWithAuthorization.getBody().as(GeneralListOfOrders.class).getOrders().get(0).getIngredients()))
                .body("orders[0].createdAt", equalTo(orderListWithAuthorization.getBody().as(GeneralListOfOrders.class).getOrders().get(0).createdAt))
                .body("orders[0].updatedAt", equalTo(orderListWithAuthorization.getBody().as(GeneralListOfOrders.class).getOrders().get(0).updatedAt))
                .body("orders[0].number", equalTo(9289))
                .body("orders[0].name", equalTo("Флюоресцентный бессмертный бургер"))
                .body("orders[0].ingredients", equalTo(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f")))
                .body("totalToday", equalTo(orderListWithAuthorization.getBody().as(GeneralListOfOrders.class).totalToday))
                .body("total", equalTo(orderListWithAuthorization.getBody().as(GeneralListOfOrders.class).total))
                .body("success", equalTo(true));
        System.out.println(orderListWithAuthorization.getBody().as(GeneralListOfOrders.class).getOrders().get(0).id);
    }

}
