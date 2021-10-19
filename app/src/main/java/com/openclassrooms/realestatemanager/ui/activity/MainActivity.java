package com.openclassrooms.realestatemanager.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.di.Injections;
import com.openclassrooms.realestatemanager.di.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.QueryFilter;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.repository.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.fragment.DetailFragment;
import com.openclassrooms.realestatemanager.ui.fragment.MapsFragment;
import com.openclassrooms.realestatemanager.ui.fragment.RealEstateListFragment;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RealEstateViewModel realEstateViewModel;
    public List<RealEstate> realEstates = new ArrayList<>();

    MaterialToolbar topAppBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;

    RealEstateListFragment listFragment;
    MapsFragment mapsFragment;
    DetailFragment detailFragment;

    TextView headerName, headerMail;

    QueryFilter queryFilter;

    ImageButton searchButton;
    NestedScrollView bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    RangeSlider priceSlider, surfaceSlider, roomsSlider, bathroomsSlider, bedroomsSlider;
    AutoCompleteTextView typeFilter;
    TextView cityFilter;
    ChipGroup bottomSheetChipGroup;
    ChipGroup topChipGroup;
    CheckBox soldFilter;
    FloatingActionButton filterButton;
    boolean isFilter;

    float minPriceFilter;
    float maxPriceFilter;
    float minSurfaceFilter;
    float maxSurfaceFilter;
    float minRoomsFilter;
    float maxRoomsFilter;
    float minBathRoomsFilter;
    float maxBathRoomsFilter;
    float minBedRoomsFilter;
    float maxBedRoomsFilter;

    RealEstate minRealEstate, maxRealEstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        initFragment();
        Utils.isConvertedInEuro = false;
        setBottomSheetBehavior();
        setBottomNavigation();
        setTopAppBar();
        setViewModel();
        getRealEstates(false);
    }

    private void findViewById() {
        topAppBar = findViewById(R.id.top_app_bar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bot_navigation);
        searchButton = findViewById(R.id.search_button);
        bottomSheet = findViewById(R.id.bottom_sheet);
        priceSlider = findViewById(R.id.price_filter);
        surfaceSlider = findViewById(R.id.surface_filter);
        roomsSlider = findViewById(R.id.rooms_filter);
        bathroomsSlider = findViewById(R.id.bathrooms_filter);
        bedroomsSlider = findViewById(R.id.bedrooms_filter);
        typeFilter = findViewById(R.id.type_filter_input);
        cityFilter = findViewById(R.id.city_filter_input);
        bottomSheetChipGroup = findViewById(R.id.POI_chip_group);
        soldFilter = findViewById(R.id.checkbox);
        filterButton = findViewById(R.id.validation_button);
        topChipGroup = findViewById(R.id.top_chip_group);
    }

    private void setBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void setViewModel() {
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this);
        this.realEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void getRealEstates(boolean isConvert) {
        if (isFilter) {
            getFilteredValues();
            if (isConvert)
                realEstateViewModel.getRealEstateList().observe(this, this::setFilterBottomSheet);
        } else
            realEstateViewModel.getRealEstateList().observe(this, this::setFilterBottomSheet);

        realEstateViewModel.getFilteredRealEstateList(queryFilter).observe(this, this::setRealEstates);
    }

    public void setRealEstates(List<RealEstate> realEstatesList) {
        if (mapsFragment.isVisible()) {
            mapsFragment.setRealEstates(realEstatesList);
            setMapsFragment();
        } else {
            listFragment.setRealEstateList(realEstatesList);
            setListFragment();
        }

    }

    private void setFilterBottomSheet(List<RealEstate> realEstatesList) {
        int lastItem = realEstatesList.size() - 1;

        if (!realEstatesList.isEmpty()) {
            Collections.sort(realEstatesList, new RealEstate.PriceComparator());
            minPriceFilter = (float) realEstatesList.get(0).getPrice();
            maxPriceFilter = (float) realEstatesList.get(lastItem).getPrice() + 1;

            Collections.sort(realEstatesList, new RealEstate.SurfaceComparator());
            minSurfaceFilter = (float) realEstatesList.get(0).getSurface();
            maxSurfaceFilter = (float) realEstatesList.get(lastItem).getSurface() + 1;

            Collections.sort(realEstatesList, new RealEstate.RoomsComparator());
            minRoomsFilter = (float) realEstatesList.get(0).getRooms();
            maxRoomsFilter = (float) realEstatesList.get(lastItem).getRooms() + 1;

            Collections.sort(realEstatesList, new RealEstate.BathRoomsComparator());
            minBathRoomsFilter = (float) realEstatesList.get(0).getBathrooms();
            maxBathRoomsFilter = (float) realEstatesList.get(lastItem).getBathrooms() + 1;

            Collections.sort(realEstatesList, new RealEstate.BedRoomsComparator());
            minBedRoomsFilter = (float) realEstatesList.get(0).getBedrooms();
            maxBedRoomsFilter = (float) realEstatesList.get(lastItem).getBedrooms() + 1;

            priceSlider.setValueFrom(minPriceFilter);
            priceSlider.setValueTo(maxPriceFilter);
            priceSlider.setValues(minPriceFilter, maxPriceFilter);
            priceSlider.setLabelFormatter(value -> {
                NumberFormat currencyformat = NumberFormat.getCurrencyInstance();
                if (Utils.isConvertedInEuro) {
                    currencyformat.setCurrency(Currency.getInstance("EUR"));
                } else
                    currencyformat.setCurrency(Currency.getInstance("USD"));
                return currencyformat.format(value);
            });

            surfaceSlider.setValueFrom(minSurfaceFilter);
            surfaceSlider.setValueTo(maxSurfaceFilter);
            surfaceSlider.setValues(minSurfaceFilter, maxSurfaceFilter);

            roomsSlider.setValueFrom(minRoomsFilter);
            roomsSlider.setValueTo(maxRoomsFilter);
            roomsSlider.setValues(minRoomsFilter, maxRoomsFilter);
            roomsSlider.setStepSize(1);

            bathroomsSlider.setValueFrom(minBathRoomsFilter);
            bathroomsSlider.setValueTo(maxBathRoomsFilter);
            bathroomsSlider.setValues(minBathRoomsFilter, maxBathRoomsFilter);
            bathroomsSlider.setStepSize(1);

            bedroomsSlider.setValueFrom(minBedRoomsFilter);
            bedroomsSlider.setValueTo(maxBedRoomsFilter);
            bedroomsSlider.setValues(minBedRoomsFilter, maxBedRoomsFilter);
            bedroomsSlider.setStepSize(1);
        }

        cityFilter.getEditableText().clear();
        typeFilter.getText().clear();

        String[] types = new String[]{"HOUSE", "FLAT", "STUDIO", "DUPLEX", "TRIPLEX"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, types);
        typeFilter.setAdapter(adapter);
        typeFilter.setDropDownBackgroundResource(R.color.colorPrimary800);

        for (int i = 0; i < bottomSheetChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) bottomSheetChipGroup.getChildAt(i);
            chip.setChecked(false);
        }

        soldFilter.setChecked(false);

        filterButton.setOnClickListener(v -> {
            if (!realEstatesList.isEmpty()) {
                isFilter = true;
                getRealEstates(false);
            }
        });
    }

    private void getFilteredValues() {
        String type = String.valueOf(typeFilter.getText());
        String city = String.valueOf(cityFilter.getText());
        boolean isSold = soldFilter.isChecked();
        List<String> pointsOfInterestList = new ArrayList<>();

        for (int i = 0; i < bottomSheetChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) bottomSheetChipGroup.getChildAt(i);
            if (chip.isChecked()) pointsOfInterestList.add(chip.getText().toString());
        }

        queryFilter = new QueryFilter.Builder()
                .type(type)
                .minPrice(priceSlider.getValues().get(0))
                .maxPrice(priceSlider.getValues().get(1))
                .isSold(isSold)
                .minSurface(surfaceSlider.getValues().get(0))
                .maxSurface(surfaceSlider.getValues().get(1))
                .minRooms(Math.round(roomsSlider.getValues().get(0)))
                .maxRooms(Math.round(roomsSlider.getValues().get(1)))
                .minBathrooms(Math.round(bathroomsSlider.getValues().get(0)))
                .maxBathrooms(Math.round(bathroomsSlider.getValues().get(1)))
                .minBedrooms(Math.round(bedroomsSlider.getValues().get(0)))
                .maxBedrooms(Math.round(bedroomsSlider.getValues().get(1)))
                .pointsOfInterest(pointsOfInterestList)
                .city(city)
                .build();

        setChipGroup(pointsOfInterestList);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void setChipGroup(List<String> pointsOfInterestList) {
        topChipGroup.removeAllViews();

        Chip clearChip = new Chip(this);
        clearChip.setText("Clear");
        clearChip.setCloseIconVisible(true);
        clearChip.setChipBackgroundColorResource(R.color.colorPrimary800);
        clearChip.setTextColor(getResources().getColor(R.color.colorPrimary100));
        topChipGroup.addView(clearChip);

        clearChip.setOnClickListener(v -> {
            isFilter = false;
            topChipGroup.removeAllViews();
            getRealEstates(false);
        });

        if (minPriceFilter != minRealEstate.getPrice()) {
            if (Utils.isConvertedInEuro) {
                topChipGroup.addView(Utils.chipGenerator(String.valueOf((int) minRealEstate.getPrice()), " €", false, true, this));
            } else
                topChipGroup.addView(Utils.chipGenerator(String.valueOf((int) minRealEstate.getPrice()), " $", false, true, this));
        }

        if (maxPriceFilter != maxRealEstate.getPrice()) {
            if (Utils.isConvertedInEuro) {
                topChipGroup.addView(Utils.chipGenerator(String.valueOf((int) maxRealEstate.getPrice()), " €", false, false, this));
            } else
                topChipGroup.addView(Utils.chipGenerator(String.valueOf((int) maxRealEstate.getPrice()), " $", false, false, this));
        }

        if (minSurfaceFilter != minRealEstate.getSurface())
            topChipGroup.addView(Utils.chipGenerator(String.valueOf((int) minRealEstate.getSurface()), "m²", false, true, this));

        if (maxSurfaceFilter != maxRealEstate.getSurface())
            topChipGroup.addView(Utils.chipGenerator(String.valueOf((int) maxRealEstate.getSurface()), "m²", false, false, this));

        if (minRoomsFilter != minRealEstate.getRooms())
            topChipGroup.addView(Utils.chipGenerator(String.valueOf(minRealEstate.getRooms()), " rooms", false, true, this));

        if (maxRoomsFilter != maxRealEstate.getRooms())
            topChipGroup.addView(Utils.chipGenerator(String.valueOf(maxRealEstate.getRooms()), " rooms", false, false, this));

        if (minBathRoomsFilter != minRealEstate.getBathrooms())
            topChipGroup.addView(Utils.chipGenerator(String.valueOf(minRealEstate.getBathrooms()), " bathrooms", false, true, this));

        if (maxBathRoomsFilter != maxRealEstate.getBathrooms())
            topChipGroup.addView(Utils.chipGenerator(String.valueOf(maxRealEstate.getBathrooms()), " bathrooms", false, false, this));

        if (minBedRoomsFilter != minRealEstate.getBedrooms())
            topChipGroup.addView(Utils.chipGenerator(String.valueOf(minRealEstate.getBedrooms()), " bedrooms", false, true, this));

        if (maxBedRoomsFilter != maxRealEstate.getBedrooms())
            topChipGroup.addView(Utils.chipGenerator(String.valueOf(maxRealEstate.getBedrooms()), " bedrooms", false, false, this));

        if (!typeFilter.getText().toString().isEmpty())
            topChipGroup.addView(Utils.chipGenerator(typeFilter.getText().toString(), null, true, false, this));

        if (!cityFilter.getEditableText().toString().isEmpty())
            topChipGroup.addView(Utils.chipGenerator(cityFilter.getText().toString(), null, true, false, this));

        if (soldFilter.isChecked()) {
            topChipGroup.addView(Utils.chipGenerator("Sold", null, true, false, this));
        } else
            topChipGroup.addView(Utils.chipGenerator("Unsold", null, true, false, this));

        for (String poi : pointsOfInterestList) {
            topChipGroup.addView(Utils.chipGenerator(poi, null, true, false, this));
        }
    }

    private void setTopAppBar() {
        searchButton.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
        setSupportActionBar(topAppBar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, topAppBar, R.string.open_drawer, R.string.close_drawer
        );
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setHeader();
    }

    private void setHeader() {
        View header = navigationView.inflateHeaderView(R.layout.header_navigation_drawer);
        headerName = header.findViewById(R.id.user_name);
        headerMail = header.findViewById(R.id.user_mail);

        FirebaseUser firebaseUser = Utils.getFirebaseUser();

        if (firebaseUser != null) {
            headerName.setText(firebaseUser.getDisplayName());
            headerMail.setText(firebaseUser.getEmail());
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.convert:
                convert();
                break;
            case R.id.synchronize:
                break;
            case R.id.logout:
                signOutUserFromFirebase();
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void convert() {
        if (Utils.isConvertedInEuro) {
            Utils.isConvertedInEuro = false;
        } else {
            Utils.isConvertedInEuro = true;
        }
        getRealEstates(true);
    }

    private void signOutUserFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted());
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted() {
        return aVoid -> startAuthenticationActivity();
    }

    private void startAuthenticationActivity() {
        finish();
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    private void setBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.list:
                    searchButton.setVisibility(View.VISIBLE);
                    setListFragment();
                    break;
                case R.id.map:
                    searchButton.setVisibility(View.VISIBLE);
                    setMapsFragment();
                    break;
            }
            return true;
        });
    }

    public void initFragment() {
        listFragment = new RealEstateListFragment();
        mapsFragment = new MapsFragment();
        detailFragment = new DetailFragment();
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }

    public void setListFragment() {
        setFragment(listFragment);
    }

    public void setMapsFragment() {
        setFragment(mapsFragment);
    }

    public void launchDetailFragment(RealEstate realEstate) {
        detailFragment = DetailFragment.newInstance(realEstate.getId());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (findViewById(R.id.fragment_container2) != null) {
            fragmentTransaction.replace(R.id.fragment_container2, detailFragment).commit();
        } else {
            fragmentTransaction.replace(R.id.fragment_container, detailFragment).commit();
            searchButton.setVisibility(View.GONE);
        }
    }
}
