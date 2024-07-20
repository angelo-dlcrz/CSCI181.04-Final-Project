package cs181.finalproject;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RecipeImage extends RealmObject {
    @PrimaryKey
    private String uuid = UUID.randomUUID().toString();
    private String imagePath;
    private User uploader;
    private String username;
    private Recipe recipe;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
        this.username = uploader.getName();
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "RecipeImage{" +
                "uuid='" + uuid + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", uploader=" + uploader +
                ", recipe=" + recipe +
                '}';
    }
}
