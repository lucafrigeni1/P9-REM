package com.openclassrooms.realestatemanager.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.RangeSlider;
import com.openclassrooms.realestatemanager.FiltersUtils;
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
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RealEstateViewModel realEstateViewModel;

    MaterialToolbar topAppBar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;

    RealEstateListFragment listFragment;
    MapsFragment mapsFragment;
    DetailFragment detailFragment;
    boolean isMapsFragmentVisible;

    TextView headerName, headerMail;

    ImageButton searchButton, backButton;
    NestedScrollView bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    RangeSlider priceSlider, surfaceSlider, roomsSlider, bathroomsSlider, bedroomsSlider;
    AutoCompleteTextView typeFilter;
    TextView cityFilter;
    ChipGroup bottomSheetChipGroup, topChipGroup;
    CheckBox soldFilter;
    FloatingActionButton filterButton;

    float minPriceFilter, maxPriceFilter, minSurfaceFilter, maxSurfaceFilter, minRoomsFilter, maxRoomsFilter,
            minBathRoomsFilter, maxBathRoomsFilter, minBedRoomsFilter, maxBedRoomsFilter;

    QueryFilter queryFilter;
    boolean isFilterListShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        Utils.isConvertedInEuro = false;
        setBottomNavigation();
        setTopNavigation();
        setViewModel();
        isMapsFragmentVisible = false;
        getRealEstatesForFilters();
        closeDetailFragment();
    }

    private void findViewById() {
        topAppBar = findViewById(R.id.top_app_bar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bot_navigation);
        searchButton = findViewById(R.id.search_button);
        backButton = findViewById(R.id.back_button);
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

        View header = navigationView.inflateHeaderView(R.layout.header_navigation_drawer);
        headerName = header.findViewById(R.id.user_name);
        headerMail = header.findViewById(R.id.user_mail);
    }

    private void setViewModel() {
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this);
        this.realEstateViewModel = new ViewModelProvider(this, viewModelFactory).get(RealEstateViewModel.class);
        //realEstateViewModel.getRealEstateList(null).observe(this, this::databaseSynchronisation);
    }

    private void databaseSynchronisation(List<RealEstate> realEstatesList) {
        realEstateViewModel.synchroniseWithFirebase(realEstatesList);
    }

    private void getRealEstatesForLists(QueryFilter queryFilter) {
        realEstateViewModel.getRealEstateList(queryFilter).observe(this, this::setRealEstates);
    }

    private void getRealEstatesForFilters() {
        realEstateViewModel.getRealEstateList(null).observe(this, this::setFilterBottomSheet);
    }

    public void setRealEstates(List<RealEstate> realEstatesList) {
        initFragment();
        if (isMapsFragmentVisible) setFragment(mapsFragment);
        else setFragment(listFragment);

        mapsFragment.setRealEstates(realEstatesList);
        listFragment.setRealEstateList(realEstatesList);
    }

    private void setFilterBottomSheet(List<RealEstate> realEstatesList) {
        if (!realEstatesList.isEmpty()) {
            minPriceFilter = FiltersUtils.initFiltersValues(realEstatesList, "price", true);
            maxPriceFilter = FiltersUtils.initFiltersValues(realEstatesList, "price", false);
            minSurfaceFilter = FiltersUtils.initFiltersValues(realEstatesList, "surface", true);
            maxSurfaceFilter = FiltersUtils.initFiltersValues(realEstatesList, "surface", false);
            minRoomsFilter = FiltersUtils.initFiltersValues(realEstatesList, "rooms", true);
            maxRoomsFilter = FiltersUtils.initFiltersValues(realEstatesList, "rooms", false);
            minBathRoomsFilter = FiltersUtils.initFiltersValues(realEstatesList, "bathrooms", true);
            maxBathRoomsFilter = FiltersUtils.initFiltersValues(realEstatesList, "bathrooms", false);
            minBedRoomsFilter = FiltersUtils.initFiltersValues(realEstatesList, "bedrooms", true);
            maxBedRoomsFilter = FiltersUtils.initFiltersValues(realEstatesList, "bedrooms", false);

            FiltersUtils.setSlider(priceSlider, minPriceFilter, maxPriceFilter, false, true);
            FiltersUtils.setSlider(surfaceSlider, minSurfaceFilter, maxSurfaceFilter, false, false);
            FiltersUtils.setSlider(roomsSlider, minRoomsFilter, maxRoomsFilter, true, false);
            FiltersUtils.setSlider(bathroomsSlider, minBathRoomsFilter, maxBathRoomsFilter, true, false);
            FiltersUtils.setSlider(bedroomsSlider, minBedRoomsFilter, maxBedRoomsFilter, true, false);
        }

        FiltersUtils.setTypeFilter(typeFilter, this);
        cityFilter.getEditableText().clear();
        soldFilter.setChecked(false);

        for (int i = 0; i < bottomSheetChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) bottomSheetChipGroup.getChildAt(i);
            chip.setChecked(false);
        }

        setList();
        setFilterButton(realEstatesList);
    }

    private void setFilterButton(List<RealEstate> realEstatesList){
        filterButton.setOnClickListener(v -> {
            if (!realEstatesList.isEmpty()) {
                isFilterListShowing = true;
                setList();
            }
        });
    }

    private void setList(){
        if (isFilterListShowing) {
            getFilteredValues();
            getRealEstatesForLists(queryFilter);
        } else
            getRealEstatesForLists(null);
    }

    private void getFilteredValues() {
        float minPrice = priceSlider.getValues().get(0);
        float maxPrice = priceSlider.getValues().get(1);

        List<String> pointsOfInterestList = new ArrayList<>();
        for (int i = 0; i < bottomSheetChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) bottomSheetChipGroup.getChildAt(i);
            if (chip.isChecked()) pointsOfInterestList.add(chip.getText().toString());
        }

        queryFilter = new QueryFilter.Builder()
                .type(String.valueOf(typeFilter.getText()))
                .minPrice((int) minPrice)
                .maxPrice((int) maxPrice)
                .isSold(soldFilter.isChecked())
                .minSurface(surfaceSlider.getValues().get(0))
                .maxSurface(surfaceSlider.getValues().get(1))
                .minRooms(Math.round(roomsSlider.getValues().get(0)))
                .maxRooms(Math.round(roomsSlider.getValues().get(1)))
                .minBathrooms(Math.round(bathroomsSlider.getValues().get(0)))
                .maxBathrooms(Math.round(bathroomsSlider.getValues().get(1)))
                .minBedrooms(Math.round(bedroomsSlider.getValues().get(0)))
                .maxBedrooms(Math.round(bedroomsSlider.getValues().get(1)))
                .pointsOfInterest(pointsOfInterestList)
                .city(String.valueOf(cityFilter.getText()))
                .build();

        setChipGroup(pointsOfInterestList);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @SuppressLint("StringFormatMatches")
    private void setChipGroup(List<String> pointsOfInterestList) {
        topChipGroup.removeAllViews();

        Chip clearChip = new Chip(this);
        clearChip.setText(getString(R.string.clear));
        clearChip.setCloseIconVisible(true);
        clearChip.setChipBackgroundColorResource(R.color.colorPrimary400);
        clearChip.setTextColor(getResources().getColor(R.color.colorPrimary800));
        topChipGroup.addView(clearChip);

        clearChip.setOnClickListener(v -> {
            topChipGroup.removeAllViews();
            isFilterListShowing = false;
            getRealEstatesForLists(null);
            getRealEstatesForFilters();
        });

        if (minPriceFilter != queryFilter.getMinPrice()) {
            if (Utils.isConvertedInEuro) {
                topChipGroup.addView(FiltersUtils.chipGenerator(getString(R.string.euro_price,
                        NumberFormat.getNumberInstance(Locale.FRANCE).format(queryFilter.getMinPrice())), true,  this));
            } else
                topChipGroup.addView(FiltersUtils.chipGenerator(getString(R.string.dollar_price,
                        NumberFormat.getNumberInstance(Locale.FRANCE).format(queryFilter.getMinPrice())), true,  this));
        }

        if (maxPriceFilter != queryFilter.getMaxPrice()) {
            if (Utils.isConvertedInEuro) {
                topChipGroup.addView(FiltersUtils.chipGenerator(getString(R.string.euro_price,
                        NumberFormat.getNumberInstance(Locale.FRANCE).format(queryFilter.getMaxPrice())), false,  this));
            } else
                topChipGroup.addView(FiltersUtils.chipGenerator(getString(R.string.dollar_price,
                        NumberFormat.getNumberInstance(Locale.FRANCE).format(queryFilter.getMaxPrice())), false,  this));
        }

        if (minSurfaceFilter != queryFilter.getMinSurface())
            topChipGroup.addView(FiltersUtils.chipGenerator(
                    getString(R.string.surface, String.valueOf((int) queryFilter.getMinSurface())), true, this));

        if (maxSurfaceFilter != queryFilter.getMaxSurface())
            topChipGroup.addView(FiltersUtils.chipGenerator(
                    getString(R.string.surface, String.valueOf((int) queryFilter.getMaxSurface())), false, this));

        if (minRoomsFilter != queryFilter.getMinRooms())
            topChipGroup.addView(FiltersUtils.chipGenerator(
                    getString(R.string.rooms, String.valueOf(queryFilter.getMinRooms())), true, this));

        if (maxRoomsFilter != queryFilter.getMaxRooms())
            topChipGroup.addView(FiltersUtils.chipGenerator(
                    getString(R.string.rooms, String.valueOf(queryFilter.getMaxRooms())), false, this));

        if (minBathRoomsFilter != queryFilter.getMinBathrooms())
            topChipGroup.addView(FiltersUtils.chipGenerator(
                    getString(R.string.bathrooms, String.valueOf(queryFilter.getMinBathrooms())),true, this));

        if (maxBathRoomsFilter != queryFilter.getMaxBathrooms())
            topChipGroup.addView(FiltersUtils.chipGenerator(
                    getString(R.string.bathrooms, String.valueOf(queryFilter.getMaxBathrooms())),false, this));

        if (minBedRoomsFilter != queryFilter.getMinBedrooms())
            topChipGroup.addView(FiltersUtils.chipGenerator(
                    getString(R.string.bedrooms, String.valueOf(queryFilter.getMinBedrooms())),true, this));

        if (maxBedRoomsFilter != queryFilter.getMaxBedrooms())
            topChipGroup.addView(FiltersUtils.chipGenerator(
                    getString(R.string.bedrooms, String.valueOf(queryFilter.getMaxBedrooms())),false, this));

        if (!typeFilter.getText().toString().isEmpty())
            topChipGroup.addView(FiltersUtils.chipGenerator(typeFilter.getText().toString(),null, this));

        if (!cityFilter.getEditableText().toString().isEmpty())
            topChipGroup.addView(FiltersUtils.chipGenerator(cityFilter.getText().toString(),null, this));

        if (soldFilter.isChecked()) {
            topChipGroup.addView(FiltersUtils.chipGenerator(getString(R.string.sold), null,this));
        } else
            topChipGroup.addView(FiltersUtils.chipGenerator(getString(R.string.unsold), null,this));

        for (String poi : pointsOfInterestList) {
            topChipGroup.addView(FiltersUtils.chipGenerator(poi, null, this));
        }
    }

    private void setTopNavigation() {
        setSupportActionBar(topAppBar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                topAppBar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        headerName.setText(Utils.getFirebaseUser().getDisplayName());
        headerMail.setText(Utils.getFirebaseUser().getEmail());
        navigationView.setNavigationItemSelectedListener(this);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        searchButton.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.convert:
                Utils.isConvertedInEuro = !Utils.isConvertedInEuro;
                getRealEstatesForFilters();
                break;
            case R.id.logout:
                signOutUserFromFirebase();
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOutUserFromFirebase() {
        AuthUI.getInstance().signOut(this).addOnSuccessListener(this, aVoid -> {
            finish();
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void setBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.list:
                    searchButton.setVisibility(View.VISIBLE);
                    setFragment(listFragment);
                    isMapsFragmentVisible = false;
                    break;
                case R.id.map:
                    searchButton.setVisibility(View.VISIBLE);
                    setFragment(mapsFragment);
                    isMapsFragmentVisible = true;
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

    public void launchDetailFragment(RealEstate realEstate) {
        detailFragment = DetailFragment.newInstance(realEstate.getId());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (findViewById(R.id.fragment_container2) != null) {
            fragmentTransaction.replace(R.id.fragment_container2, detailFragment).commit();
        } else {
            fragmentTransaction.replace(R.id.fragment_container, detailFragment).commit();

            topAppBar.setVisibility(View.GONE);
            backButton.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.GONE);
            topChipGroup.setVisibility(View.GONE);
        }
    }

    private void closeDetailFragment() {
        backButton.setOnClickListener(v -> {
            if (isMapsFragmentVisible) setFragment(mapsFragment);
            else setFragment(listFragment);

            topAppBar.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            topChipGroup.setVisibility(View.VISIBLE);
        });
    }

}
