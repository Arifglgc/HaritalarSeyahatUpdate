package com.arifgolgeci.haritalarseyahat.roomDb;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.arifgolgeci.haritalarseyahat.model.ListPlace;
import com.arifgolgeci.haritalarseyahat.model.Place;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
@Dao
public interface ListsDao {
    @Query("SELECT * FROM ListPlace")
    Flowable<List<ListPlace>> getAll();




    @Insert(onConflict = REPLACE)
    Completable insert(ListPlace listPlace);

    @Update
    Completable update(ListPlace listPlace);
    @Delete
    Completable delete(ListPlace listPlace);
}
