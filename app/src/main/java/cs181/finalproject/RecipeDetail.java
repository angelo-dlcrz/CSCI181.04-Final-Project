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
    private String imagePath;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        sharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE);

        title = (TextView) findViewById(R.id.recipetitle);
        image = findViewById(R.id.imageView3);
        ingredients = findViewById(R.id.ingredientsdetail);
        steps = findViewById(R.id.stepsdetail);
        ratings = findViewById(R.id.button);
        photos = findViewById(R.id.button3);
        addphoto = findViewById(R.id.button2);

        realm = Realm.getDefaultInstance();
        intent = getIntent();

        if(intent.hasExtra("recipeUuid")){
            receivedUUID = intent.getStringExtra("recipeUuid");
        }

        if (receivedUUID != null) {
            RealmQuery<Recipe> query = realm.where(Recipe.class).equalTo("uuid", receivedUUID);
            recipe = query.findFirst();

            /*
            if (editUser != null) {
                String imagePath = editUser.getPath();
                if (imagePath != null && !imagePath.isEmpty()) {
                    File imageFile = new File(getExternalCacheDir(), imagePath);
                    if (imageFile.exists()) {
                        Picasso.get()
                                .load(imageFile)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(imageView3);
                    } else {
                        Log.e("UserEdit", "Image file not found: " + imageFile.getAbsolutePath());
                    }
                }
            }
             */
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

        String imagePath = recipe.getPath();
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(getExternalCacheDir(), imagePath);
            Picasso.get()
                    .load(imageFile)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(image);

        }


    }

    public void viewratings(){
        //make intent to go to ratings.class
    }

    public void viewphoto(){
        String uuid = sharedPreferences.getString("uuid", "N/A");
        Intent intent = new Intent(this, UserDetail.class);
        intent.putExtra("userUuid", uuid);
        Log.d("Dashboard", "DASHBOARD UUID: " + uuid);
        startActivity(intent);
    }
    public void addImage(){
        Intent intent = new Intent(this, ImageActivity.class);
        startActivityForResult(intent, 0);
    }
    public void onActivityResult(int requestCode, int responseCode, Intent data){
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
                    try {
                        realm.beginTransaction();
                        recipe.setPath(imagePath);
                        recipe.addImagePath(imagePath);
                        realm.commitTransaction();
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