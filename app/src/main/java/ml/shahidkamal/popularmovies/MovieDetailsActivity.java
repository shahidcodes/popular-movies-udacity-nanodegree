package ml.shahidkamal.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import ml.shahidkamal.popularmovies.API.ResponseHandler;
import ml.shahidkamal.popularmovies.API.TMDB;
import ml.shahidkamal.popularmovies.Adapters.TrailerAdapter;
import ml.shahidkamal.popularmovies.DB.DBHelper;
import ml.shahidkamal.popularmovies.DB.MyContract;
import ml.shahidkamal.popularmovies.Models.Movie;
import ml.shahidkamal.popularmovies.Models.Trailer;

import static ml.shahidkamal.popularmovies.DB.MyContract.FavMovies.CONTENT_URI;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = "MovieDetailsActivity";

    Movie movie;
    TextView movieTitle, movieDesc, movieRating;
    ImageView moviePoster;
    RecyclerView trailerList;
    private Button addFavbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        Bundle bundle = getIntent().getExtras();


        if(bundle != null && bundle.containsKey("movie") ) {
            movie = (Movie) bundle.getSerializable("movie");
            bindViews();
            checkForFav();
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#E640434E"));
        }
    }

    private void checkForFav() {
        boolean isFav = DBHelper.isAlreadyFav(this, movie.movieId);
        if(isFav){
            addFavbtn.setText(R.string.label_added_fav);
            addFavbtn.setOnClickListener(null); // TODO: replace with delete from db method
        }
    }

    private void bindViews() {

        moviePoster = findViewById(R.id.iv_movie_poster_img);
        movieDesc = findViewById(R.id.tv_movie_desc);
        movieTitle = findViewById(R.id.tv_movie_title);
        movieRating = findViewById(R.id.tv_movie_rating);
        trailerList = findViewById(R.id.trailersListView);

        Picasso.get().load(movie.poster).error(R.drawable.ic_broken_image).into(moviePoster);

        movieTitle.setText(movie.orignalTitle);
        movieDesc.setText(movie.overview);
        String year = movie.releaseDate.substring(0, 4);
        movieRating.setText("⭐ " + movie.voteAverage + "/10 - " + year);
        addFavbtn = findViewById(R.id.btnAddFav);


        setupTrailerList();


        findViewById(R.id.reviewsTvBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewActivityIntent = new Intent(MovieDetailsActivity.this, ReviewsActivity.class);
                reviewActivityIntent.putExtra("movieId", movie.movieId);
                startActivity(reviewActivityIntent);
            }
        });

        findViewById(R.id.movie_details_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        addFavbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFav(movie);
            }
        });

    }

    private void addToFav(Movie movie) {
//        boolean status = DBHelper.addToFav(this, movie);
        ContentValues values = new ContentValues();
        values.put(MyContract.FavMovies.COLUMN_MOVIE_NAME, movie.orignalTitle);
        values.put(MyContract.FavMovies.COLUMN_MOVIE_ID, movie.movieId);
        values.put(MyContract.FavMovies.COLUMN_MOVIE_OVERVIEW, movie.overview);
        values.put(MyContract.FavMovies.COLUMN_MOVIE_POSTER, movie.poster);
        values.put(MyContract.FavMovies.COLUMN_MOVIE_VOTE, movie.voteAverage);
        values.put(MyContract.FavMovies.COLUMN_MOVIE_RELEASE, movie.releaseDate);
        Uri added = getContentResolver().insert(CONTENT_URI, values);
        if (added != null) {
            Toast.makeText(this, "ADDED TO FAV!", Toast.LENGTH_SHORT).show();
            addFavbtn.setText(R.string.label_added_fav);
        }else{
            Toast.makeText(this, "Error adding movie to fav!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupTrailerList() {

        TMDB.getTrailersFromMovieId(this, movie.movieId, new ResponseHandler(){

            @Override
            public void onSuccess(JSONObject response) {
                ArrayList<Trailer> trailers = TMDB.parseTrailersResponse(response);
                TrailerAdapter adapter = new TrailerAdapter(MovieDetailsActivity.this, trailers);
                trailerList.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this));
                trailerList.setAdapter(adapter);
            }

            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
            }
        });

    }
}
