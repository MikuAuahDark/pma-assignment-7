package id.co.npad93.pm.t7;

import com.google.gson.annotations.SerializedName;

public class MovieList {
    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public BasicMovie[] getResults() {
        return results;
    }

    private int page;
    @SerializedName("total_pages")
    private int totalPages;
    private BasicMovie[] results;
}
