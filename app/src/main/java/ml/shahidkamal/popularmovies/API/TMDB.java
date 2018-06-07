package ml.shahidkamal.popularmovies.API;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ml.shahidkamal.popularmovies.Models.Movie;
import ml.shahidkamal.popularmovies.R;

/**
 * Created by shaah on 07-06-2018.
 */

public class TMDB {
    private static final String TAG = "TMDB";
    private static final String POST_HOST = "http://image.tmdb.org/t/p/w185/";

    public static void sendGet(Context ctx, String url, final ResponseHandler handler) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handler.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handler.onError(error);
                    }
                }
        );

        VolleySingleton.getInstance(ctx).addToRequestQueue(request);
    }

    public static void getPopularMovies(Context context, int page, ResponseHandler handler) {
        Uri.Builder query = new Uri.Builder();
        query.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3").appendPath("movie").appendPath("popular")
                .appendQueryParameter("api_key", context.getString(R.string.tmdb_api))
                .appendQueryParameter("language", "en-US")
                .appendQueryParameter("page", String.valueOf(page))
                .build();

        String url = query.toString();
        sendGet(context, url, handler);
    }

    public static ArrayList<Movie> parseResponse(JSONObject response) {
        ArrayList<Movie> moviesList = new ArrayList<>();
        try{
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJSONObject = (JSONObject) results.get(i);

                String[] genreIds = {};

                Movie movie = new Movie(
                        movieJSONObject.getString("original_title"),
                        movieJSONObject.getString("title"),
                        movieJSONObject.getString("overview"),
                        movieJSONObject.getString("release_date"),
                        TMDB.POST_HOST + movieJSONObject.getString("poster_path"),
                        movieJSONObject.getString("vote_average"),
                        genreIds
                );

                moviesList.add(movie);

            }
        }catch (JSONException error){
            Log.e(TAG, "parseResponse: can't parse", error );
        }
        return moviesList;
    }
}
