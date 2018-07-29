package ml.shahidkamal.popularmovies.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import ml.shahidkamal.popularmovies.DB.DBHelper;
import ml.shahidkamal.popularmovies.DB.MyContract;
import ml.shahidkamal.popularmovies.Models.Movie;

import static ml.shahidkamal.popularmovies.DB.MyContract.FavMovies.CONTENT_URI;
import static ml.shahidkamal.popularmovies.DB.MyContract.FavMovies.TABLE_NAME;


public class MovieProvider extends ContentProvider {

    private static final String TAG = "MovieProvider";

    public static final int MOVIE = 100;
    public static final UriMatcher sUriMatcher = buildMatcher();

    public static UriMatcher buildMatcher (){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MyContract.AUTHORITY, MyContract.PATH_MOVIE, MOVIE);

        return uriMatcher;
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = DBHelper.getInstance(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        if(match == MOVIE){
           cursor = db.query(
                   TABLE_NAME,
                   null, null, null, null, null, null
           );
        }else{
            throw new UnsupportedOperationException("Unsupported Uri");
        }

        getContext().getContentResolver().notifyChange(uri, null);


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        if(match == MOVIE){
            long id = db.insert(TABLE_NAME, null, contentValues);
            if (id>0){
                returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            }else {
                throw new SQLException("Failed to insert row");
            }
        }else{
            throw  new UnsupportedOperationException("Unknown URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    public static ArrayList<Movie> getMovieListFromCursor(Cursor cursor) {
        ArrayList<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()){
            String movieName = cursor.getString(cursor.getColumnIndex(MyContract.FavMovies.COLUMN_MOVIE_NAME));
            String movieId = cursor.getString(cursor.getColumnIndex(MyContract.FavMovies.COLUMN_MOVIE_ID));
            Log.d(TAG, "getFavMovies: movieId" + movieId);
            String overview = cursor.getString(cursor.getColumnIndex(MyContract.FavMovies.COLUMN_MOVIE_OVERVIEW));
            String poster = cursor.getString(cursor.getColumnIndex(MyContract.FavMovies.COLUMN_MOVIE_POSTER));
            String voteAverage = cursor.getString(cursor.getColumnIndex(MyContract.FavMovies.COLUMN_MOVIE_VOTE));
            String releaseDate = cursor.getString(cursor.getColumnIndex(MyContract.FavMovies.COLUMN_MOVIE_RELEASE));
            Movie movie = new Movie(movieName, overview, releaseDate, poster, voteAverage, movieId);
            movies.add(movie);
        }
        return movies;
    }
}
