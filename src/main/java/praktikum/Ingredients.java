package praktikum;

public class Ingredients {

    String[] ingredients;

    public static final Ingredients BURGER_INGREDIENTS = new Ingredients(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"});
    public static final Ingredients BURGER_ONE_INGREDIENT = new Ingredients(new String[]{"61c0c5a71d1f82001bdaaa71"});
    public static final Ingredients WITHOUT_INGREDIENTS = new Ingredients(new String[]{});
    public static final Ingredients ERROR_INGREDIENTS = new Ingredients(new String[]{"61c0c5a71f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"});

    public Ingredients(String[] ingredients) {
        this.ingredients = ingredients;
    }


}
