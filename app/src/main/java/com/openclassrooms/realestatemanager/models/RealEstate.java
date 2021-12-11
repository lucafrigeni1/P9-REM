package com.openclassrooms.realestatemanager.models;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity(tableName = "realEstate")
public class RealEstate {

    @PrimaryKey()
    @NonNull
    String id;
    String type, descriptions;
    String mainPhoto;
    int dollarPrice, euroPrice;
    boolean isSold;
    double surface;
    List<RoomsPhotos> roomsPhotosList;
    int rooms, bathrooms, bedrooms;
    List<String> pointsOfInterest;
    String street, city, postalCode, country;
    double latitude, longitude;
    String recordDate, saleDate;
    String estateAgent;

    public RealEstate(@NotNull String id,
                      String type,
                      String descriptions,
                      String mainPhoto,
                      int dollarPrice,
                      int euroPrice,
                      boolean isSold,
                      double surface,
                      List<RoomsPhotos> roomsPhotosList,
                      int rooms,
                      int bathrooms,
                      int bedrooms,
                      List<String> pointsOfInterest,
                      String street,
                      String city,
                      String postalCode,
                      String country,
                      double latitude,
                      double longitude,
                      String recordDate,
                      String saleDate,
                      String estateAgent) {
        this.id = id;
        this.type = type;
        this.descriptions = descriptions;
        this.mainPhoto = mainPhoto;
        this.dollarPrice = dollarPrice;
        this.euroPrice = euroPrice;
        this.isSold = isSold;
        this.surface = surface;
        this.roomsPhotosList = roomsPhotosList;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.bedrooms = bedrooms;
        this.pointsOfInterest = pointsOfInterest;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.recordDate = recordDate;
        this.saleDate = saleDate;
        this.estateAgent = estateAgent;
    }

