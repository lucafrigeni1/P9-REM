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
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.openclassrooms.realestatemanager.provider.ContentProvider;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.database.RealEstateManagerDataBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLOutput;

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {

    private ContentResolver contentResolver;


    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                RealEstateManagerDataBase.class)
                .allowMainThreadQueries()
                .build();
        contentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    @Test
    public void getRealEstate() {
        final Cursor cursor = contentResolver.query(ContentUris.withAppendedId(ContentProvider.URI_REALESTATE, 1),
                null,
                null,
                null,
                null);

        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
    }
}
