package com.openclassrooms.realestatemanager.repository;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.openclassrooms.realestatemanager.utils.FiltersUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.database.RealEstateDAO;
import com.openclassrooms.realestatemanager.models.QueryFilter;
import com.openclassrooms.realestatemanager.models.RealEstate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

public class RealEstateDataRepository {

    CollectionReference realEstateCollectionReference =
            FirebaseFirestore.getInstance().collection("realEstate");

    private final RealEstateDAO realEstateDAO;

    public RealEstateDataRepository(RealEstateDAO realEstateDAO) {
        this.realEstateDAO = realEstateDAO;
    }

    public LiveData<List<RealEstate>> getRealEstateList(Executor executor) {
        MutableLiveData<List<RealEstate>> data = new MutableLiveData<>();
        executor.execute(() -> {
            data.postValue(realEstateDAO.getRealEstateList());
            if (Utils.isNetworkAvailable()) {
                synchroniseWithFirebase(realEstateDAO.getRealEstateList(), executor, data);
            }
        });
        return data;
    }

    public void synchroniseWithFirebase(List<RealEstate> roomList, Executor executor, MutableLiveData<List<RealEstate>> data) {
        List<RealEstate> firebaseList = new ArrayList<>();
        realEstateCollectionReference.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                RealEstate realEstate = document.toObject(RealEstate.class);
                firebaseList.add(realEstate);
            }

            for (RealEstate firebaseListItem : firebaseList) {
                if (!roomList.contains(firebaseListItem)) {
                    executor.execute(() -> realEstateDAO.insertRealEstate(firebaseListItem));
                }
            }
            data.setValue(roomList);
        });
    }

    public LiveData<List<RealEstate>> getFilteredRealEstateList(QueryFilter queryFilter, Executor executor) {
        MutableLiveData<List<RealEstate>> data = new MutableLiveData<>();
        boolean isPOIEmpty = queryFilter.getPointsOfInterest().isEmpty();
        executor.execute(() -> {
            List<RealEstate> list = this.realEstateDAO.getFilteredRealEstateList(
                    Utils.isConvertedInEuro + "",
                    isPOIEmpty + "",
                    queryFilter.getType(),
                    queryFilter.getMinPrice(),
                    queryFilter.getMaxPrice(),
                    queryFilter.isSold(),
                    queryFilter.getMinSurface(),
                    queryFilter.getMaxSurface(),
                    queryFilter.getMinRooms(),
                    queryFilter.getMaxRooms(),
                    queryFilter.getMinBathrooms(),
                    queryFilter.getMaxBathrooms(),
                    queryFilter.getMinBedrooms(),
                    queryFilter.getMaxBedrooms(),
                    FiltersUtils.searchPOI(queryFilter.getPointsOfInterest()),
                    queryFilter.getCity()
            );
            data.postValue(list);
        });
        return data;
    }

    public LiveData<RealEstate> getRealEstate(long id) {
        return this.realEstateDAO.getRealEstate(id);
    }

    public void createRealEstate(RealEstate realEstate) {
        realEstateCollectionReference.document(String.valueOf(realEstate.getId())).set(realEstate);
        realEstateDAO.insertRealEstate(realEstate);
    }

    public LiveData<String> uploadImage(Uri imageUri) {
        MutableLiveData<String> data = new MutableLiveData<>();
        String uuid = UUID.randomUUID().toString();
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        mImageRef.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) data.setValue(uuid);
        });
        return data;
    }
}
