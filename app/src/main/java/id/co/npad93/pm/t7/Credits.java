package id.co.npad93.pm.t7;

import com.google.gson.annotations.SerializedName;

public class Credits {
    public Cast[] getCasts() {
        return casts;
    }

    public void setCasts(Cast[] casts) {
        this.casts = casts;
    }

    @SerializedName("cast")
    private Cast[] casts;
}
