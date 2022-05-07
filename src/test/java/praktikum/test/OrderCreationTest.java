package praktikum.test;

import api.client.AuthClient;
import api.client.OrdersClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.*;

import java.util.List;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;


public class OrderCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    //без авторизации, с ингредиентами
    @Test
    @DisplayName("Without authorization, with ingredients")
    public void withoutAuthorizationWithIngredients() {

        OrdersClient ordersClient = new OrdersClient();
        Response orderWithoutAuthorization = ordersClient.createANewOrder(Ingredients.BURGER_INGREDIENTS);
        orderWithoutAuthorization.then().statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("order.number", equalTo(orderWithoutAuthorization.getBody().as(General.class).getOrder().number))
                .body("name", equalTo(orderWithoutAuthorization.getBody().as(General.class).getName()));

        System.out.println(orderWithoutAuthorization.getBody().as(General.class).getOrder().number);
        System.out.println(orderWithoutAuthorization.getBody().as(General.class).getName());

    }

    //без авторизации, без ингредиентов
    @Test
    @DisplayName("Without authorization, without ingredients")
    public void withoutAuthorizationWithoutIngredients() {

        OrdersClient ordersClient = new OrdersClient();
        Response orderWithoutIngredients = ordersClient.createANewOrder(Ingredients.WITHOUT_INGREDIENTS);
        orderWithoutIngredients.then().statusCode(HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    //без авторизации, неверный хеш ингредиентов
    @Test
    public void withoutAuthorizationErrorIngredients() {

        OrdersClient ordersClient = new OrdersClient();
        Response errorIngredients = ordersClient.createANewOrder(Ingredients.ERROR_INGREDIENTS);
        errorIngredients.then().statusCode(HTTP_INTERNAL_ERROR);

    }

    //с авторизацией, с ингредиентами
    @Test
    @DisplayName("With authorization, with ingredients")
    public void withAuthorizationWithIngredients() {

        AuthClient authClient = new AuthClient();
        Response loginUserResponse = authClient.login(LoginUser.USER_EXISTS);
        loginUserResponse.then().statusCode(HTTP_OK).body("success", equalTo(true));
        String str = loginUserResponse.getBody().as(UserAuthorization.class).getAccessToken().substring(7);
        System.out.println(str);

        OrdersClient ordersClient = new OrdersClient();
        Response orderWithAuthorization = ordersClient.createANewOrderWithAuthorization(Ingredients.BURGER_INGREDIENTS, str);
        orderWithAuthorization.then().statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("order.number", equalTo(orderWithAuthorization.getBody().as(General.class).getOrder().number))
                .body("order.price", equalTo(2325))
                .body("order._id", equalTo(orderWithAuthorization.getBody().as(General.class).getOrder().id))
                .body("order.status", equalTo(orderWithAuthorization.getBody().as(General.class).getOrder().status))
                .body("order.createdAt", equalTo(orderWithAuthorization.getBody().as(General.class).getOrder().createdAt))
                .body("order.updatedAt", equalTo(orderWithAuthorization.getBody().as(General.class).getOrder().updatedAt))
                .body("order.ingredients[1].type", equalTo("main"))
                .body("order.ingredients[1].__v", equalTo(0))
                .body("order.ingredients[1].price", equalTo(1337))
                .body("order.ingredients[1].fat", equalTo(244))
                .body("order.ingredients[1].carbohydrates", equalTo(33))
                .body("order.ingredients[1].calories", equalTo(420))
                .body("order.ingredients[1].proteins", equalTo(433))
                .body("order.ingredients[1].image", equalTo(orderWithAuthorization.getBody().as(General.class).getOrder().getIngredients().get(1).image))
                .body("order.ingredients[1].image_large", equalTo(orderWithAuthorization.getBody().as(General.class).getOrder().getIngredients().get(1).imageLarge))
                .body("order.ingredients[1].image_mobile", equalTo(orderWithAuthorization.getBody().as(General.class).getOrder().getIngredients().get(1).imageMobile))
                .body("order.ingredients[1]._id", equalTo(orderWithAuthorization.getBody().as(General.class).getOrder().getIngredients().get(1).id))
                .body("name", equalTo("Флюоресцентный бессмертный бургер"))
                .body("order.ingredients[1].name", equalTo("Мясо бессмертных моллюсков Protostomia"))
                .body("order.owner.createdAt", equalTo(orderWithAuthorization.getBody().as(General.class).getOrder().getOwner().createdAt))
                .body("order.owner.updatedAt", equalTo(orderWithAuthorization.getBody().as(General.class).getOrder().getOwner().updatedAt))
                .body("order.owner.email", equalTo("piglet@mail.ru"))
                .body("order.owner.name", equalTo("Анна"));

    }

    //с авторизацией, только один ингредиент
    @Test
    @DisplayName("With authorization, one ingredient")
    public void withAuthorizationOneIngredient() {

        AuthClient authClient = new AuthClient();
        Response loginUserResponse = authClient.login(LoginUser.USER_EXISTS);
        loginUserResponse.then().statusCode(HTTP_OK).body("success", equalTo(true));
        String str = loginUserResponse.getBody().as(UserAuthorization.class).getAccessToken().substring(7);
        System.out.println(str);

        OrdersClient ordersClient = new OrdersClient();
        Response orderWithAuthorizationOneIngredient = ordersClient.createANewOrderWithAuthorization(Ingredients.BURGER_ONE_INGREDIENT, str);
        orderWithAuthorizationOneIngredient.then().statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("order.number", equalTo(orderWithAuthorizationOneIngredient.getBody().as(General.class).getOrder().number))
                .body("order.price", equalTo(424))
                .body("order._id", equalTo(orderWithAuthorizationOneIngredient.getBody().as(General.class).getOrder().id))
                .body("order.status", equalTo(orderWithAuthorizationOneIngredient.getBody().as(General.class).getOrder().status))
                .body("order.createdAt", equalTo(orderWithAuthorizationOneIngredient.getBody().as(General.class).getOrder().createdAt))
                .body("order.updatedAt", equalTo(orderWithAuthorizationOneIngredient.getBody().as(General.class).getOrder().updatedAt))
                .body("order.ingredients.type", equalTo(List.of("main")))
                .body("order.ingredients.__v", equalTo(List.of(0)))
                .body("order.ingredients.price", equalTo(List.of(424)))
                .body("order.ingredients.fat", equalTo(List.of(142)))
                .body("order.ingredients.carbohydrates", equalTo(List.of(242)))
                .body("order.ingredients.calories", equalTo(List.of(4242)))
                .body("order.ingredients.proteins", equalTo(List.of(420)))
                .body("order.ingredients.image", equalTo(List.of(orderWithAuthorizationOneIngredient.getBody().as(General.class).getOrder().getIngredients().get(0).image)))
                .body("order.ingredients.image_large", equalTo(List.of(orderWithAuthorizationOneIngredient.getBody().as(General.class).getOrder().getIngredients().get(0).imageLarge)))
                .body("order.ingredients.image_mobile", equalTo(List.of(orderWithAuthorizationOneIngredient.getBody().as(General.class).getOrder().getIngredients().get(0).imageMobile)))
                .body("order.ingredients._id", equalTo(List.of(orderWithAuthorizationOneIngredient.getBody().as(General.class).getOrder().getIngredients().get(0).id)))
                .body("name", equalTo("Био-марсианский бургер"))
                .body("order.ingredients.name", equalTo(List.of("Биокотлета из марсианской Магнолии")))
                .body("order.owner.createdAt", equalTo(orderWithAuthorizationOneIngredient.getBody().as(General.class).getOrder().getOwner().createdAt))
                .body("order.owner.updatedAt", equalTo(orderWithAuthorizationOneIngredient.getBody().as(General.class).getOrder().getOwner().updatedAt))
                .body("order.owner.email", equalTo("piglet@mail.ru"))
                .body("order.owner.name", equalTo("Анна"));

    }

}
