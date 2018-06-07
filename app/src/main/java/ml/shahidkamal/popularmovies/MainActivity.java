package ml.shahidkamal.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;

import ml.shahidkamal.popularmovies.API.ResponseHandler;
import ml.shahidkamal.popularmovies.API.TMDB;
import ml.shahidkamal.popularmovies.API.VolleySingleton;
import ml.shahidkamal.popularmovies.Adapters.MovieGridAdapter;
import ml.shahidkamal.popularmovies.Models.Movie;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    RecyclerView recyclerView;
    MovieGridAdapter adapter;

    ArrayList<Movie> movieList = new ArrayList<>();
    private int GRID_SIZE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), GRID_SIZE);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MovieGridAdapter(this, movieList, this);
        recyclerView.setAdapter(adapter);

        getMovies();
    }

    public void getMovies() {
        TMDB.getPopularMovies(this, 1, new ResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess: " + response);
                ArrayList<Movie> movies = TMDB.parseResponse(response);
                movieList.addAll(movies);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onSuccess: length: " + movieList.size());
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, "onError: ", error );
            }
        });
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        Movie movie = movieList.get(position);
        Toast.makeText(this, movie.movieTitle, Toast.LENGTH_SHORT).show();
    }
}
