package com.illicitintelligence.android.foodfinder.database;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteEntity.class},version = 1)
public abstract class FavoritesDatabase extends RoomDatabase {

    public abstract DAO getDAO();
}
