package id.co.npad93.pm.t7;

import androidx.annotation.Keep;

public class Genre {
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

    private int id;
    private String name;
}
