package com.openclassrooms.realestatemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static LatLng latLng;

    public static boolean isConvertedInEuro;

    public static FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.85);
    }

    public static int convertEuroToDollar(int euro) {
        return (int) Math.round(euro * 1.18);
    }

    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    public static Boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static Address geolocate(Context context, String locationName) {
        Address address = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            if (addressList.size() > 0) {
                address = addressList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public static Uri convertBitmapToUri(Bitmap bitmap, Context context) {
        Uri uri;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        uri = Uri.parse(path);
        return uri;
    }

    public static String setUrl(String url) {
        url = "https://firebasestorage.googleapis.com/v0/b/real-estate-manager-24a7c.appspot.com/o/" + url + "?alt=media&token=aaa40131-adc6-404d-b184-532e7e720092";
        return url;
    }

    @SuppressLint("ResourceAsColor")
    public static Chip chipGenerator(String a, String b, boolean isText, boolean isUpper, Context context){
        Chip chip = new Chip(context);
        if (!isText) {
            if (isUpper) {
                chip.setText("> " + a + b);
            } else
                chip.setText("< " + a + b);
        } else
            chip.setText(a);

        chip.setChipBackgroundColorResource(R.color.colorPrimary800);
        chip.setTextColor(R.color.colorPrimary50);
        return chip;
    }
}
