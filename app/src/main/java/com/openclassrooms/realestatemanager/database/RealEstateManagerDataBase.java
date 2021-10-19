package com.openclassrooms.realestatemanager.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.openclassrooms.realestatemanager.models.RealEstate;

@Database(entities = {RealEstate.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class RealEstateManagerDataBase extends RoomDatabase {

    private static volatile RealEstateManagerDataBase INSTANCE;
    public abstract RealEstateDAO realEstateDAO();

    public static RealEstateManagerDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateManagerDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RealEstateManagerDataBase.class,
                            "RealEstateManagerDataBase.db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
