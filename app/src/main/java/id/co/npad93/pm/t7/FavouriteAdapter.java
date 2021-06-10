package id.co.npad93.pm.t7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FavouriteAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    public static void initialize(Context context) {
        if (instance == null) {
            instance = new FavouriteAdapter(context);
        }
    }

    public static FavouriteAdapter getInstance() {
        if (instance == null) {
            throw new IllegalStateException("instance null, forgot to initialize?");
        }

        return instance;
    }

    public boolean isInFavourite(int id) {
        return favouritesLookup.contains(id);
    }

    public boolean add(BasicMovie movie) {
        int id = movie.getId();

        if (!isInFavourite(id)) {
            final ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("adult", movie.isAdult() ? 1 : 0);
            values.put("title", movie.getTitle());
            values.put("original", movie.getOriginalTitle());
            values.put("poster", movie.getPosterPath());

            favourites.add(movie);
            favouritesLookup.add(movie.getId());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    db.insert("favourites", null, values);
                }
            });

            return true;
        }

        return false;
    }

    public boolean remove(BasicMovie movie) {
        int id = movie.getId();
        if (isInFavourite(id)) {
            final String[] idWhere = new String[] {String.valueOf(id)};

            favourites.remove(movie);
            favouritesLookup.remove(id);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    db.delete("favourites", "id = ?", idWhere);
                }
            });

            return true;
        }

        return false;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        MovieViewHolder vh = new MovieViewHolder(view);

        view.setOnClickListener(vh);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(favourites.get(position));
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    private FavouriteAdapter(Context context) {
        if (db == null) {
            db = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath("fav.sqlite3"), null);

            Cursor query = db.query(
                "sqlite_master",
                new String[] {"count(*)"},
                "type='table' and name='favourites'",
                null,
                null,
                null,
                null,
                null
            );
            query.moveToFirst();

            if (query.getInt(0) == 0) {
                db.execSQL("create table 'favourites' (id integer primary key, adult, title, original, poster)");
            }
            query.close();

            query = db.query("favourites", null, null, null, null, null, "id");
            int count = query.getCount();

            executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
            favourites = new SortedList<BasicMovie>(BasicMovie.class, new SortedList.Callback<BasicMovie>() {
                @Override
                public int compare(BasicMovie o1, BasicMovie o2) {
                    return o1.compareTo(o2);
                }

                @Override
                public void onChanged(int position, int count) {
                    FavouriteAdapter.this.notifyItemRangeChanged(position, count);
                }

                @Override
                public boolean areContentsTheSame(BasicMovie oldItem, BasicMovie newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areItemsTheSame(BasicMovie item1, BasicMovie item2) {
                    return item1.getId() == item2.getId();
                }

                @Override
                public void onInserted(int position, int count) {
                    FavouriteAdapter.this.notifyItemRangeInserted(position, count);
                }

                @Override
                public void onRemoved(int position, int count) {
                    FavouriteAdapter.this.notifyItemRangeRemoved(position, count);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    FavouriteAdapter.this.notifyItemMoved(fromPosition, toPosition);
                }
            });
            favouritesLookup = new HashSet<Integer>();

            query.moveToFirst();
            for (int i = 0; i < count; i++) {
                int id = query.getInt(0);

                BasicMovie basicMovie = new BasicMovie();
                basicMovie.setId(id);
                basicMovie.setAdult(query.getInt(1) == 1);
                basicMovie.setTitle(query.getString(2));
                basicMovie.setOriginalTitle(query.getString(3));
                basicMovie.setPosterPath(query.getString(4));

                favourites.add(basicMovie);
                favouritesLookup.add(id);
                query.moveToNext();
            }

            query.close();
        }
    }

    private SQLiteDatabase db;
    private SortedList<BasicMovie> favourites;
    private HashSet<Integer> favouritesLookup;
    private ThreadPoolExecutor executor;

    private static FavouriteAdapter instance;
}
