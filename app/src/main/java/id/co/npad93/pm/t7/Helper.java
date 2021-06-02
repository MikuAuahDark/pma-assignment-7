package id.co.npad93.pm.t7;

import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Helper {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        return retrofit;
    }

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
            .baseUrl("http://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
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
}
