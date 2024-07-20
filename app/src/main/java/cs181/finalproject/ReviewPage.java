package cs181.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class ReviewPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    Realm realm;
    String recipeId;
    private Button addRating;

    public void init(){
        recyclerView = findViewById(R.id.reviewsRecycler);
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
        Recipe recipe = realm.where(Recipe.class)
                .equalTo("uuid", recipeId)
                .findFirst();
        Log.e("recipeID", recipe.toString());

        RealmList<Rating> ratings = realm.where(Recipe.class)
                .equalTo("uuid", recipeId)
                .findFirst()
                .getRatings();
        ReviewAdapter adapter = new ReviewAdapter(this, ratings, true);
        recyclerView.setAdapter(adapter);

        addRating = findViewById(R.id.addRating);
        addRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRating();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }
    public void addRating(){
        Intent intent = new Intent(this, AddRating.class);
        intent.putExtra("recipeId", recipeId);
        startActivity(intent);
    }
}