package com.arifgolgeci.haritalarseyahat.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity//(foreignKeys = @ForeignKey(entity = ListPlace.class,
        //parentColumns = "id",
       //childColumns = "listId",
        //onUpdate = ForeignKey.CASCADE,
        //onDelete = ForeignKey.CASCADE))
public class Place implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name ")
    public String  name;

    @ColumnInfo(name="latitude")
    public Double latitude;


    @ColumnInfo(name="longitude")
    public Double longitude;

   @ColumnInfo(name="listId",index = true)
    public int listId;


    public Place(String name, Double latitude, Double longitude, int listId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.listId = listId;
    }


    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public int getListId() {
        return listId;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        return "Place{id=" + id + ", name='" + name + "', latitude=" + latitude + ", longitude=" + longitude + ", listId=" + listId + "}";
    }

}
