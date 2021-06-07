package id.co.npad93.pm.t7;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Helper {
    public static void init(AssetManager am) {
        // Decrypt token
        ApiToken.initialize(am);

        // Interceptor
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request req = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + ApiToken.getToken())
                    .build();
                return chain.proceed(req);
            }
        }).build();

        // Retrofit
        retrofit = new Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        basicMovieIndex = new ArrayList<ArrayList<BasicMovie>>();
        basicMovieIndex.add(null);
        basicMovieIndex.add(null);
        basicMovieIndex.add(null);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static byte[] readAllBytes(InputStream input) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final byte[] b = new byte[1024];
        int length = 0;

        while ((length = input.read(b)) != -1) {
            os.write(b, 0, length);
        }

        return os.toByteArray();
    }

    public static void loadImage(Context context, String size, String path, ImageView imageView) {
        Glide.with(context).load("https://image.tmdb.org/t/p/" + size + path).into(imageView);
    }

    public static void switchData(MovieFragment f, boolean reload, int kind) {
        if (kind < 0 || kind > 2) {
            throw new IllegalArgumentException("kind out of range");
        }

        ArrayList<BasicMovie> dataset = basicMovieIndex.get(kind);

        if (reload || dataset == null) {
            basicMovieIndex.set(kind, null);
            requestPage(f, 1, kind);
        } else {
            f.loadRecyclerViewDataset(dataset);
        }
    }

    public static void requestPage(MovieFragment f, int page, int kind) {
        TheMovieDBApi api = getRetrofit().create(TheMovieDBApi.class);
        Call<MovieList> call;

        switch (kind) {
            case 0: {
                call = api.getNowPlayingMovies(page);
                break;
            }
            case 1: {
                call = api.getUpcomingMovies(page);
                break;
            }
            case 2: {
                call = api.getPopularMovies(page);
                break;
            }
            default: {
                assert false;
                throw new IllegalArgumentException("kind out of range");
            }
        }

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, retrofit2.Response<MovieList> response) {
                if (response.isSuccessful()) {
                    ArrayList<BasicMovie> basicMovieData = basicMovieIndex.get(kind);

                    if (basicMovieData == null) {
                        basicMovieData = new ArrayList<BasicMovie>();
                        basicMovieIndex.set(kind, basicMovieData);
                    }

                    basicMovieData.addAll(Arrays.asList(response.body().getResults()));
                    f.loadRecyclerViewDataset(basicMovieData);
                } else {
                    Toast.makeText(f.getContext(), "HTTP Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Toast.makeText(f.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private static Retrofit retrofit;
    private static ArrayList<ArrayList<BasicMovie>> basicMovieIndex;
}
