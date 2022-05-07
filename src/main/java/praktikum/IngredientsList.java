package praktikum;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;

import java.lang.reflect.Array;
import java.util.List;

public class IngredientsList {

    @JsonProperty("_id") public String id;

   /* @JsonProperty("_id")
    public String getId() {
        return id;
    }*/

    public String name;
    public String type;
    public Integer proteins;
    public Integer fat;
    public Integer carbohydrates;
    public Integer calories;
    public Double price;
    public String image;
    @JsonProperty("image_mobile") public String imageMobile;
    @JsonProperty("image_large") public String imageLarge;
    @JsonProperty("__v") public Integer v;

}
