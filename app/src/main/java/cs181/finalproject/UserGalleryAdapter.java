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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class UserGalleryAdapter extends RealmRecyclerViewAdapter<RecipeImage, UserGalleryAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView recipeImage;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            recipeImage = itemView.findViewById(R.id.userRecipeImage);
        }
    }

    UserDetail activity;

    public UserGalleryAdapter(UserDetail activity, @Nullable OrderedRealmCollection<RecipeImage> data, boolean autoUpdate) {
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
        RecipeImage image = getItem(position);

        if (image.getImagePath() != null) {
            File getImageDir = activity.getExternalCacheDir();
            File file = new File(getImageDir, image.getImagePath());
            if (file.exists()) {
                Picasso.get()
                        .load(file)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(holder.recipeImage);
            }
        }
        else {
            holder.recipeImage.setImageResource(R.mipmap.ic_launcher);
        }
    }

}