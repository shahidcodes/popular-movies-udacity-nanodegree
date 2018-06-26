package ml.shahidkamal.popularmovies;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ml.shahidkamal.popularmovies.Models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView movieTitle, movieDesc, movieRating;
    ImageView moviePoster;
    ImageButton closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        Bundle bundle = getIntent().getExtras();

        if (bundle == null ) finish();

        movie = (Movie) bundle.getSerializable("movie");

        bindViews();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#33FFFFFF"));
        }
    }

    private void bindViews() {

        moviePoster = findViewById(R.id.iv_movie_poster_img);
        movieDesc = findViewById(R.id.tv_movie_desc);
        movieTitle = findViewById(R.id.tv_movie_title);
        movieRating = findViewById(R.id.tv_movie_rating);

        Picasso.get().load(movie.poster).error(R.drawable.ic_broken_image).into(moviePoster);

        movieTitle.setText(movie.orignalTitle);
        movieDesc.setText(movie.overview);
        String year = movie.releaseDate.substring(0, 4);
        movieRating.setText("⭐ " + movie.voteAverage + "/10 - " + year);



        findViewById(R.id.movie_details_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
