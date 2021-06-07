package id.co.npad93.pm.t7;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

public class MovieDetail extends BasicMovie {
    @SerializedName("backdrop_path")
    public String backdropPath;
    public Genre[] genres;
    @SerializedName("overview")
    public String synopsis;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("vote_average")
    public double voteAverage;
    @SerializedName("vote_count")
    public int voteCount;
    public Credits credits;
}
