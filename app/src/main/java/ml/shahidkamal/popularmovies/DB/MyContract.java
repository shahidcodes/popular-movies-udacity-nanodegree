package ml.shahidkamal.popularmovies.DB;

import android.provider.BaseColumns;

/**
 * Created by shaah on 28-07-2018.
 */

final class MyContract {

    private MyContract() {}

    static class FavMovies implements BaseColumns {
        static final String TABLE_NAME = "fav";
        static final String COLUMN_MOVIE_NAME = "movieName";
        static final String COLUMN_MOVIE_ID = "movieId";
        static final String COLUMN_MOVIE_OVERVIEW= "overview";
        static final String COLUMN_MOVIE_VOTE= "vote_avg";
        static final String COLUMN_MOVIE_POSTER= "poster";
        static final String COLUMN_MOVIE_RELEASE= "release_date";
        static final String QUERY_ALL_ITEM =  "SELECT * FROM " + TABLE_NAME;


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