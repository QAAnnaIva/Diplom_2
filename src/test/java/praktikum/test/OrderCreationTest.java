package praktikum.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import praktikum.General;
import praktikum.UserAuthorization;

import java.util.List;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static praktikum.EndPoints.*;
import static praktikum.Ingredients.*;
import static praktikum.PatchUser.USER_EXISTS;

public class OrderCreationTest {

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

//без авторизации, с ингредиентами
    @Test
    public void withoutAuthorizationWithIngredients() {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(BURGER_INGREDIENTS)
                        .when()
                        .post(ORDERS);
        response.then().statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("order.number", equalTo(response.getBody().as(General.class).getOrder().number))
                .body("name", equalTo(response.getBody().as(General.class).getName()));

        System.out.println(response.getBody().as(General.class).getOrder().number);
        System.out.println(response.getBody().as(General.class).getName());

    }

//без авторизации, без ингредиентов
    @Test
    public void withoutAuthorizationWithoutIngredients() {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(WITHOUT_INGREDIENTS)
                        .when()
                        .post(ORDERS);
        response.then().statusCode(HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

//без авторизации, неверный хеш ингредиентов
    @Test
    public void withoutAuthorizationErrorIngredients() {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(ERROR_INGREDIENTS)
                        .when()
                        .post(ORDERS);
        response.then().statusCode(HTTP_INTERNAL_ERROR);
    }

//с авторизацией, с ингредиентами
    @Test
    public void withAuthorizationWithIngredients() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(USER_EXISTS)
                        .when()
                        .post(LOGIN);

        response.then().body("accessToken",equalTo(response.getBody().as(UserAuthorization.class).getAccessToken()));
        String str = response.getBody().as(UserAuthorization.class).getAccessToken().substring(7);
        System.out.println(str);

        Response response1 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(BURGER_INGREDIENTS)
                        .when()
                        .auth().oauth2(str)
                        .post(ORDERS);
        response1.then().statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("order.number", equalTo(response1.getBody().as(General.class).getOrder().number))
                .body("order.price", equalTo(2325))
                .body("order._id", equalTo(response1.getBody().as(General.class).getOrder().id))
                .body("order.status", equalTo(response1.getBody().as(General.class).getOrder().status))
                .body("order.createdAt",equalTo(response1.getBody().as(General.class).getOrder().createdAt))
                .body("order.updatedAt",equalTo(response1.getBody().as(General.class).getOrder().updatedAt))
                .body("order.ingredients[1].type", equalTo("main"))
                .body("order.ingredients[1].__v", equalTo(0))
                .body("order.ingredients[1].price", equalTo(1337))
                .body("order.ingredients[1].fat", equalTo(244))
                .body("order.ingredients[1].carbohydrates", equalTo(33))
                .body("order.ingredients[1].calories", equalTo(420))
                .body("order.ingredients[1].proteins", equalTo(433))
                .body("order.ingredients[1].image", equalTo(response1.getBody().as(General.class).getOrder().getIngredients().get(1).image))
                //.body("order.ingredients[1].image_large", equalTo(response1.getBody().as(General.class).getOrder().getIngredients().get(1).imageLarge))
                //.body("order.ingredients[1].image_mobile", equalTo(response1.getBody().as(General.class).getOrder().getIngredients().get(1).imageMobile))
                .body("order.ingredients[1]._id", equalTo(response1.getBody().as(General.class).getOrder().getIngredients().get(1).id))
                .body("name", equalTo("Флюоресцентный бессмертный бургер"))
                .body("order.ingredients[1].name", equalTo("Мясо бессмертных моллюсков Protostomia"))
                .body("order.owner.createdAt",equalTo(response1.getBody().as(General.class).getOrder().getOwner().createdAt))
                .body("order.owner.updatedAt",equalTo(response1.getBody().as(General.class).getOrder().getOwner().updatedAt))
                .body("order.owner.email", equalTo("piglet@mail.ru"))
                .body("order.owner.name", equalTo("Анна"));

    }

//с авторизацией, только один ингредиент
   /* @Test
    public void withAuthorizationOneIngredient() {
    Response response =
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(USER_EXISTS)
                    .when()
                    .post(LOGIN);

    response.then().body("accessToken",equalTo(response.getBody().as(UserAuthorization.class).getAccessToken()));
    String str = response.getBody().as(UserAuthorization.class).getAccessToken().substring(7);
    System.out.println(str);

    Response response1 =
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(BURGER_ONE_INGREDIENT)
                    .when()
                    .auth().oauth2(str)
                    .post(ORDERS);
    response1.then().statusCode(HTTP_OK)
            .body("success", equalTo(true))
            .body("order.number", equalTo(response1.getBody().as(General.class).getOrder().number))
            .body("order.price", equalTo(424))
            .body("order._id", equalTo(response1.getBody().as(General.class).getOrder()._id))
            .body("order.status", equalTo(response1.getBody().as(General.class).getOrder().status))
            .body("order.createdAt",equalTo(response1.getBody().as(General.class).getOrder().createdAt))
            .body("order.updatedAt",equalTo(response1.getBody().as(General.class).getOrder().updatedAt))
            .body("order.ingredients.type", equalTo(List.of("main")))
            .body("order.ingredients.__v", equalTo(List.of(0)))
            .body("order.ingredients.price", equalTo(List.of(424)))
            .body("order.ingredients.fat", equalTo(List.of(142)))
            .body("order.ingredients.carbohydrates", equalTo(List.of(242)))
            .body("order.ingredients.calories", equalTo(List.of(4242)))
            .body("order.ingredients.proteins", equalTo(List.of(420)))
            .body("order.ingredients.image", equalTo(List.of(response1.getBody().as(General.class).getOrder().getIngredients().get(0).image)))
            .body("order.ingredients.image_large", equalTo(List.of(response1.getBody().as(General.class).getOrder().getIngredients().get(0).image_large)))
            .body("order.ingredients.image_mobile", equalTo(List.of(response1.getBody().as(General.class).getOrder().getIngredients().get(0).image_mobile)))
            .body("order.ingredients._id", equalTo(List.of(response1.getBody().as(General.class).getOrder().getIngredients().get(0)._id)))
            .body("name", equalTo("Био-марсианский бургер"))
            .body("order.ingredients.name", equalTo(List.of("Биокотлета из марсианской Магнолии")))
            .body("order.owner.createdAt",equalTo(response1.getBody().as(General.class).getOrder().getOwner().createdAt))
            .body("order.owner.updatedAt",equalTo(response1.getBody().as(General.class).getOrder().getOwner().updatedAt))
            .body("order.owner.email", equalTo("piglet@mail.ru"))
            .body("order.owner.name", equalTo("Анна"));

}*/

}
