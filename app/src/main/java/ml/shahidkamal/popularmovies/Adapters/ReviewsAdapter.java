package ml.shahidkamal.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ml.shahidkamal.popularmovies.Models.Reviews;
import ml.shahidkamal.popularmovies.R;

/**
 * Created by shaah on 26-07-2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.rViewHolder> {
    ArrayList<Reviews> reviews;
    Context context;

    public ReviewsAdapter(Context context, ArrayList<Reviews> reviewsList) {
        reviews = reviewsList;
        this.context = context;
    }


    @Override
    public rViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_row, null, false);
        return new rViewHolder(view);
    }

    @Override
    public void onBindViewHolder(rViewHolder holder, int position) {
        Reviews review = reviews.get(position);
        holder.author.setText(review.author);
        holder.content.setText(review.review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class rViewHolder extends RecyclerView.ViewHolder{

        public TextView author, content;

        public rViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.reviewAuthor);
            content = itemView.findViewById(R.id.reviewContent);
        }
    }
}
