package cs181.finalproject;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    private String uuid = UUID.randomUUID().toString();
    private String name;
    private String password;
    private String path;
    private RealmList<Recipe> createdRecipes;
    private RealmList<Rating> ratingsGiven;

    public User(){}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public RealmList<Recipe> getCreatedRecipes() {
        return createdRecipes;
    }

    public void addCreatedRecipes(Recipe recipe) {
        this.createdRecipes = createdRecipes;
        if (this.createdRecipes==null){
            this.createdRecipes = new RealmList<>();
        }
        this.createdRecipes.add(recipe);
    }

    public RealmList<Rating> getRatingsGiven() {
        return ratingsGiven;
    }

    public void addRatingsGiven(Rating rating) {
        if(this.ratingsGiven==null){
            this.ratingsGiven = new RealmList<>();
        }
        this.ratingsGiven.add(rating);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", path='" + path + '\'' +
                ", createdRecipes=" + createdRecipes +
                ", ratingsGiven=" + ratingsGiven +
                '}';
    }
}
