package id.co.npad93.pm.t7;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

public class Cast {
    @Keep
    public boolean isAdult() {
        return adult;
    }

    @Keep
    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    @Keep
    public int getGender() {
        return gender;
    }

    @Keep
    public void setGender(int gender) {
        this.gender = gender;
    }

    @Keep
    public int getId() {
        return id;
    }

    @Keep
    public void setId(int id) {
        this.id = id;
    }

    @Keep
    public String getName() {
        return name;
    }

    @Keep
    public void setName(String name) {
        this.name = name;
    }

    @Keep
    public String getCharacter() {
        return character;
    }

    @Keep
    public void setCharacter(String character) {
        this.character = character;
    }

    @Keep
    public String getProfilePath() {
        return profilePath;
    }

    @Keep
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    private boolean adult;
    private int gender;
    private int id;
    private String name;
    private String character;
    @SerializedName("profile_path")
    private String profilePath;
}
