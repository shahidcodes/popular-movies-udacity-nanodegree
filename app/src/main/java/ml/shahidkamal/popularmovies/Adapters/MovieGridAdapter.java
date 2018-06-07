package ml.shahidkamal.popularmovies.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ml.shahidkamal.popularmovies.Models.Movie;
import ml.shahidkamal.popularmovies.R;

/**
 * Created by shaah on 07-06-2018.
 */

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieGridViewHolder> {


    Context context;
    ArrayList<Movie> movieArrayList;
    View.OnClickListener onClickListener;

    public MovieGridAdapter(Context context, ArrayList<Movie> movies, View.OnClickListener onClickListener) {
        this.movieArrayList = movies;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public MovieGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                                .inflate(R.layout.movie_grid, parent, false);
        return new MovieGridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieGridViewHolder holder, int position) {
        Movie movie = movieArrayList.get(position);
        Picasso.get()
                .load(movie.poster)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    class MovieGridViewHolder extends RecyclerView.ViewHolder  {
        public  ImageView imageView;

        public MovieGridViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.moviePoster);
            itemView.setOnClickListener(onClickListener);
        }
    }
}
