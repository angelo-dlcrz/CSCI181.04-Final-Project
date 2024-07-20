package cs181.finalproject;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Recipe extends RealmObject {
    @PrimaryKey
    private String uuid = UUID.randomUUID().toString();
    private String name;
    private User author;
    private String description;
    private String instructions;
    private String ingredients;
    private String path;
    private RealmList<Rating> ratings;
    private RealmList<RecipeImage> userImages;

    public Recipe(){}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public User getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", author=" + author +
                ", description='" + description + '\'' +
                ", instructions='" + instructions + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", path='" + path + '\'' +
                ", ratings=" + ratings +
                ", userImages=" + userImages +
                '}';
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public RealmList<Rating> getRatings() {
        return ratings;
    }

    public void addRatings(Rating rating) {
        if(this.ratings==null){
            this.ratings = new RealmList<>();
        }
        this.ratings.add(rating);
    }

    public RealmList<RecipeImage> getUserImages() {
        return userImages;
    }

    public void addUserImages(RecipeImage image) {
        if(this.userImages==null){
            this.userImages = new RealmList<>();
        }
        this.userImages.add(image);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
