package com.arifgolgeci.haritalarseyahat.roomDb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.arifgolgeci.haritalarseyahat.model.Place;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM Place")
    Flowable<List<Place>> getAll();

    @Query("SELECT * FROM Place WHERE listId = :listId")
    Flowable<List<Place>> getAllByListId(int listId);

    @Insert
    Completable insert(Place place);

    @Update
    Completable update(Place place);


    @Delete
    Completable delete(Place place);

}
