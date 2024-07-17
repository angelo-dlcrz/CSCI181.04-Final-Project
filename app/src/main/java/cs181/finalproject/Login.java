package cs181.finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;

import io.realm.Case;
import io.realm.Realm;

public class Login extends AppCompatActivity {
    Button signin;
    Button register;
    Button clear;
    EditText user;
    EditText password;
    CheckBox checkBox;
    SharedPreferences sharedPreferences;
    Intent intent;
    Realm realm;
    public void init(){
        signin = findViewById(R.id.signin);
        register = findViewById(R.id.register);
        user = findViewById(R.id.username);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.remember);
        sharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE);

        realm =Realm.getDefaultInstance();

        if (sharedPreferences.getBoolean("remember", false)){
            checkBox.toggle();
            String uuid = sharedPreferences.getString("uuid", null);
            User profile = realm.where(User.class)
                    .equalTo("uuid", uuid)
                    .findFirst();
            if(profile != null) {
                user.setText(profile.getName());
                password.setText(profile.getPassword());
            }
        }


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }
    public void signin(){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        User checkUser = realm.where(User.class)
                .equalTo("name", user.getText().toString(), Case.INSENSITIVE)
                .findFirst();
        if(checkUser!=null){
            if(password.getText().toString().equals(checkUser.getPassword())){
                editor.putBoolean("remember", checkBox.isChecked());
                editor.putString("uuid", checkUser.getUuid());
                editor.apply();

                intent = new Intent(this,Dashboard.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "No User found", Toast.LENGTH_SHORT).show();
        }
    }
    public void register(){
        intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}