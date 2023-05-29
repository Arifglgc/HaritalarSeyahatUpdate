package com.arifgolgeci.haritalarseyahat.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
// Serializeble ile sınıflar byte a cevrilerek kaydedilebilir asamaya gelir
@Entity
public class ListPlace implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int uid;

    @ColumnInfo(name = "name ")
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    public ListPlace(String name){
        this.name=name;
    }
}
