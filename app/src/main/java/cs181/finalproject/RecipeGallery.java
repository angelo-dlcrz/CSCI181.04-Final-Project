package cs181.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RecipeGallery extends AppCompatActivity {
    private RecyclerView recyclerView;
    Realm realm;
    String recipeId;

    public void init(){
        recyclerView = findViewById(R.id.galleryRecycler);
        realm = Realm.getDefaultInstance();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            recipeId = bundle.getString("recipeId");
        }
        else{
            recipeId = null;
        }

        RealmList<RecipeImage> images = realm.where(Recipe.class)
                .equalTo("uuid", recipeId)
                .findFirst()
                .getUserImages();
        RecipeImageAdapter adapter = new RecipeImageAdapter(this, images, true);
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_gallery);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }
}