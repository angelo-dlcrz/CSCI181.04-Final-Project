package cs181.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;

public class RecipeDetail extends AppCompatActivity {

    String receivedUUID;
    Intent intent;
    Realm realm;
    TextView title;
    TextView ingredients;
    TextView steps;
    Button ratings;
    Button photos;
    Button addphoto;
    ImageView image;
    Recipe recipe;
    RecipeImage recipeImage;
    private String imagePath;
    SharedPreferences sharedPreferences;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        realm = Realm.getDefaultInstance();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String uuid = sharedPreferences.getString("uuid", null);
        user = realm.where(User.class)
                .equalTo("uuid", uuid)
                .findFirst();

        title = (TextView) findViewById(R.id.recipetitle);
        image = findViewById(R.id.imageView3);
        ingredients = findViewById(R.id.ingredientsdetail);
        steps = findViewById(R.id.stepsdetail);
        ratings = findViewById(R.id.button);
        photos = findViewById(R.id.button3);
        addphoto = findViewById(R.id.button2);


        intent = getIntent();

        if(intent.hasExtra("recipeUuid")){
            receivedUUID = intent.getStringExtra("recipeUuid");
        }

        if (receivedUUID != null) {
            RealmQuery<Recipe> query = realm.where(Recipe.class).equalTo("uuid", receivedUUID);
            recipe = query.findFirst();
        }

        ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewratings();
            }
        });

        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewphoto();
            }
        });

        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });

        title.setText(recipe.getName());
        steps.setText(recipe.getInstructions());
        ingredients.setText(recipe.getIngredients());

        RealmList<String> imagePath = recipe.getImagePaths();
        if (!imagePath.isEmpty()) {
            File imageFile = new File(getExternalCacheDir(), recipe.getImagePaths().get(recipe.getImagePaths().size() - 1));
            Picasso.get()
                    .load(imageFile)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(image);

        }


    }

    public void viewratings(){
        Intent intent = new Intent(this, ReviewPage.class);
        intent.putExtra("recipeId", receivedUUID);
        startActivity(intent);
    }

    public void viewphoto(){
        Intent intent = new Intent(this, RecipeGallery.class);
        intent.putExtra("recipeId", recipe.getUuid());
        startActivity(intent);
    }
    public void addImage(){
        Intent intent = new Intent(this, ImageActivity.class);
        startActivityForResult(intent, 0);
    }
    public void onActivityResult(int requestCode, int responseCode, Intent data){
        recipeImage = new RecipeImage();
        super.onActivityResult(requestCode, responseCode, data);

        if(requestCode==0){
            if(responseCode == ImageActivity.RESULT_CODE_IMAGE_TAKEN){
                byte[] jpeg = data.getByteArrayExtra("rawJpeg");

                try{
                    imagePath = System.currentTimeMillis()+".jpeg";
                    File savedImage = saveFile(jpeg, imagePath);
                    Log.e("RecipeDetail", "IMAGE SAVED + ADDED TO addImagePath: " + imagePath);
                    String sd = String.join(",", recipe.getImagePaths());
                    Log.e("RecipeDetail", "FOLLOWING IMAGE PATHS: " + sd);
                    recipeImage.setImagePath(imagePath);
                    recipeImage.setRecipe(recipe);
                    recipeImage.setUploader(user);
                    try {
                        realm.beginTransaction();
                        realm.copyToRealm(recipeImage);
                        recipe.addUserImages(recipeImage);
                        realm.commitTransaction();
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        Toast.makeText(this, "Error saving", Toast.LENGTH_SHORT).show();
                    }
                    refreshImageView(image, savedImage);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    private File saveFile(byte[] jpeg, String filename) throws IOException
    {
        // this is the root directory for the images
        File getImageDir = getExternalCacheDir();

        // just a sample, normally you have a diff image name each time
        File savedImage = new File(getImageDir, filename);


        FileOutputStream fos = new FileOutputStream(savedImage);
        fos.write(jpeg);
        fos.close();
        return savedImage;
    }
    private void refreshImageView(ImageView imageView, File savedImage) {


        // this will put the image saved to the file system to the imageview
        Picasso.get()
                .load(savedImage)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageView);
    }
    public void onDestroy() {
        super.onDestroy();

        if (!realm.isClosed()) {
            realm.close();
        }
    }
}