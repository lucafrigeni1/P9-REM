package com.openclassrooms.realestatemanager.ui.fragment;

import static com.openclassrooms.realestatemanager.utils.Utils.latLng;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.di.Injections;
import com.openclassrooms.realestatemanager.di.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.repository.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    RealEstateViewModel realEstateViewModel;
    List<RealEstate> realEstates = new ArrayList<>();

    private GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 15, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    Location location;
    double lat, lng;

    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floatingActionButton = view.findViewById(R.id.location_button);
        setViewModel();
        setFloatingActionButton();
    }

    private void setViewModel() {
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this.getContext());
        this.realEstateViewModel = new ViewModelProvider(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    public void setRealEstates(List<RealEstate> realEstateList) {
        realEstates = realEstateList;
    }

    private void setFloatingActionButton() {
        floatingActionButton.setOnClickListener(v -> getDeviceLocation());
    }

    @Override
    public void onResume() {
        super.onResume();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity());
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_fragment);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        setMapStyle();
        getDeviceLocation();
    }

    private void setMapStyle() {
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void getDeviceLocation() {
        getLocationPermission();
        try {
            if (Utils.isInternetAvailable(Objects.requireNonNull(this.getContext()))) {
                if (locationPermissionGranted) {
                    map.setMyLocationEnabled(true);
                    Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(this.requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            location = task.getResult();
                            if (location != null) {
                                getAndSaveLocation();
                                setMarkers();
                            } else
                                Toast.makeText(this.getContext(), getString(R.string.error_location), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else
                Toast.makeText(this.getContext(), getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getAndSaveLocation() {
        lat = location.getLatitude();
        lng = location.getLongitude();
        latLng = new LatLng(lat, lng);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }

    public void setMarkers() {
        map.clear();

        if (getContext() != null) {
            if (!realEstates.isEmpty()) {
                for (RealEstate realEstate : realEstates) {
                    Bitmap bm;
                    if (realEstate.isSold()) {
                        bm = getBitmapFromVectorDrawable(getContext(), R.drawable.ic_baseline_place_orange_24);
                    } else {
                        bm = getBitmapFromVectorDrawable(getContext(), R.drawable.ic_baseline_place_green_24);
                    }

                    LatLng latLng = new LatLng(realEstate.getLatitude(), realEstate.getLongitude());

                    map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(bm))
                            .title(realEstate.getStreet())
                    );
                }
            }
        }

        map.setOnInfoWindowClickListener(marker -> {
            for (RealEstate realEstate : realEstates) {
                if (realEstate.getStreet().equals(marker.getTitle())) {
                    ((MainActivity) Objects.requireNonNull(this.getActivity())).launchDetailFragment(realEstate);
                }
            }
        });
    }

    private static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
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

}
