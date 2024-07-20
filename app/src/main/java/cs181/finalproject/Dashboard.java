package cs181.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmResults;

public class Dashboard extends AppCompatActivity {
    private ImageButton search, user, add;
    private RecyclerView recyclerView;
    Realm realm;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user();
            }
        });
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        realm = Realm.getDefaultInstance();
        RealmResults<Recipe> list = realm.where(Recipe.class).findAll();
        RecipeAdapter adapter = new RecipeAdapter(this, list, true);
        recyclerView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String uuid = sharedPreferences.getString("uuid", null);
        User u = realm.where(User.class)
                .equalTo("uuid", uuid)
                .findFirst();

        File getImageDir = getExternalCacheDir();
        if(u.getPath()!=null) {
            File file = new File(getImageDir, u.getPath());
            if (file.exists()) {
                Picasso.get()
                        .load(file)
                        .into(user);
            } else {
                user.setImageResource(R.mipmap.ic_launcher);
            }
        }


    }
    public void add(){
        Intent intent = new Intent(this, AddRecipe.class);
        startActivity(intent);
    }
    public void details(){
        //set intent to recipe details
    }
    public void onDestroy() {
        super.onDestroy();

        if (!realm.isClosed()) {
            realm.close();
        }
    }
    public void search(){
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }
    public void user(){
        Intent intent = new Intent(this, Edit.class);
        String uuid = sharedPreferences.getString("uuid", null);
        intent.putExtra("uuid", uuid);
        startActivity(intent);
    }
}