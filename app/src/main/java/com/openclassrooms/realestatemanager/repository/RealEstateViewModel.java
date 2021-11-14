package com.openclassrooms.realestatemanager.repository;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.QueryFilter;
import com.openclassrooms.realestatemanager.models.RealEstate;

import java.util.List;
import java.util.concurrent.Executor;

public class RealEstateViewModel extends ViewModel {

    private final RealEstateDataRepository realEstateDataRepository;
    private final Executor executor;

    public RealEstateViewModel(RealEstateDataRepository realEstateDataRepository, Executor executor) {
        this.realEstateDataRepository = realEstateDataRepository;
        this.executor = executor;
    }

    public LiveData<List<RealEstate>> getRealEstateList(QueryFilter queryFilter) {
        return realEstateDataRepository.getRealEstateList(queryFilter, executor);
    }

    public LiveData<RealEstate> getRealEstate(String id) {
        return realEstateDataRepository.getRealEstate(id);
    }

    public void createRealEstate(RealEstate realEstate) {
        executor.execute(() -> realEstateDataRepository.createRealEstate(realEstate));
    }

    public void synchroniseWithFirebase(List<RealEstate> realEstateList) {
        executor.execute(() -> realEstateDataRepository.synchroniseWithFirebase(realEstateList, executor));
    }

    public LiveData<String> uploadImageInStorage(Uri uri) {
        return realEstateDataRepository.uploadImage(uri);
    }
}
