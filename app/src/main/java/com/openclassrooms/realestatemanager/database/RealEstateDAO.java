package com.openclassrooms.realestatemanager.database;

import android.database.Cursor;

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
    List<RealEstate> getRealEstateList();


    @Query("SELECT * FROM RealEstate WHERE " +
            "type LIKE :type AND " +
            "(( :isConvertedInEuro = 'false' AND dollarPrice BETWEEN :minPrice AND :maxPrice)" +
            "OR ( :isConvertedInEuro = 'true' AND euroPrice BETWEEN :minPrice AND :maxPrice )) AND " +
            "isSold LIKE :isSold AND " +
            "surface BETWEEN :minSurface AND :maxSurface AND " +
            "rooms BETWEEN :minRooms AND :maxRooms AND " +
            "bathrooms BETWEEN :minBathrooms AND :maxBathrooms AND " +
            "bedrooms BETWEEN :minBedrooms AND :maxBedrooms AND " +
            "((:isPOIEmpty = 'false' AND pointsOfInterest LIKE (:pointsOfInterest))" +
            "OR (:isPOIEmpty = 'true')) AND " +
            "city LIKE :city"
    )
    List<RealEstate> getFilteredRealEstateList(
            String isConvertedInEuro,
            String isPOIEmpty,
            String type,
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
            int maxBedrooms,
            String pointsOfInterest,
            String city
    );

    @Query("SELECT * FROM RealEstate WHERE id = :id")
    LiveData<RealEstate> getRealEstate(long id);

    @Query("SELECT * FROM RealEstate WHERE id = :id")
    Cursor getRealEstateWithCursor(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRealEstate(RealEstate realEstate);
}
