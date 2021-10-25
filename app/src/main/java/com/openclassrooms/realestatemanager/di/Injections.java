package com.openclassrooms.realestatemanager.di;

import android.content.Context;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDataBase;
import com.openclassrooms.realestatemanager.repository.RealEstateDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injections {

    public static RealEstateDataRepository provideRealEstateDataRepository(Context context) {
        RealEstateManagerDataBase dataBase = RealEstateManagerDataBase.getInstance(context);
        return new RealEstateDataRepository(dataBase.realEstateDAO());
    }

    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        RealEstateDataRepository realEstateDataRepository = provideRealEstateDataRepository(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(realEstateDataRepository, executor);
    }
}
