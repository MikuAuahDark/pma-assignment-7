package id.co.npad93.pm.t7;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

public class MovieDetail extends BasicMovie {
    @Keep
    public String getBackdropPath() {
        return backdropPath;
    }

    @Keep
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Keep
    public Genre[] getGenres() {
        return genres;
    }

    @Keep
    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }

    @Keep
    public String getSynopsis() {
        return synopsis;
    }

    @Keep
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Keep
    public String getReleaseDate() {
        return releaseDate;
    }

    @Keep
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Keep
    public double getVoteAverage() {
        return voteAverage;
    }

    @Keep
    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Keep
    public int getVoteCount() {
        return voteCount;
    }

    @Keep
    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Keep
    public Credits getCredits() {
        return credits;
    }

    @Keep
    public void setCredits(Credits credits) {
        this.credits = credits;
    }

    @SerializedName("backdrop_path")
    private String backdropPath;
    private Genre[] genres;
    @SerializedName("overview")
    private String synopsis;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("vote_count")
    private int voteCount;
    private Credits credits;
}
