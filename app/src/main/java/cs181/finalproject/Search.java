package cs181.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class Search extends AppCompatActivity {
    private EditText name;
    private Button button, home;
    private Realm realm;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home();
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        name = findViewById(R.id.searchname);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        realm = Realm.getDefaultInstance();
        RealmResults<Recipe> list = realm.where(Recipe.class).findAll();
        RecipeAdapter2 adapter = new RecipeAdapter2(this, list, true);
        recyclerView.setAdapter(adapter);
    }
    public void search(){
        String recipeName = name.getText().toString();
        RealmResults<Recipe> list = realm.where(Recipe.class).
                contains("name", recipeName, Case.INSENSITIVE)
                .findAll();
        RecipeAdapter2 adapter = new RecipeAdapter2(this, list, true);
        recyclerView.setAdapter(adapter);
    }
    public void home(){
        finish();
    }
    public void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed())
        {
            realm.close();
        }
    }
    public void details(){
        //set intent to recipe details
    }
}