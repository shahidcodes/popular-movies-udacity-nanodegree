package ml.shahidkamal.popularmovies.Models;

import java.io.Serializable;

/**
 * Created by shaah on 07-06-2018.
 */

public class Movie implements Serializable{
    public String overview;
    public String releaseDate;
    public String orignalTitle;
    public String movieTitle;
    public String poster;
    public String voteAverage;
    public String[] genreIds;

    public Movie(String orignalTitle, String movieTitle, String overview, String releaseDate, String poster, String voteAverage, String[] genreIds) {
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.orignalTitle = orignalTitle;
        this.movieTitle = movieTitle;
        this.poster = poster;
        this.voteAverage = voteAverage;
        this.genreIds = genreIds;
    }
}
