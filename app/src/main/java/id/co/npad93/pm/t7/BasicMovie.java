package id.co.npad93.pm.t7;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movie")
public class BasicMovie {
    public boolean adult;
    @PrimaryKey
    public int id;
    public String title;
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("poster_path")
    public String posterPath;
}
