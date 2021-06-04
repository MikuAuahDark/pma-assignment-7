package id.co.npad93.pm.t7;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "favourites")
public class Movie {
    public boolean adult;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("genre_ids")
    public int[] genreIds;
    @PrimaryKey
    public int id;
    @SerializedName("overview")
    public String synopsis;
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("release_date")
    public String releaseDate;
    public String title;
    @SerializedName("vote_average")
    public double voteAverage;
    @SerializedName("vote_count")
    public int voteCount;
}
