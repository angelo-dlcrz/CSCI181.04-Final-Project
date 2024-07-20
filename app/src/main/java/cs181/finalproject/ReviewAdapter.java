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

public class ReviewAdapter extends RealmRecyclerViewAdapter<Rating, ReviewAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView rating;
        TextView comment;
        TextView author;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            rating  = itemView.findViewById(R.id.reviewRating);
            author = itemView.findViewById(R.id.reviewAuthor);
            comment = itemView.findViewById(R.id.reviewComment);
        }
    }

    ReviewPage activity;

    public ReviewAdapter(ReviewPage activity, @Nullable OrderedRealmCollection<Rating> data, boolean autoUpdate) {
        super(data, autoUpdate);

        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = activity.getLayoutInflater().inflate(R.layout.review_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rating review = getItem(position);

        holder.rating.setText(String.format("%d \u2B50", review.getRating()));
        holder.comment.setText(String.format("\"%s\"", review.getComment()));
        holder.author.setText(String.format("Review by %s", review.getUser().getName()));
    }
}
