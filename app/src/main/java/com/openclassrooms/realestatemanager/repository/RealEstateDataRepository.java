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
import com.openclassrooms.realestatemanager.database.RealEstateDAO;
import com.openclassrooms.realestatemanager.models.QueryFilter;
import com.openclassrooms.realestatemanager.models.RealEstate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RealEstateDataRepository {

   private final RealEstateDAO realEstateDAO;

   public RealEstateDataRepository(RealEstateDAO realEstateDAO) {
       this.realEstateDAO = realEstateDAO;
   }

   public LiveData<List<RealEstate>> getRealEstateList(QueryFilter queryFilter){
       if (queryFilter != null ) {
           return this.realEstateDAO.getFilteredRealEstateList(
                   //queryFilter.getType(),
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
                   queryFilter.getMaxBedrooms()
                   //,
                   //queryFilter.getPointsOfInterest(),
                   //queryFilter.getCity()
           );
       } else {
           return this.realEstateDAO.getRealEstateList();
       }
    }

    public LiveData<RealEstate> getRealEstate(String id) {
        return this.realEstateDAO.getRealEstate(id);
    }

   public void createRealEstate(RealEstate realEstate){
       realEstateDAO.insertRealEstate(realEstate);
   }

   //SYNCHRONIZE WITH FIREBASE
    public void synchroniseWithFirebase(List<RealEstate> roomList){
        CollectionReference realEstateCollectionReference =
                FirebaseFirestore.getInstance().collection("realEstate");

        List<RealEstate> firebaseList = new ArrayList<>();

        realEstateCollectionReference.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                RealEstate realEstate = document.toObject(RealEstate.class);
                firebaseList.add(realEstate);
            }

            for (RealEstate roomListItem : roomList){
                if (!firebaseList.contains(roomListItem)){
                    realEstateCollectionReference.document(roomListItem.getId()).set(roomListItem);
                }
            }

            for (RealEstate firebaseListItem : firebaseList){
                if (!roomList.contains(firebaseListItem)){
                    createRealEstate(firebaseListItem);
                }
            }

            for (RealEstate roomListItem : roomList){
                for (RealEstate firebaseListItem : firebaseList) {
                    if (roomListItem.getId().equals(firebaseListItem.getId())) {
                        try {
                            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(roomListItem.getLastEditDate());
                            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(firebaseListItem.getLastEditDate());

                            if (date1.after(date2)) realEstateCollectionReference.document(roomListItem.getId()).set(roomListItem);
                            if (date1.before(date2)) createRealEstate(firebaseListItem);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

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
}
