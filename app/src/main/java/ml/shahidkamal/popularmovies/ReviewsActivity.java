package ml.shahidkamal.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;

import ml.shahidkamal.popularmovies.API.ResponseHandler;
import ml.shahidkamal.popularmovies.API.TMDB;
import ml.shahidkamal.popularmovies.Adapters.ReviewsAdapter;
import ml.shahidkamal.popularmovies.Models.Reviews;

public class ReviewsActivity extends AppCompatActivity {
    private static final String TAG = "ReviewsActivity";
    RecyclerView recyclerView ;
    ProgressBar progressBar;
    TextView noReviewView;
    ImageButton closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent intent = getIntent();

        String movieId = intent.getStringExtra("movieId");
        if (movieId == null) finish();

        recyclerView = findViewById(R.id.reviewRecyclerView);
        progressBar = findViewById(R.id.rProgressbar);
        noReviewView = findViewById(R.id.noReviewTextView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TMDB.fetchReviews(this, movieId, new ResponseHandler(){

            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess: " + response);
                ArrayList<Reviews>  reviews = TMDB.parseReviewResponse(response);
                ReviewsAdapter adapter = new ReviewsAdapter(ReviewsActivity.this, reviews);
                recyclerView.setAdapter(adapter);
                hideProgressbar();
                if(reviews.size() == 0) showNoReviewError();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
        ActionBar actionBar = getSupportActionBar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#E640434E"));
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#E640434E"));
            if (actionBar != null) {
                actionBar.setBackgroundDrawable(colorDrawable);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected");
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNoReviewError() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        noReviewView.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
