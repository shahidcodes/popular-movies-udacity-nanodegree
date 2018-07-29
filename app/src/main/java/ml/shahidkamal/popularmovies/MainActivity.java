package ml.shahidkamal.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;

import ml.shahidkamal.popularmovies.API.ResponseHandler;
import ml.shahidkamal.popularmovies.API.TMDB;
import ml.shahidkamal.popularmovies.Adapters.EndlessScrollListener;
import ml.shahidkamal.popularmovies.Adapters.MovieGridAdapter;
import ml.shahidkamal.popularmovies.ContentProvider.MovieProvider;
import ml.shahidkamal.popularmovies.DB.DBHelper;
import ml.shahidkamal.popularmovies.Models.Movie;

import static ml.shahidkamal.popularmovies.DB.MyContract.FavMovies.CONTENT_URI;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    final int TYPE_POPULAR = 121;
    final int TYPE_RATED = 122;
    final int TYPE_FAV = 123;
    int CURRENT_TYPE = TYPE_POPULAR;

    RecyclerView recyclerView;
    ProgressBar progressBar, loadMoreProgressbar;
    MovieGridAdapter adapter;
    ArrayList<Movie> movieList = new ArrayList<>();
    EndlessScrollListener endlessScrollListener;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        loadMoreProgressbar = findViewById(R.id.pb_load_more);
        findViewById(R.id.btn_filter).setOnClickListener(filterBtnOnClickListener);

        int GRID_SIZE = 2;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), GRID_SIZE);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MovieGridAdapter(this, movieList, recyclerViewClickListener);
        recyclerView.setAdapter(adapter);
        endlessScrollListener = new EndlessScrollListener() {
            @Override
            public void onLoadMore() {
                page++;
                loadMoreProgressbar.setVisibility(View.VISIBLE);
                Log.d(TAG, "onLoadMore: page" + page + " type " + CURRENT_TYPE);
                if (CURRENT_TYPE == TYPE_POPULAR)
                    getPopularMovies(page);
                else if (CURRENT_TYPE == TYPE_RATED) getHighestRatedMovies(page);
            }
        };
        recyclerView.addOnScrollListener(endlessScrollListener);
        if (isNetworkAvailable())
            getPopularMovies(page);
        else getFavMovies();
    }

    public void getPopularMovies(final int page) {
        TMDB.getPopularMovies(this, page, new ResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "Popular: " + response);
                ArrayList<Movie> movies = TMDB.parseResponse(response);
                if (page == 1) movieList.clear();
                movieList.addAll(movies);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Popular: length: " + movieList.size());
                hideProgressbar();
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, "onError: " + error.getMessage(), error);
                hideProgressbar();
            }
        });
    }

    private void getFavMovies() {
        ArrayList<Movie> movies;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null, null);
            movies = MovieProvider.getMovieListFromCursor(cursor);
        }else{
            movies = DBHelper.getFavMovies(this);
        }

        Log.d(TAG, "getFavMovies: " + movies.size());
        movieList.clear();
        movieList.addAll(movies);
        adapter.notifyDataSetChanged();
        hideProgressbar();
    }

    private void getHighestRatedMovies(final int page) {
        TMDB.getHighestRatedMovies(this, page, new ResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess: High Rated " + response);
                ArrayList<Movie> movies = TMDB.parseResponse(response);
                Log.d(TAG, "onSuccess: High Rated " + movies.size());
                if (page == 1) movieList.clear();
                movieList.addAll(movies);
                adapter.notifyDataSetChanged();
                hideProgressbar();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(MainActivity.this, "Can't fetch movies", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onError: " + error.getMessage(), error);
                hideProgressbar();
            }
        });
    }

    private void changeProgressbarVisiblity(Boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            loadMoreProgressbar.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }


    private void showProgressbar() {
        Log.d(TAG, "showProgressbar");
        changeProgressbarVisiblity(true);
    }

    private void hideProgressbar() {
        Log.d(TAG, "hideProgressbar");
        changeProgressbarVisiblity(false);
    }

    View.OnClickListener recyclerViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = recyclerView.getChildAdapterPosition(view);
            Movie movie = movieList.get(position);
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("movie", movie);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };


    View.OnClickListener filterBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isNetworkAvailable()) showFilterDialog();
            else Toast.makeText(MainActivity.this, "Bro! I need internet connection", Toast.LENGTH_SHORT).show();
        }
    };

    private void showFilterDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.filter_dialog_layout, null, false);

        final Spinner sortSpinner = view.findViewById(R.id.filter_spinner);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final DialogInterface dialogInterface = dialog.setTitle("Filter")
                .setView(view)
                .show();

        view.findViewById(R.id.sort_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                endlessScrollListener.resetPreviousItemCache();
                String selectedItem = String.valueOf(sortSpinner.getSelectedItem());
                Log.d(TAG, "Sort Btn : " + selectedItem);
                showProgressbar();
                if ("Highest Rated".equals(selectedItem)) {
                    CURRENT_TYPE = TYPE_RATED;
                    getHighestRatedMovies(page);
                } else if ("Favourite Movies".equals(selectedItem)) {
                    CURRENT_TYPE = TYPE_FAV;
                    getFavMovies();
                } else {
                    CURRENT_TYPE = TYPE_POPULAR;
                    getPopularMovies(page);
                }
                dialogInterface.dismiss();

            }
        });


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
