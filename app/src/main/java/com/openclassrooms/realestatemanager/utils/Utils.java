package com.openclassrooms.realestatemanager.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.App;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Utils {

    public static boolean isTablet;
    public static LatLng latLng;
    public static boolean isConvertedInEuro;
    public static boolean wasMapsFragmentShown;
    public static String selectedRealEstate;

    public static FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.85);
    }

    public static int convertEuroToDollar(int euro) {
        return (int) Math.round(euro * 1.1765);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    public static Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                App.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static Address geolocate(String locationName) {
        Address address = null;
        Geocoder geocoder =
                new Geocoder(App.getInstance().getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList =
                    geocoder.getFromLocationName(locationName, 1);
            if (addressList.size() > 0) {
                address = addressList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public static Uri convertBitmapToUri(Bitmap bitmap) {
        Uri uri;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                App.getInstance().getApplicationContext().getContentResolver(),
                bitmap, "Title", null);
        uri = Uri.parse(path);
        return uri;
    }

    @SuppressLint("ObsoleteSdkInt")
    public static Bitmap getBitmapFromVectorDrawable(int drawableId) {
        Drawable drawable =
                ContextCompat.getDrawable(App.getInstance().getApplicationContext(), drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(Objects.requireNonNull(drawable))).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(Objects.requireNonNull(drawable).getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static String setPhotoUrl(String url) {
        url = "https://firebasestorage.googleapis.com/v0/b/real-estate-manager-24a7c.appspot.com/o/" + url + "?alt=media&token=aaa40131-adc6-404d-b184-532e7e720092";
        return url;
    }
}
