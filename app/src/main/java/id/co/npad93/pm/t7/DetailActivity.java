package id.co.npad93.pm.t7;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get movie ID
        Intent i = getIntent();
        if (i == null) {
            Toast.makeText(this, "Missing intent!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int id = i.getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(this, "Missing movie id!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        root = findViewById(R.id.scrollView);
        backdrop = findViewById(R.id.imageView2);
        poster = findViewById(R.id.imageView3);
        title = findViewById(R.id.textView4);
        originalTitle = findViewById(R.id.textView5);
        releaseDate = findViewById(R.id.textView10);
        genre = findViewById(R.id.textView11);
        ratingText = findViewById(R.id.textView18);
        synopsis = findViewById(R.id.textView13);
        favourite = findViewById(R.id.button);
        votes = findViewById(R.id.ratingBar);
        casts = findViewById(R.id.recyclerView3);

        // Support action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }

        // Adapter
        adapter = FavouriteAdapter.getInstance();

        TheMovieDBApi api = Api.getApi();
        Call<MovieDetail> movie = currentCall = api.getMovie(id);
        movie.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful()) {
                    MovieDetail result = data = response.body();

                    // Load values
                    casts.setAdapter(new CastAdapter(result.getCredits().getCasts()));
                    votes.setRating((float) (result.getVoteAverage() / 2.0));
                    synopsis.setText(result.getSynopsis());
                    ratingText.setText(String.format(Locale.US,
                        "(%.2g/10) %d rates",
                        result.getVoteAverage(),
                        result.getVoteCount()
                    ));
                    originalTitle.setText(result.getOriginalTitle());
                    title.setText(result.getTitle());

                    // Favourites
                    if (adapter.isInFavourite(data.getId())) {
                        Drawable icon = ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_baseline_favorite_24,
                            getTheme()
                        );
                        favourite.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                    }

                    favourite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Drawable icon;

                            if (adapter.isInFavourite(data.getId())) {
                                icon = ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.ic_baseline_favorite_border_24,
                                    getTheme()
                                );

                                // Unfavourite
                                adapter.remove(data);
                            } else {
                                icon = ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.ic_baseline_favorite_24,
                                    getTheme()
                                );

                                // Favourite
                                adapter.add(data);
                            }

                            favourite.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                        }
                    });

                    // Load image
                    Api.loadImage(DetailActivity.this, "w1280", result.getBackdropPath(), backdrop);
                    Api.loadImage(DetailActivity.this, "w500", result.getPosterPath(), poster);

                    // Set action bar title
                    if (actionBar != null) {
                        actionBar.setTitle(result.getTitle());
                    }

                    // Build genre
                    Genre[] genres = result.getGenres();
                    String[] genresConcat = new String[genres.length];
                    for (int i = 0; i < genres.length; i++) {
                        genresConcat[i] = genres[i].getName();
                    }
                    genre.setText(TextUtils.join(", ", genresConcat));

                    // Release date ISO 8601 to human readable
                    try {
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = fmt.parse(result.getReleaseDate());
                        releaseDate.setText(DateFormat.getDateInstance().format(date));
                    } catch (ParseException e) {
                        Log.e("DetailActivity", "ISO 8601 date parse failed???", e);
                        releaseDate.setText(result.getReleaseDate());
                    }

                    // Display
                    root.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(DetailActivity.this, "HTTP Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                currentCall = null;
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                currentCall = null;
                Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (currentCall != null) {
            currentCall.cancel();
        }

        casts.setAdapter(null);

        super.onDestroy();
    }

    private NestedScrollView root;
    private ImageView poster, backdrop;
    private TextView title, originalTitle, releaseDate, genre, ratingText, synopsis;
    private Button favourite;
    private RatingBar votes;
    private RecyclerView casts;

    private MovieDetail data;
    private FavouriteAdapter adapter;
    private Call<MovieDetail> currentCall;
}
