package com.openclassrooms.realestatemanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.models.RealEstate;

import java.util.List;

@Dao
public interface RealEstateDAO {

    @Query("SELECT * FROM RealEstate")
    LiveData<List<RealEstate>> getRealEstateList();

   @Query("SELECT * FROM RealEstate WHERE " +
           //"type LIKE :type AND " +
           "dollarPrice BETWEEN :minPrice AND :maxPrice AND " +
           "isSold LIKE :isSold AND " +
           "surface BETWEEN :minSurface AND :maxSurface AND " +
           "rooms BETWEEN :minRooms AND :maxRooms AND " +
           "bathrooms BETWEEN :minBathrooms AND :maxBathrooms AND " +
           "bedrooms BETWEEN :minBedrooms AND :maxBedrooms "
           //"AND "+
           //"pointsOfInterest IN (:pointsOfInterest) AND " +
           //"city LIKE :city"
           )
   LiveData<List<RealEstate>> getFilteredRealEstateList(
           //String type,
           double minPrice,
           double maxPrice,
           boolean isSold,
           double minSurface,
           double maxSurface,
           int minRooms,
           int maxRooms,
           int minBathrooms,
           int maxBathrooms,
           int minBedrooms,
           int maxBedrooms
           //,
           //List<String> pointsOfInterest,
           //String city
   );

    @Query("SELECT * FROM RealEstate WHERE id = :id")
    LiveData<RealEstate> getRealEstate(String id);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertRealEstate(RealEstate realEstate);
}
