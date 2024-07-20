package cs181.finalproject;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RecipeImageAdapter extends RealmRecyclerViewAdapter<RecipeImage, RecipeImageAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView recipeImage;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            recipeImage = itemView.findViewById(R.id.userRecipeImage);
        }
    }

    RecipeGallery activity;

    public RecipeImageAdapter(RecipeGallery activity, @Nullable OrderedRealmCollection<RecipeImage> data, boolean autoUpdate) {
        super(data, autoUpdate);

        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = activity.getLayoutInflater().inflate(R.layout.recipe_image_layout, parent, false);

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
