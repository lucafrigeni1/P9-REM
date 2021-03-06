package com.openclassrooms.realestatemanager.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.di.Injections;
import com.openclassrooms.realestatemanager.di.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RoomsPhotos;
import com.openclassrooms.realestatemanager.repository.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.activity.CreateActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DetailFragment extends Fragment implements OnMapReadyCallback {

    public static DetailFragment newInstance(long realEstateId) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", realEstateId);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    RealEstateViewModel viewModel;

    RecyclerView recyclerView;
    RoomsPhotosAdapter roomsPhotosAdapter;

    FloatingActionButton editButton;
    ImageView mainPhoto;
    ImageView sold;
    TextView type, price, dates, description, roomsAndSurface, bathroomsNumber,
            bedroomsNumber, street, city, country;
    ChipGroup chipGroup;

    LatLng latLng;
    private GoogleMap map;
    private static final int DEFAULT_ZOOM = 15;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_real_estate_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(view);
        setViewModel();
    }

    private void findViewById(View view) {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.lite_map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        mainPhoto = view.findViewById(R.id.main_picture);
        type = view.findViewById(R.id.type);
        price = view.findViewById(R.id.price);
        dates = view.findViewById(R.id.dates);
        sold = view.findViewById(R.id.sold);
        recyclerView = view.findViewById(R.id.photo_recyclerview);
        editButton = view.findViewById(R.id.edit_button);
        description = view.findViewById(R.id.description);
        roomsAndSurface = view.findViewById(R.id.rooms_and_surface);
        bathroomsNumber = view.findViewById(R.id.bathrooms);
        bedroomsNumber = view.findViewById(R.id.bedrooms);
        chipGroup = view.findViewById(R.id.chip_group);
        street = view.findViewById(R.id.street);
        city = view.findViewById(R.id.city);
        country = view.findViewById(R.id.country);
    }

    private void setViewModel() {
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this.getContext());
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get( RealEstateViewModel.class);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        getRealEstate();
    }

    private void getRealEstate(){
        assert getArguments() != null;
        viewModel.getRealEstate((getArguments()).getLong("id")).observe(getViewLifecycleOwner(), this::setView);
    }

    private void setView(RealEstate realEstate) {
        if (realEstate != null) {
            setHeader(realEstate);
            setPhotos(realEstate);
            setChipGroup(realEstate);
            setMaps(realEstate);

            roomsAndSurface.setText(getString(R.string.surface_text,
                    String.valueOf(realEstate.getSurface()), String.valueOf(realEstate.getRooms())));
            bathroomsNumber.setText(String.valueOf(realEstate.getBathrooms()));
            bedroomsNumber.setText(String.valueOf(realEstate.getBedrooms()));
            description.setText(realEstate.getDescriptions());

            setEditButton(realEstate);
        }
    }

    private void setHeader(RealEstate realEstate){
        type.setText(realEstate.getType());

        if (Utils.isConvertedInEuro){
            price.setText(getString(R.string.euro_price,
                    NumberFormat.getNumberInstance(Locale.FRANCE).format(realEstate.getEuroPrice())));
        } else {
            price.setText(getString(R.string.dollar_price,
                    NumberFormat.getNumberInstance(Locale.FRANCE).format(realEstate.getDollarPrice())));
        }

        if (!realEstate.isSold()) sold.setBackgroundResource(R.drawable.ic_baseline_not_sell_24);
        else sold.setBackgroundResource(R.drawable.ic_baseline_sell_24);

        if (realEstate.getSaleDate().isEmpty()) dates.setText(realEstate.getRecordDate());
        else dates.setText(String.format("%s - %s", realEstate.getRecordDate(), realEstate.getSaleDate()));
    }

    private void setPhotos(RealEstate realEstate){
        Glide.with(mainPhoto).load(Utils.setPhotoUrl(realEstate.getMainPhoto())).centerCrop().into(mainPhoto);

        List<RoomsPhotos> roomsPhotosList = realEstate.getRoomsPhotosList();
        roomsPhotosAdapter = new RoomsPhotosAdapter(roomsPhotosList, false);
        recyclerView.setAdapter(roomsPhotosAdapter);
    }

    private void setChipGroup(RealEstate realEstate){
        chipGroup.removeAllViews();
        for (String poi : realEstate.getPointsOfInterest()) {
            Chip chip = new Chip(Objects.requireNonNull(this.getContext()));
            chip.setText(poi);
            chip.setChipBackgroundColorResource(R.color.colorPrimary100);
            chip.setTextColor(getResources().getColor(R.color.colorPrimary800));
            chipGroup.addView(chip);
        }
    }

    private void setMaps(RealEstate realEstate){
        street.setText(realEstate.getStreet());
        city.setText(String.format("%s %s", realEstate.getCity(), realEstate.getPostalCode()));
        country.setText(realEstate.getCountry());

        latLng = new LatLng(realEstate.getLatitude(), realEstate.getLongitude());
        map.addMarker(new MarkerOptions().position(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }


    private void setEditButton(RealEstate realEstate) {
        editButton.setOnClickListener(v -> {
            if (Utils.isNetworkAvailable()) {
                startActivity(
                        new Intent(this.getContext(), CreateActivity.class)
                                .putExtra(CreateActivity.EXTRA_REAL_ESTATE, realEstate.getId())
                );
            } else
                Toast.makeText(this.getContext(), R.string.error_no_internet,
                        Toast.LENGTH_LONG).show();
        });
    }
}
