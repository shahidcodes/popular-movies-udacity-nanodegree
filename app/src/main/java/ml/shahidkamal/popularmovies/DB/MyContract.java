package ml.shahidkamal.popularmovies.DB;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by shaah on 28-07-2018.
 */

public final class MyContract {

    public static String AUTHORITY = "ml.shahidkamal.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIE = "fav";

    private MyContract() {}

    public static class FavMovies implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "fav";
        public static final String COLUMN_MOVIE_NAME = "movieName";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_OVERVIEW= "overview";
        public static final String COLUMN_MOVIE_VOTE= "vote_avg";
        public static final String COLUMN_MOVIE_POSTER= "poster";
        public static final String COLUMN_MOVIE_RELEASE= "release_date";
        public static final String QUERY_ALL_ITEM =  "SELECT * FROM " + TABLE_NAME;


        static String CREATE_TABLE_FAV = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_MOVIE_NAME + " TEXT, " +
                COLUMN_MOVIE_ID + " TEXT, " +
                COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                COLUMN_MOVIE_VOTE + " TEXT, " +
                COLUMN_MOVIE_RELEASE + " TEXT, " +
                COLUMN_MOVIE_POSTER + " TEXT, UNIQUE(" + COLUMN_MOVIE_ID + ") );";

        static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    }


}