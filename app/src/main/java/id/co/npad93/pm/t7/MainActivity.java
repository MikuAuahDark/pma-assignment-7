package id.co.npad93.pm.t7;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize
        Api.initialize(getAssets());
        FavouriteAdapter.initialize(this);

        // Initialize fragment
        movieFragment = new MovieFragment();
        favoriteFragment = new FavouriteFragment();

        // Initialize menu title
        menuTitle = new HashMap<Integer, String>();
        menuTitle.put(R.id.menuItem, "Now Playing");
        menuTitle.put(R.id.menuItem2, "Upcoming");
        menuTitle.put(R.id.menuItem3, "Popular");
        menuTitle.put(R.id.menuItem4, "Favourite");

        // Initialize view
        setContentView(R.layout.activity_main);
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.menuItem);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!firstStart) {
            movieFragment.setDataKind(0);
            firstStart = true;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuItem4) {
            replaceFragment(favoriteFragment);
        } else {
            replaceFragment(movieFragment);

            if (id == R.id.menuItem) {
                movieFragment.setDataKind(0);
            } else if (id == R.id.menuItem2) {
                movieFragment.setDataKind(1);
            } else if (id == R.id.menuItem3) {
                movieFragment.setDataKind(2);
            }
        }

        String title = menuTitle.get(id);
        if (title != null) {
            getSupportActionBar().setTitle(title);
        }

        return true;
    }

    private void replaceFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, f).commit();
    }

    private MovieFragment movieFragment;
    private FavouriteFragment favoriteFragment;

    private HashMap<Integer, String> menuTitle;
    private boolean firstStart;
}
