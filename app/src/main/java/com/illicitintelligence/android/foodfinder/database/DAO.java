package com.illicitintelligence.android.foodfinder.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.illicitintelligence.android.foodfinder.util.Constants;

import java.util.List;

@Dao
public interface DAO {


    @Insert
    void addFavorite(FavoriteEntity entity);

    @Delete
    void deleteFavorite(FavoriteEntity entity);

    @Query(value = "SELECT * FROM "+ Constants.FAVORITES_TABLE+" ORDER BY "+Constants.DATE)
    List<FavoriteEntity> getFavorites();

    @Update
    void updateEntry(FavoriteEntity entity);
}
