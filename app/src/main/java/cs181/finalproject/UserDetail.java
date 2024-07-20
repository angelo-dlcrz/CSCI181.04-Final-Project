package cs181.finalproject;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class UserDetail extends AppCompatActivity {
    String receivedUUID;
    User user;
    Realm realm;
    Intent intent;
    String username;
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_detail);

        init();
    }

    public void init() {
        realm = Realm.getDefaultInstance();
        recyclerView = findViewById(R.id.usergallery);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String uuid = sharedPreferences.getString("uuid", null);
        user = realm.where(User.class)
                .equalTo("uuid", uuid)
                .findFirst();
        username = user.getName();
//        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
//
//        receivedUUID = sharedPreferences.getString("uuid", "N/A");
//        if (receivedUUID != "N/A") {
//            RealmQuery<User> query = realm.where(User.class).equalTo("uuid", receivedUUID);
//            user = query.findFirst();
//            assert user != null;
//            username = user.getName();
//        }

        Log.d("UserDetail", "CURRENT LOGGED IN USER: " + username);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        RealmResults<RecipeImage> list = realm.where(RecipeImage.class).equalTo("username", username).findAll();
        UserGalleryAdapter adapter = new UserGalleryAdapter(this, list, true);
        recyclerView.setAdapter(adapter);
    }

    public void onDestroy() {
        super.onDestroy();

        if (!realm.isClosed()) {
            realm.close();
        }
    }


}