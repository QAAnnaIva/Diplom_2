package praktikum;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;

import java.util.List;

public class Orders {

    public List getIngredients() {
        return ingredients;
    }

    public List<String> ingredients;

    public String status;
    public String createdAt;
    public String updatedAt;

   /* @JsonProperty("_id")
    public String getId() {
        return id;
    }*/

    @JsonProperty("_id") public String id;
    public String name;
    public Integer number;
}
