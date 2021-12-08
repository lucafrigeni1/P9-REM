package com.openclassrooms.realestatemanager.repository;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.QueryFilter;
import com.openclassrooms.realestatemanager.models.RealEstate;

import java.util.List;
import java.util.concurrent.Executor;

public class RealEstateViewModel extends ViewModel {

    private RealEstateDataRepository realEstateDataRepository;
    private Executor executor;

    MutableLiveData<QueryFilter> queryFilterLiveData = new MutableLiveData<>();

    LiveData<List<RealEstate>> realEstateListLiveData = Transformations.switchMap(queryFilterLiveData, queryFilter -> {
        if (queryFilter == null){
            return realEstateDataRepository.getRealEstateList(executor);
        } else {
            return realEstateDataRepository.getFilteredRealEstateList(queryFilter, executor);
        }
    });

    public RealEstateViewModel(RealEstateDataRepository realEstateDataRepository, Executor executor) {
        this.realEstateDataRepository = realEstateDataRepository;
        this.executor = executor;
    }

    public LiveData<List<RealEstate>> getRealEstateList() {
        return realEstateListLiveData;
    }

    public void refresh(){
        queryFilterLiveData.setValue(null);
    }

    public void setQueryFilter(QueryFilter queryFilter) {
        this.queryFilterLiveData.setValue(queryFilter);
    }

    public LiveData<RealEstate> getRealEstate(String id) {
        return realEstateDataRepository.getRealEstate(id);
    }

    public void createRealEstate(RealEstate realEstate) {
        executor.execute(() -> realEstateDataRepository.createRealEstate(realEstate));
    }

    public LiveData<String> uploadImageInStorage(Uri uri) {
        return realEstateDataRepository.uploadImage(uri);
    }
}