    public RealEstate(@NotNull String id){
        this.id = id;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public int getDollarPrice() {
        return dollarPrice;
    }

    public void setDollarPrice(int dollarPrice) {
        this.dollarPrice = dollarPrice;
    }

    public int getEuroPrice() {
        return euroPrice;
    }

    public void setEuroPrice(int euroPrice) {
        this.euroPrice = euroPrice;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public List<RoomsPhotos> getRoomsPhotosList() {
        return roomsPhotosList;
    }

    public void setRoomsPhotosList(List<RoomsPhotos> roomsPhotosList) {
        this.roomsPhotosList = roomsPhotosList;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public List<String> getPointsOfInterest() {
        return pointsOfInterest;
    }

    public void setPointsOfInterest(List<String> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public String getEstateAgent() {
        return estateAgent;
    }

    public void setEstateAgent(String estateAgent) {
        this.estateAgent = estateAgent;
    }

    public static class Builder {
        String id;
        String type, descriptions;
        String mainPhoto;
        int dollarPrice;
        int euroPrice;
        boolean isSold;
        double surface;
        List<RoomsPhotos> roomsPhotosList;
        int rooms, bathrooms, bedrooms;
        List<String> pointsOfInterest;
        String street, city, postalCode, country;
        double latitude, longitude;
        String recordDate, saleDate;
        String estateAgent;

        public Builder(String id) {
            this.id = id;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder descriptions(String descriptions) {
            this.descriptions = descriptions;
            return this;
        }

        public Builder mainPhoto(String mainPhoto) {
            this.mainPhoto = mainPhoto;
            return this;
        }

        public Builder dollarPrice(int dollarPrice) {
            this.dollarPrice = dollarPrice;
            return this;
        }

        public Builder euroPrice(int euroPrice) {
            this.euroPrice = euroPrice;
            return this;
        }

        public Builder isSold(Boolean isSold) {
            this.isSold = isSold;
            return this;
        }

        public Builder surface(double surface) {
            this.surface = surface;
            return this;
        }

        public Builder roomsPhotosList(List<RoomsPhotos> roomsPhotosList) {
            this.roomsPhotosList = roomsPhotosList;
            return this;
        }

        public Builder rooms(int rooms) {
            this.rooms = rooms;
            return this;
        }

        public Builder bathrooms(int bathrooms) {
            this.bathrooms = bathrooms;
            return this;
        }

        public Builder bedrooms(int bedrooms) {
            this.bedrooms = bedrooms;
            return this;
        }

        public Builder pointsOfInterest(List<String> pointsOfInterest) {
            this.pointsOfInterest = pointsOfInterest;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder recordDate(String recordDate) {
            this.recordDate = recordDate;
            return this;
        }

        public Builder saleDate(String saleDate) {
            this.saleDate = saleDate;
            return this;
        }

        public Builder estateAgent(String estateAgent) {
            this.estateAgent = estateAgent;
            return this;
        }

        public RealEstate build() {
            RealEstate realEstate = new RealEstate();
            realEstate.id = this.id;
            realEstate.type = this.type;
            realEstate.descriptions = this.descriptions;
            realEstate.mainPhoto = this.mainPhoto;
            realEstate.dollarPrice = this.dollarPrice;
            realEstate.euroPrice = this.euroPrice;
            realEstate.isSold = this.isSold;
            realEstate.surface = this.surface;
            realEstate.roomsPhotosList = this.roomsPhotosList;
            realEstate.rooms = this.rooms;
            realEstate.bathrooms = this.bathrooms;
            realEstate.bedrooms = this.bedrooms;
            realEstate.pointsOfInterest = this.pointsOfInterest;
            realEstate.street = this.street;
            realEstate.city = this.city;
            realEstate.postalCode = this.postalCode;
            realEstate.country = this.country;
            realEstate.latitude = this.latitude;
            realEstate.longitude = this.longitude;
            realEstate.recordDate = this.recordDate;
            realEstate.saleDate = this.saleDate;
            realEstate.estateAgent = this.estateAgent;
            return realEstate;
        }
    }

    public RealEstate() {}

    public static class PriceComparator implements Comparator<RealEstate> {
        @Override
        public int compare(RealEstate o1, RealEstate o2) {
            return (o1.getDollarPrice() - o2.getDollarPrice());
        }
    }

    public static class SurfaceComparator implements Comparator<RealEstate> {
        @Override
        public int compare(RealEstate o1, RealEstate o2) {
            return (int) (o1.getSurface() - o2.getSurface());
        }
    }

    public static class RoomsComparator implements Comparator<RealEstate> {
        @Override
        public int compare(RealEstate o1, RealEstate o2) {
            return (o1.getRooms() - o2.getRooms());
        }
    }

    public static class BathRoomsComparator implements Comparator<RealEstate> {
        @Override
        public int compare(RealEstate o1, RealEstate o2) {
            return (o1.getBathrooms() - o2.getBathrooms());
        }
    }

    public static class BedRoomsComparator implements Comparator<RealEstate> {
        @Override
        public int compare(RealEstate o1, RealEstate o2) {
            return (o1.getBedrooms() - o2.getBedrooms());
        }
    }

    public static RealEstate fromContentValues(ContentValues value){
        final RealEstate realEstate = new RealEstate();
        if (value.containsKey("id")) realEstate.setId(value.getAsString("id"));
        if (value.containsKey("type")) realEstate.setType(value.getAsString("type"));
        if (value.containsKey("descriptions")) realEstate.setDescriptions(value.getAsString("descriptions"));
        if (value.containsKey("mainPhoto")) realEstate.setMainPhoto(value.getAsString("mainPhoto"));
        if (value.containsKey("dollarPrice")) realEstate.setDollarPrice(value.getAsInteger("dollarPrice"));
        if (value.containsKey("euroPrice")) realEstate.setEuroPrice(value.getAsInteger("euroPrice"));
        if (value.containsKey("isSold")) realEstate.setSold(value.getAsBoolean("isSold"));
        if (value.containsKey("surface")) realEstate.setSurface(value.getAsDouble("surface"));
        if (value.containsKey("roomsPhotosList")){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>() {}.getType();
            realEstate.setRoomsPhotosList(
                    gson.fromJson(value.getAsString("roomsPhotosList"), listType)
            );
        }
        if (value.containsKey("rooms")) realEstate.setRooms(value.getAsInteger("rooms"));
        if (value.containsKey("bathrooms")) realEstate.setBathrooms(value.getAsInteger("bathrooms"));
        if (value.containsKey("bedrooms")) realEstate.setBedrooms(value.getAsInteger("bedrooms"));
        if (value.containsKey("pointsOfInterest")) {
            realEstate.setPointsOfInterest(Collections.singletonList(value.getAsString("pointsOfInterest")));
        }
        if (value.containsKey("street")) realEstate.setStreet(value.getAsString("street"));
        if (value.containsKey("city")) realEstate.setCity(value.getAsString("city"));
        if (value.containsKey("postalCode")) realEstate.setPostalCode(value.getAsString("postalCode"));
        if (value.containsKey("country")) realEstate.setCountry(value.getAsString("country"));
        if (value.containsKey("latitude")) realEstate.setLatitude(value.getAsDouble("latitude"));
        if (value.containsKey("longitude")) realEstate.setLongitude(value.getAsDouble("longitude"));
        if (value.containsKey("recordDate")) realEstate.setRecordDate(value.getAsString("recordDate"));
        if (value.containsKey("saleDate")) realEstate.setSaleDate(value.getAsString("saleDate"));
        if (value.containsKey("estateAgent")) realEstate.setEstateAgent(value.getAsString("estateAgent"));
        return  realEstate;
    }
}
