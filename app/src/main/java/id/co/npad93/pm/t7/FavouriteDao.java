package id.co.npad93.pm.t7;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouriteDao {
    @Query("select * from movie")
    BasicMovie[] getAll();
    @Query("SELECT * FROM movie WHERE id IN (:ids)")
    List<BasicMovie> getByIds(int... ids);

    @Insert
    void insert(BasicMovie... movies);
    @Delete
    void delete(BasicMovie movie);
}
