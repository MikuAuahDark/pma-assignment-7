package id.co.npad93.pm.t7;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouriteDao {
    @Query("select * from favourites")
    Movie[] getAll();
    @Query("SELECT * FROM favourites WHERE id IN (:ids)")
    List<Movie> getByIds(int... ids);

    @Insert
    void insert(Movie... movies);
    @Delete
    void delete(Movie movie);
}
