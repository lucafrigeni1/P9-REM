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
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.database.RealEstateDAO;
import com.openclassrooms.realestatemanager.models.QueryFilter;
import com.openclassrooms.realestatemanager.models.RealEstate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RealEstateDataRepository {

    public static final CollectionReference realEstateCollectionReference =
            FirebaseFirestore.getInstance().collection("realEstate");

   // public LiveData<List<RealEstate>> getRealEstateList(
   //         boolean isFiltered,
   //         RealEstate minFilteredValues,
   //         RealEstate maxFilteredValues) {
   //     List<RealEstate> realEstateList = new ArrayList<>();
   //     MutableLiveData<List<RealEstate>> data = new MutableLiveData<>();
//
   //     realEstateCollectionReference.get().addOnCompleteListener(task -> {
   //         for (QueryDocumentSnapshot document : task.getResult()) {
   //             RealEstate realEstate = document.toObject(RealEstate.class);
   //             realEstateList.add(realEstate);
   //         }
//
   //         if (Utils.isConvertedInEuro){
   //             for (RealEstate realEstate : realEstateList){
   //                 realEstate.setPrice(Utils.convertDollarToEuro((int) realEstate.getPrice()));
   //             }
   //         }
//
   //         if (isFiltered){
   //             filter(minFilteredValues, maxFilteredValues, realEstateList, data);
   //             Log.e("getRealEstateList min: ", String.valueOf(minFilteredValues.getPrice()));
   //             Log.e("getRealEstateList max: ", String.valueOf(maxFilteredValues.getPrice()));
   //         } else
   //             data.setValue(realEstateList);
   //     });
   //     return data;
   // }

    public void filter(
            RealEstate minFilteredValues,
            RealEstate maxFilteredValues,
            List<RealEstate> realEstateList,
            MutableLiveData<List<RealEstate>> data) {

        List<RealEstate> filteredRealEstateList = new ArrayList<>();

        for (RealEstate realEstate : realEstateList) {
            if (realEstate.getPrice() >= minFilteredValues.getPrice() && realEstate.getPrice() <= maxFilteredValues.getPrice())
                if (realEstate.getSurface() >= minFilteredValues.getSurface() && realEstate.getSurface() <= maxFilteredValues.getSurface())
                    if (realEstate.getRooms() >= minFilteredValues.getRooms() && realEstate.getRooms() <= maxFilteredValues.getRooms())
                        if (realEstate.getBathrooms() >= minFilteredValues.getBathrooms() && realEstate.getBathrooms() <= maxFilteredValues.getBathrooms())
                            if (realEstate.getBedrooms() >= minFilteredValues.getBedrooms() && realEstate.getBedrooms() <= maxFilteredValues.getBedrooms())
                                if (realEstate.getType().equals(minFilteredValues.getType()) || minFilteredValues.getType().equals(""))
                                    if (realEstate.getCity().equals(minFilteredValues.getCity()) || minFilteredValues.getCity().equals(""))
                                        if (realEstate.isSold() == minFilteredValues.isSold())
                                            filteredRealEstateList.add(realEstate);
        }

        for (RealEstate realEstate : filteredRealEstateList){
            for (String a : minFilteredValues.getPointsOfInterest()) {
                if (!realEstate.getPointsOfInterest().contains(a))
                    filteredRealEstateList.remove(realEstate);
            }
        }
        data.setValue(filteredRealEstateList);
    }

    //public LiveData<RealEstate> getRealEstate(String id) {
    //    MutableLiveData<RealEstate> data = new MutableLiveData<>();
    //    realEstateCollectionReference.document(id).get().addOnCompleteListener(task -> {
    //        if (task.isSuccessful()) {
    //            data.setValue(task.getResult().toObject(RealEstate.class));
    //        }
    //    });
    //    return data;
    //}

    //public void createRealEstate(RealEstate realEstate) {
    //    realEstateCollectionReference.document(realEstate.getId()).set(realEstate);
    //}

    public LiveData<String> uploadImage(Uri imageUri) {
        MutableLiveData<String> data = new MutableLiveData<>();
        String uuid = UUID.randomUUID().toString();
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        mImageRef.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                data.setValue(uuid);
            }
        });
        return data;
    }


// ROOM

   private final RealEstateDAO realEstateDAO;

   public RealEstateDataRepository(RealEstateDAO realEstateDAO) {
       this.realEstateDAO = realEstateDAO;
   }

   public LiveData<List<RealEstate>> getRealEstateList(){
       return this.realEstateDAO.getRealEstateList();
   }

    public LiveData<List<RealEstate>> getFilteredRealEstateList(QueryFilter queryFilter){

        // List<RealEstate> realEstateList = new ArrayList<>();
        // MutableLiveData<List<RealEstate>> data = new MutableLiveData<>();
//
        // realEstateCollectionReference.get().addOnCompleteListener(task -> {
        //     for (QueryDocumentSnapshot document : task.getResult()) {
        //         RealEstate realEstate = document.toObject(RealEstate.class);
        //         realEstateList.add(realEstate);
        //     }
//
        //     if (Utils.isConvertedInEuro){
        //         for (RealEstate realEstate : realEstateList){
        //             realEstate.setPrice(Utils.convertDollarToEuro((int) realEstate.getPrice()));
        //         }
        //     }
//
        //     if (isFiltered){
        //         filter(minFilteredValues, maxFilteredValues, realEstateList, data);
        //         Log.e("getRealEstateList min: ", String.valueOf(minFilteredValues.getPrice()));
        //         Log.e("getRealEstateList max: ", String.valueOf(maxFilteredValues.getPrice()));
        //     } else
        //         data.setValue(realEstateList);
        // });
        // return data;

        return this.realEstateDAO.getFilteredRealEstateList();
    }

    public LiveData<RealEstate> getRealEstate(String id) {
        return this.realEstateDAO.getRealEstate(id);
    }

   public void createRealEstate(RealEstate realEstate){
       realEstateDAO.insertRealEstate(realEstate);
   }
}
