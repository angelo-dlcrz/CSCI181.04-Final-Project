package cs181.finalproject;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Rating extends RealmObject {
    @PrimaryKey
    private String uuid = UUID.randomUUID().toString();
    private User user;
    private Recipe recipe;
    private int rating;
    private String comment;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "uuid='" + uuid + '\'' +
                ", user=" + user +
                ", recipe=" + recipe +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
