package cs181.finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.realm.Realm;

public class AddRating extends AppCompatActivity {
    private EditText recipeRating, recipeComment;
    private Button add, cancel;
    SharedPreferences prefs;
    Realm realm;

    public void init(){
        recipeRating = findViewById(R.id.rating);
        recipeComment = findViewById(R.id.comments);
        add = findViewById(R.id.ratingAdd);
        cancel = findViewById(R.id.ratingCancel);

        realm = Realm.getDefaultInstance();
        prefs = getSharedPreferences("user_details", MODE_PRIVATE);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRating();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRating();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_rating);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    public void addRating(){
        Rating rating = new Rating();
        String uuid = prefs.getString("uuid", null);
        String recipeId = getIntent().getStringExtra("recipeId");
        User currentUser = realm.where(User.class)
                .equalTo("uuid", uuid)
                .findFirst();
        Recipe currentRecipe = realm.where(Recipe.class)
                .equalTo("uuid", recipeId)
                .findFirst();
        if (recipeRating.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter a rating", Toast.LENGTH_SHORT).show();
        } else if (recipeComment.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
        } else {
            rating.setRating(Integer.parseInt(recipeRating.getText().toString()));
            rating.setComment(recipeComment.getText().toString());
            rating.setUser(currentUser);
            rating.setRecipe(currentRecipe);
            try {
                realm.beginTransaction();
                realm.copyToRealm(rating);
                realm.commitTransaction();
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    public void cancelRating(){
        finish();
    }

}