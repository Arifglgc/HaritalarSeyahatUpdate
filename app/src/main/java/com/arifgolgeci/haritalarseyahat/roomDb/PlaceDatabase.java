package com.arifgolgeci.haritalarseyahat.roomDb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.arifgolgeci.haritalarseyahat.model.ListPlace;
import com.arifgolgeci.haritalarseyahat.model.Place;


@Database(entities = {Place.class, ListPlace.class},version=5)
public abstract class PlaceDatabase extends RoomDatabase {
    public abstract  PlaceDao placeDao();
    public abstract  ListsDao listsDao();


}
