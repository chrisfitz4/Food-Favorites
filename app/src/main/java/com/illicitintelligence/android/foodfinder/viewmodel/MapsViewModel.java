package com.illicitintelligence.android.foodfinder.viewmodel;

import android.app.Application;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.illicitintelligence.android.foodfinder.database.FavoriteEntity;
import com.illicitintelligence.android.foodfinder.database.FavoritesDatabase;
import com.illicitintelligence.android.foodfinder.model.FoodFound;
import com.illicitintelligence.android.foodfinder.network.RetrofitMapsInstance;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsViewModel extends AndroidViewModel {

    private final String TAG = "TAG_X";
    private RetrofitMapsInstance retrofitMapsInstance = new RetrofitMapsInstance();
    private MutableLiveData<FoodFound> foodFoundMutableLiveData = new MutableLiveData<>();
    private FavoritesDatabase database;
    private List<FavoriteEntity> favorites;

    public MapsViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "MapsViewModel: constructed");
        database = Room.databaseBuilder(this.getApplication(),FavoritesDatabase.class,"database")
                .allowMainThreadQueries()
                .build();
        favorites = database.getDAO().getFavorites();
    }

    public void searchFood(String location, String foodType){
        retrofitMapsInstance.searchFood(location,foodType).enqueue(new Callback<FoodFound>() {
            @Override
            public void onResponse(Call<FoodFound> call, Response<FoodFound> response) {
                foodFoundMutableLiveData.setValue(response.body());
                Log.d("TAG_X", "onResponse error: "+response.body().getErrorMessage());
                Log.d("TAG_X", "onResponse status: "+response.body().getStatus());
            }

            @Override
            public void onFailure(Call<FoodFound> call, Throwable t) {
                Log.d("TAG_X", "onFailure: "+t.getMessage());
            }
        });
    }



    public MutableLiveData<FoodFound> getFoodFoundMutableLiveData(){
        return foodFoundMutableLiveData;
    }

    public List<FavoriteEntity> getFavorites(){
        return favorites;
    }

    public void addFavorite(FavoriteEntity entity){
        //database.getDAO().addFavorite(entity);
        boolean exists = false;
        int id = entity.getId();
        favorites.add(0,entity);
        int i = 1;
        while(i<favorites.size()){
            if(id==favorites.get(i).getId()){
                favorites.remove(i);
                exists = true;
            }
            i++;
        }
        if(exists){
            database.getDAO().updateEntry(entity);
        }else{
            database.getDAO().addFavorite(entity);
        }
    }

    public void deleteFavorite(FavoriteEntity entity){
        database.getDAO().deleteFavorite(entity);
        favorites.remove(entity);
    }

    public void deleteFavorite(int position){
        FavoriteEntity entity = favorites.remove(position);
        deleteFavorite(entity);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        database.close();
    }
}
