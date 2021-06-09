package id.co.npad93.pm.t7;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class BasicMovie implements Comparable<BasicMovie> {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int compareTo(BasicMovie o) {
        return id - o.id;
    }

    public boolean equals(@Nullable BasicMovie obj) {
        return
            obj != null &&
            id == obj.id &&
            adult == obj.adult &&
            cmpObject(title, obj.title) &&
            cmpObject(originalTitle, obj.originalTitle) &&
            cmpObject(posterPath, obj.posterPath);
    }

    private static boolean cmpObject(@Nullable Object o1, @Nullable Object o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 == null || o2 == null) {
            return false;
        } else {
            return o1.equals(o2);
        }
    }

    private int id;
    private boolean adult;
    private String title;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("poster_path")
    private String posterPath;
}
