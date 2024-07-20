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

import org.w3c.dom.Text;

import java.io.File;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RecipeAdapter extends RealmRecyclerViewAdapter<Recipe, RecipeAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView description;
        TextView author;
        ImageView image;
        Button button;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            name  = itemView.findViewById(R.id.recipeName);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.recipeDescription);
            image = itemView.findViewById(R.id.recipeImage);
            button = itemView.findViewById(R.id.button);
        }
    }

    Dashboard activity;

    public RecipeAdapter(Dashboard activity, @Nullable OrderedRealmCollection<Recipe> data, boolean autoUpdate) {
        super(data, autoUpdate);

        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = activity.getLayoutInflater().inflate(R.layout.recipe_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = getItem(position);

        holder.name.setText(recipe.getName());
        holder.description.setText(recipe.getDescription());
        holder.author.setText("by " + recipe.getAuthor().getName());

        if (recipe.getImagePaths().get(0) != null) {
            File getImageDir = activity.getExternalCacheDir();
            File file = new File(getImageDir, recipe.getImagePaths().get(recipe.getImagePaths().size() - 1));
            if (file.exists()) {
                Picasso.get()
                        .load(file)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(holder.image);
            }
        }
        else {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.details();
            }
        });
    }
}
