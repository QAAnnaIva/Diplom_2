package praktikum;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Orders {

    public List getIngredients() {
        return ingredients;
    }

    public List<String> ingredients;

    public String status;
    public String createdAt;
    public String updatedAt;

    @SerializedName(value = "_id")
    public String id;
    public String name;
    public Integer number;
}
