package com.openclassrooms.realestatemanager.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.repository.RealEstateDataRepository;
import com.openclassrooms.realestatemanager.repository.RealEstateViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RealEstateDataRepository realEstateDataRepository;
    private final Executor executor;

    public ViewModelFactory(RealEstateDataRepository realEstateDataRepository, Executor executor) {
        this.realEstateDataRepository = realEstateDataRepository;
        this.executor = executor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RealEstateViewModel.class)) {
            return (T) new RealEstateViewModel(realEstateDataRepository, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
