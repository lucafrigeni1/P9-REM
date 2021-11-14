package com.openclassrooms.realestatemanager.ui.activity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.ContentProvider;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.database.RealEstateManagerDataBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {

    private ContentResolver contentResolver;

    private static long USER_ID = 1;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateManagerDataBase.class).allowMainThreadQueries().build();
        contentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @Test
    public void getItemsWhenNoItemInserted() {
        final Cursor cursor = contentResolver.query(ContentUris.withAppendedId(ContentProvider.URI_REALESTATE, USER_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

    @Test
    public void insertAndGetItem() {
        // BEFORE : Adding demo item
        final Uri userUri = contentResolver.insert(ContentProvider.URI_REALESTATE, generateRealEstate());
        // TEST
        final Cursor cursor = contentResolver.query(ContentUris.withAppendedId(ContentProvider.URI_REALESTATE, USER_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("descriptions")), is("nice house"));
    }

    private ContentValues generateRealEstate(){
        final ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("type", "HOUSE");
        values.put("descriptions", "nice house");
        values.put("mainPhoto", "");
        values.put("dollarPrice", 1000);
        values.put("euroPrice", 850);
        values.put("isSold", true);
        values.put("surface", 500);
        values.put("roomsPhotosList", "");
        values.put("rooms", 5);
        values.put("bathrooms", 2);
        values.put("bedrooms", 2);
        values.put("pointsOfInterest", "Doctor");
        values.put("street", "street");
        values.put("city", "city");
        values.put("postalCode", "00000");
        values.put("country", "country");
        values.put("latitude", 0);
        values.put("longitude", 0);
        values.put("recordDate", Utils.getTodayDate());
        values.put("saleDate", Utils.getTodayDate());
        values.put("lastEditDate", Utils.getTodayDate());
        values.put("estateAgent", "me");
        return values;
    }
}
