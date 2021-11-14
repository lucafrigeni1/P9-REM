package com.openclassrooms.realestatemanager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.database.RealEstateDAO;
import com.openclassrooms.realestatemanager.database.RealEstateManagerDataBase;
import com.openclassrooms.realestatemanager.models.RealEstate;

public class ContentProvider extends android.content.ContentProvider {

   public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider";
   public static final String TABLE_NAME = RealEstate.class.getSimpleName();
   public static final Uri URI_REALESTATE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

   @Override
   public boolean onCreate() {
       return true;
   }

   @Nullable
   @Override
   public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
       if (getContext() != null){
           String id = String.valueOf(ContentUris.parseId(uri));
           final Cursor cursor = RealEstateManagerDataBase.getInstance(getContext()).realEstateDAO().getRealEstateWithCursor(id);
           cursor.setNotificationUri(getContext().getContentResolver(), uri);
           return cursor;
       }
       throw new IllegalArgumentException("Failed to query row for uri " + uri);
   }

   @Nullable
   @Override
   public String getType(@NonNull Uri uri) {
       return "vnd.android.cursor.realEstate/" + AUTHORITY + "." + TABLE_NAME;
   }

   @Nullable
   @Override
   public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
       if (getContext() != null){
           final long id = RealEstateManagerDataBase.getInstance(getContext()).realEstateDAO().insertRealEstate(RealEstate.fromContentValues(values));
           if (id != 0){
               getContext().getContentResolver().notifyChange(uri, null);
               return ContentUris.withAppendedId(uri, id);
           }
       }
       throw new IllegalArgumentException("Failed to insert row into " + uri);
   }

   @Override
   public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
       return 0;
   }

   @Override
   public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
       return 0;
   }
}
