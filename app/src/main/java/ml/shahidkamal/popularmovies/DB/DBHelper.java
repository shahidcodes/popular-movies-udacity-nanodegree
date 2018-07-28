package ml.shahidkamal.popularmovies.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ml.shahidkamal.popularmovies.MainActivity;
import ml.shahidkamal.popularmovies.Models.Movie;

/**
 * Created by shaah on 28-07-2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PopularMovies.db";
    public static DBHelper mInstance;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyContract.FavMovies.CREATE_TABLE_FAV);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyContract.FavMovies.DELETE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static DBHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new DBHelper(context);
        }

        return mInstance;
    }

    public static boolean addToFav(Context context, Movie movie){
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MyContract.FavMovies.COLUMN_MOVIE_NAME, movie.orignalTitle);
        values.put(MyContract.FavMovies.COLUMN_MOVIE_ID, movie.movieId);
        values.put(MyContract.FavMovies.COLUMN_MOVIE_OVERVIEW, movie.overview);
        values.put(MyContract.FavMovies.COLUMN_MOVIE_POSTER, movie.poster);
        values.put(MyContract.FavMovies.COLUMN_MOVIE_VOTE, movie.voteAverage);
        values.put(MyContract.FavMovies.COLUMN_MOVIE_RELEASE, movie.releaseDate);
        long rowId = db.insert(MyContract.FavMovies.TABLE_NAME, null, values);
        return rowId != -1;

    }

    public static ArrayList<Movie> getFavMovies(Context context) {
        ArrayList<Movie> movies = new ArrayList<>();
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                MyContract.FavMovies.TABLE_NAME,
                null, null, null, null, null, null);

        Log.d(TAG, "getFavMovies: DB: " + cursor.getCount());
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
        cursor.close();
        db.close();

        return movies;
    }

    public static boolean isAlreadyFav(Context context, String movieId) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {MyContract.FavMovies.COLUMN_MOVIE_ID};
        String selection = MyContract.FavMovies.COLUMN_MOVIE_ID +  " = ?";
        String [] selectionParam = { movieId };
        Cursor cursor = db.query(
                MyContract.FavMovies.TABLE_NAME,
                projection,
                selection,
                selectionParam,
                null, null, null
        );
        return cursor.getCount() != 0;
    }
}
