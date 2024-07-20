package cs181.finalproject;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class UserGalleryAdapter extends RealmRecyclerViewAdapter<Recipe, UserGalleryAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView image;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            name  = itemView.findViewById(R.id.recipeName2);
        }
    }

    UserDetail activity;

    public UserGalleryAdapter(UserDetail activity, @Nullable RealmResults<Recipe> data, boolean autoUpdate) {
        super(data, autoUpdate);

        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = activity.getLayoutInflater().inflate(R.layout.user_photo_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = getItem(position);

        LinearLayout imageContainer = holder.itemView.findViewById(R.id.imageContainer);
        imageContainer.removeAllViews(); // Clear previous images

        RealmList<String> realmImagePaths = recipe.getImagePaths();
        if (realmImagePaths != null && !realmImagePaths.isEmpty()) {
            List<String> imagePaths = new ArrayList<>(realmImagePaths);

            for (String imagePath : imagePaths) {
                File getImageDir = activity.getExternalCacheDir();
                File file = new File(getImageDir, imagePath);

                if (file.exists()) {
                    LinearLayout imageAndNameLayout = new LinearLayout(activity);
                    imageAndNameLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    imageAndNameLayout.setLayoutParams(layoutParams);

                    TextView nameTextView = new TextView(activity);
                    nameTextView.setText(recipe.getName());
                    imageAndNameLayout.addView(nameTextView);

                    ImageView imageView = new ImageView(activity);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            500, 500);
                    imageView.setLayoutParams(imageParams);
                    Picasso.get()
                            .load(file)
                            .fit()
                            .centerCrop()
                            .into(imageView);
                    imageAndNameLayout.addView(imageView);

                    imageContainer.addView(imageAndNameLayout);
                }
            }
        }
    }

}
