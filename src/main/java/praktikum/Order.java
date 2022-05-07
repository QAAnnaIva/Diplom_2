package praktikum;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonCreator;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public Integer number;
    public Owner owner;
    public Integer price;
    public String name;

    public Integer getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public List<IngredientsList> getIngredients() {
        return ingredients;
    }

    public String status;
    public String createdAt;
    public String updatedAt;


  /*  @JsonProperty("_id")
    public String getId() {
        return id;
    }*/

    @JsonProperty("_id") public String id;
    public List<IngredientsList> ingredients = new ArrayList<>();

    public Owner getOwner() {
        return owner;
    }

   /* @JsonCreator

    public Order(

            @JsonProperty("_id") String id) {

        this._id = id;


    }*/
}

