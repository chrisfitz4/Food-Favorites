package com.illicitintelligence.android.foodfinder.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.illicitintelligence.android.foodfinder.util.Constants;

import java.util.Calendar;

@Entity(tableName = Constants.FAVORITES_TABLE)
public class FavoriteEntity implements Parcelable {

    @PrimaryKey
    private int id;
    @ColumnInfo(name = Constants.RESTAURANT_NAME)
    private String restaurantName;
    @ColumnInfo(name = Constants.IMAGE_URL)
    private String imageURL;
    @ColumnInfo(name = Constants.DATE)
    private long visitedOn;
    @ColumnInfo(name = Constants.LOCATION)
    private String location;

    public FavoriteEntity(String restaurantName, String imageURL, long visitedOn, String location) {
        this.id = (restaurantName+location).hashCode();
        this.restaurantName = restaurantName;
        this.imageURL = imageURL;
        this.visitedOn = visitedOn;
        this.location = location;
    }


    /////////////////////getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantTitle) {
        this.restaurantName = restaurantTitle;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getVisitedOn() {
        return visitedOn;
    }

    public void setVisitedOn(long visited) {
        this.visitedOn = visited;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //////////////////////toSting and comparable


    @NonNull
    @Override
    public String toString() {
        return restaurantName;
    }

    public String dateToString(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(visitedOn);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        long current = System.currentTimeMillis();
        calendar.setTimeInMillis(current);
        int currentYear = calendar.get(Calendar.YEAR);
        if(currentYear==year){
            return month+"-"+day;
        }else{
            return month+"-"+day+"-"+year;
        }
    }





/////////////////Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(restaurantName);
        dest.writeString(imageURL);
        dest.writeLong(visitedOn);
        dest.writeString(location);
    }

    protected FavoriteEntity(Parcel in) {
        id = in.readInt();
        restaurantName = in.readString();
        imageURL = in.readString();
        visitedOn = in.readLong();
        location = in.readString();
    }

    public static final Creator<FavoriteEntity> CREATOR = new Creator<FavoriteEntity>() {
        @Override
        public FavoriteEntity createFromParcel(Parcel in) {
            return new FavoriteEntity(in);
        }

        @Override
        public FavoriteEntity[] newArray(int size) {
            return new FavoriteEntity[size];
        }
    };


}
