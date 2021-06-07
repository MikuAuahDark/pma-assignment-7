package id.co.npad93.pm.t7;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBApi {
    @GET("movie/now_playing")
    Call<MovieList> getNowPlayingMovies(
        @Query("page") int page
    );

    @GET("movie/popular")
    Call<MovieList> getPopularMovies(
        @Query("page") int page
    );

    @GET("movie/upcoming")
    Call<MovieList> getUpcomingMovies(
        @Query("page") int page
    );

    @GET("movie/{id}?append_to_response=credits")
    Call<MovieDetail> getMovie(
        @Path("id") int id
    );
}
