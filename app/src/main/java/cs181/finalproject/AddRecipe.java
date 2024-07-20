package cs181.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AddRecipe extends AppCompatActivity {
    private Button add, cancel;
    private EditText name, description, ingredients, steps;
    private Button image;
    private ImageView imageView;
    private RealmList<String> imagePaths;
    SharedPreferences sharedPreferences;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        name = findViewById(R.id.recipename);
        description = findViewById(R.id.description);
        ingredients = findViewById(R.id.ingredients);
        steps = findViewById(R.id.instructions);
        image = findViewById(R.id.recipeImage);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });
        imageView = findViewById(R.id.image);
        realm = Realm.getDefaultInstance();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        imagePaths = new RealmList<>();
    }

    public void add() {
        Recipe recipe = new Recipe();
        String uuid = sharedPreferences.getString("uuid", null);
        User user = realm.where(User.class)
                .equalTo("uuid", uuid)
                .findFirst();

        if (name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
        } else if (description.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
        } else if (ingredients.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter ingredients", Toast.LENGTH_SHORT).show();
        } else if (steps.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter instructions", Toast.LENGTH_SHORT).show();
        } else if (imagePaths.isEmpty()) {
            Toast.makeText(this, "Please add an image", Toast.LENGTH_SHORT).show();
        }
        else {
            recipe.setName(name.getText().toString());
            recipe.setDescription(description.getText().toString());
            recipe.setIngredients(ingredients.getText().toString());
            recipe.setInstructions(steps.getText().toString());
            recipe.setImagePaths(imagePaths);
            recipe.setAuthor(user);

            try {
                realm.beginTransaction();
                realm.copyToRealm(recipe);
                realm.commitTransaction();
                Toast.makeText(this, "Recipe saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error saving", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    public void cancel() {
        finish();
    }

    public void addImage() {
        Intent intent = new Intent(this, ImageActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);

        if (requestCode == 0) {
            if (responseCode == ImageActivity.RESULT_CODE_IMAGE_TAKEN) {
                byte[] jpeg = data.getByteArrayExtra("rawJpeg");

                try {
                    String imagePath = System.currentTimeMillis() + ".jpeg";
                    File savedImage = saveFile(jpeg, imagePath);

                    imagePaths.add(imagePath);
                    refreshImageView(imageView, savedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private File saveFile(byte[] jpeg, String filename) throws IOException {
        File getImageDir = getExternalCacheDir();
        File savedImage = new File(getImageDir, filename);
        FileOutputStream fos = new FileOutputStream(savedImage);
        fos.write(jpeg);
        fos.close();
        return savedImage;
    }

    private void refreshImageView(ImageView imageView, File savedImage) {
        Picasso.get()
                .load(savedImage)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!realm.isClosed()) {
            realm.close();
        }
    }
}
