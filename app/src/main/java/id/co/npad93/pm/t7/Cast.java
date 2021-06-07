package id.co.npad93.pm.t7;

import com.google.gson.annotations.SerializedName;

public class Cast {
    public boolean adult;
    public int gender;
    public int id;
    public String name;
    public String character;
    @SerializedName("profile_path")
    public String profilePath;
}
