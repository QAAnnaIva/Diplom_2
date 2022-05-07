package praktikum;

import com.google.gson.annotations.SerializedName;

public class IngredientsList {

    @SerializedName(value = "_id")
    public String id;

    public String name;
    public String type;
    public Integer proteins;
    public Integer fat;
    public Integer carbohydrates;
    public Integer calories;
    public Double price;
    public String image;

    @SerializedName(value = "image_mobile")
    public String imageMobile;
    @SerializedName(value = "image_large")
    public String imageLarge;
    @SerializedName(value = "__v")
    public Integer v;

}
