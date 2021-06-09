package id.co.npad93.pm.t7;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

public class Credits {
    @Keep
    public Cast[] getCasts() {
        return casts;
    }

    @Keep
    public void setCasts(Cast[] casts) {
        this.casts = casts;
    }

    @SerializedName("cast")
    private Cast[] casts;
}
