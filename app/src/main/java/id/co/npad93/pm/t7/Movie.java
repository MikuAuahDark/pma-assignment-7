package id.co.npad93.pm.t7;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "favourites")
public class Movie implements Parcelable {
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

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            Movie movie = new Movie();
            movie.adult = source.readByte() == 1;
            movie.backdropPath = source.readString();
            movie.genreIds = new int[source.readInt()];
            source.readIntArray(movie.genreIds);
            movie.id = source.readInt();
            movie.synopsis = source.readString();
            movie.originalTitle = source.readString();
            movie.posterPath = source.readString();
            movie.releaseDate = source.readString();
            movie.title = source.readString();
            movie.voteAverage = source.readDouble();
            movie.voteCount = source.readInt();

            return movie;
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(backdropPath);
        dest.writeInt(genreIds.length);
        dest.writeIntArray(genreIds);
        dest.writeInt(id);
        dest.writeString(synopsis);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
        dest.writeInt(voteCount);
    }
}
