package com.openclassrooms.realestatemanager.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
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

    public static DetailFragment newInstance(String realEstateId) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", realEstateId);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    RealEstateViewModel viewModel;

    RecyclerView recyclerView;
    RoomsPhotosAdapter roomsPhotosAdapter;

    ImageButton backButton;
    FloatingActionButton editButton;
    ImageView sold;
    TextView type, price, currency, dates, description, roomsAndSurface, bathroomsNumber, bedroomsNumber, street, city, country;
    ChipGroup chipGroup;
    ConstraintLayout nothingSelected, progressIndicatorLayout;

    CircularProgressIndicator progressIndicator;

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
        viewModel.getRealEstate(getArguments().getString("id")).observe(getViewLifecycleOwner(), this::setView);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void findViewById(View view) {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.lite_map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        progressIndicatorLayout = view.findViewById(R.id.progress_bar_layout);
        progressIndicator = view.findViewById(R.id.progress_bar);
        backButton = view.findViewById(R.id.back_button);
        type = view.findViewById(R.id.type);
        price = view.findViewById(R.id.price);
        currency = view.findViewById(R.id.currency);
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
        nothingSelected = view.findViewById(R.id.nothing_selected);
    }

    private void setView(RealEstate realEstate) {
        //backButton

        progressIndicatorLayout.setVisibility(View.GONE);

        if (realEstate != null) {
            type.setText(realEstate.getType());

            if (Utils.isConvertedInEuro) {
                price.setText(NumberFormat.getNumberInstance(Locale.FRANCE).format(Utils.convertDollarToEuro((int) realEstate.getPrice())));
                currency.setText("€");
            } else {
                price.setText(NumberFormat.getNumberInstance(Locale.FRANCE).format((int) realEstate.getPrice()));
                currency.setText("$");
            }

            if (!realEstate.isSold()){
                sold.setBackgroundResource(R.drawable.ic_baseline_not_sell_24);
            } else
                sold.setBackgroundResource(R.drawable.ic_baseline_sell_24);

            if (realEstate.getSaleDate().isEmpty()){
                dates.setText(realEstate.getRecordDate());
            } else
            dates.setText(realEstate.getRecordDate() + " - " + realEstate.getSaleDate());

            List<RoomsPhotos> roomsPhotosList = realEstate.getRoomsPhotosList();
            roomsPhotosAdapter = new RoomsPhotosAdapter(roomsPhotosList, false);
            recyclerView.setAdapter(roomsPhotosAdapter);

            description.setText(realEstate.getDescriptions());
            roomsAndSurface.setText(getString(R.string.surface_text, String.valueOf(realEstate.getSurface()), String.valueOf(realEstate.getRooms())));
            bathroomsNumber.setText(String.valueOf(realEstate.getBathrooms()));
            bedroomsNumber.setText(String.valueOf(realEstate.getBedrooms()));
            street.setText(realEstate.getStreet());
            city.setText(String.format("%s %s", realEstate.getCity(), realEstate.getPostalCode()));
            country.setText(realEstate.getCountry());
            latLng = new LatLng(realEstate.getLatitude(), realEstate.getLongitude());

            for (String poi: realEstate.getPointsOfInterest()){
                Chip chip = new Chip(Objects.requireNonNull(this.getContext()));
                chip.setText(poi);
                chip.setChipBackgroundColorResource(R.color.colorPrimary100);
                chip.setTextColor(getResources().getColor(R.color.colorPrimary800));
                chipGroup.addView(chip);
            }
            setEditButton(realEstate);
        } else
            nothingSelected.setVisibility(View.VISIBLE);

        map.addMarker(new MarkerOptions().position(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }

    private void setEditButton(RealEstate realEstate) {
        editButton.setOnClickListener(v -> {
            if (Utils.isInternetAvailable(Objects.requireNonNull(this.getContext()))) {
                Intent intent = new Intent(v.getContext(), CreateActivity.class);
                intent.putExtra(CreateActivity.EXTRA_REAL_ESTATE, realEstate.getId());
                v.getContext().startActivity(intent);
            } else
                Toast.makeText(this.getContext(), "aaa", Toast.LENGTH_LONG).show();
        });
    }

    private void setViewModel() {
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this.getContext());
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

}
