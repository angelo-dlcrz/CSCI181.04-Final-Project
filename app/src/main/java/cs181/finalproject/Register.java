package cs181.finalproject;

import android.content.Intent;
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

import io.realm.Case;
import io.realm.Realm;

public class Register extends AppCompatActivity {
    Button save;
    Button cancel;
    EditText username;
    EditText password;
    EditText password2;
    Realm realm;
    ImageView image;
    String imagePath;

    public void init(){
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        image = findViewById(R.id.imageView);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });

        realm = Realm.getDefaultInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    public void save(){

        User newUser =new User();
        long count = 0;

        if(username.getText().length()==0){
            Toast.makeText(this, "Name must not be blank", Toast.LENGTH_SHORT).show();
        }
        else if(!password.getText().toString().equals(password2.getText().toString())){
            Toast.makeText(this, "Confirm password does not match", Toast.LENGTH_SHORT).show();
        }
        else if(password.getText().length()==0 || password2.getText().length()==0) {
            Toast.makeText(this, "Password must not be blank", Toast.LENGTH_SHORT).show();
        }
        else{
            User registered = realm.where(User.class)
                    .equalTo("name", username.getText().toString(), Case.INSENSITIVE)
                    .findFirst();
            if(registered!=null) {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
            }
            else{
                newUser.setName(username.getText().toString());
                newUser.setPassword(password.getText().toString());
                newUser.setPath(imagePath);
                try {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(newUser);
                    realm.commitTransaction();

                    count = realm.where(User.class).count();

                    Toast.makeText(this, "New User saved. Total " + count, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error saving", Toast.LENGTH_SHORT).show();
                }
                finish();

            }

        }
    }

    public void cancel(){
        finish();
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