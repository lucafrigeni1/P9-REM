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

    @Query("SELECT * FROM RealEstate")
    LiveData<List<RealEstate>> getFilteredRealEstateList();

    @Query("SELECT * FROM RealEstate WHERE id = :id")
    LiveData<RealEstate> getRealEstate(String id);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertRealEstate(RealEstate realEstate);
}
