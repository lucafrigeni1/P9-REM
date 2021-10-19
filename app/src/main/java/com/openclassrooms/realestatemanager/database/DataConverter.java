package com.openclassrooms.realestatemanager.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.models.RoomsPhotos;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    @TypeConverter
    public String fromRoomsPhotosList(List<RoomsPhotos> roomsPhotosList) {
        if (roomsPhotosList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<RoomsPhotos>>() {}.getType();
        String json = gson.toJson(roomsPhotosList, type);
        return json;
    }

    @TypeConverter
    public List<RoomsPhotos> toRoomsPhotosList(String roomsPhotosString) {
        if (roomsPhotosString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<RoomsPhotos>>() {}.getType();
        List<RoomsPhotos> roomsPhotosList = gson.fromJson(roomsPhotosString, type);
        return roomsPhotosList;
    }

    @TypeConverter
    public String fromPOIList(List<String> pointsOfInterestList) {
        if (pointsOfInterestList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(pointsOfInterestList, type);
        return json;
    }

    @TypeConverter
    public List<String> toPOIList(String pointsOfInterestString) {
        if (pointsOfInterestString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> pointsOfInterestList = gson.fromJson(pointsOfInterestString, type);
        return pointsOfInterestList;
    }


}
