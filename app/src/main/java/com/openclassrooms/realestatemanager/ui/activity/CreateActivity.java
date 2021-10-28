package com.openclassrooms.realestatemanager.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.Notifications;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.di.Injections;
import com.openclassrooms.realestatemanager.di.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RoomsPhotos;
import com.openclassrooms.realestatemanager.repository.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.fragment.RoomsPhotosAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CreateActivity extends AppCompatActivity {

    public static final String EXTRA_REAL_ESTATE = "RealEstateId";

    String STORAGE_PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    String CAMERA_PERMS = Manifest.permission.CAMERA;
    private final static int RC_MAIN_FILE = 100;
    private final static int RC_MAIN_CAMERA = 200;
    private final static int RC_ROOM_FILE = 300;
    private final static int RC_ROOM_CAMERA = 400;

    Uri uri;
    String mainPicture;

    RealEstateViewModel realEstateViewModel;

    List<String> pointsOfInterestList = new ArrayList<>();

    ImageButton backButton, validationButton;

    AutoCompleteTextView typeInput;
    TextInputEditText priceInput, streetInput, cityInput, postalCodeInput, countryInput, surfaceInput,
            roomsInput, bathroomsInput, bedroomsInput, descriptionInput, roomPhotoDescriptionInput;

    ChipGroup chipGroup;

    Button mainPhotoButton, mainFileButton;
    ImageView mainPhoto;
    ImageButton deleteMainPhotoButton;

    Button roomsPhotoButton, roomsFileButton;
    RecyclerView roomsPhotosRV;
    List<RoomsPhotos> roomsPhotosList = new ArrayList<>();
    RoomsPhotosAdapter roomsPhotosAdapter;

    CheckBox soldCheckBox;


    boolean isSold;
    String saleDate;

    boolean isCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        findViewById();
        setViewModel();
        setBackButton();
        setMainPhotoButton();
        setMainFileButton();
        initListRoomsPhotos();
        getRealEstate();
        setRoomsPhotoButton();
        setRoomsFileButton();
        setDeleteMainPhotoButton();
    }

    private void getRealEstate() {
        String id = getIntent().getStringExtra(EXTRA_REAL_ESTATE);
        if (id != null) {
            realEstateViewModel.getRealEstate(id).observe(this, this::setView);
        } else
            setView(null);
    }

    private void findViewById() {
        backButton = findViewById(R.id.back_button);
        typeInput = findViewById(R.id.type_input);
        priceInput = findViewById(R.id.price_input);
        streetInput = findViewById(R.id.street_input);
        cityInput = findViewById(R.id.city_input);
        postalCodeInput = findViewById(R.id.postal_code_input);
        countryInput = findViewById(R.id.country_input);
        surfaceInput = findViewById(R.id.surface_input);
        roomsInput = findViewById(R.id.rooms_input);
        bathroomsInput = findViewById(R.id.bathrooms_input);
        bedroomsInput = findViewById(R.id.bedrooms_input);
        descriptionInput = findViewById(R.id.description_input);
        mainPhotoButton = findViewById(R.id.main_photo_button);
        mainFileButton = findViewById(R.id.main_file_button);
        roomPhotoDescriptionInput = findViewById(R.id.room_description_input);
        roomsPhotoButton = findViewById(R.id.room_photo_button);
        roomsFileButton = findViewById(R.id.room_file_button);
        roomsPhotosRV = findViewById(R.id.photo_recyclerview);
        mainPhoto = findViewById(R.id.real_estate_photo);
        deleteMainPhotoButton = findViewById(R.id.delete_button);
        soldCheckBox = findViewById(R.id.checkbox);
        validationButton = findViewById(R.id.validation_button);
        chipGroup = findViewById(R.id.POI_chip_group);
    }

    private void setViewModel() {
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this);
        this.realEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }

    private void setView(RealEstate realEstate) {
        if (realEstate != null) {
            typeInput.setText(realEstate.getType());
            priceInput.setText(String.valueOf(realEstate.getDollarPrice()));
            streetInput.setText(realEstate.getStreet());
            cityInput.setText(realEstate.getCity());
            postalCodeInput.setText(realEstate.getPostalCode());
            countryInput.setText(realEstate.getCountry());
            surfaceInput.setText(String.valueOf(realEstate.getSurface()));
            roomsInput.setText(String.valueOf(realEstate.getRooms()));
            bathroomsInput.setText(String.valueOf(realEstate.getBathrooms()));
            bedroomsInput.setText(String.valueOf(realEstate.getBedrooms()));
            descriptionInput.setText(realEstate.getDescriptions());
            Glide.with(this)
                    .load(Utils.setUrl(realEstate.getMainPhoto()))
                    .centerCrop()
                    .into(mainPhoto);
            setViewForMainPhoto();
            roomsPhotosList.addAll(realEstate.getRoomsPhotosList());
            roomsPhotosRV.setVisibility(View.VISIBLE);

            if (realEstate.isSold())
                soldCheckBox.setVisibility(View.GONE);


            setChips(realEstate);
            setValidationButton(realEstate);
        } else {
            soldCheckBox.setVisibility(View.GONE);
            setChips(null);
            setValidationButton(null);
        }
        setTypeInput();
    }

    private void setBackButton() {
        backButton.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    private void setTypeInput() {
        String[] types = new String[]{"HOUSE", "FLAT", "STUDIO", "DUPLEX", "TRIPLEX"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, types);
        typeInput.setAdapter(adapter);
        typeInput.setDropDownBackgroundResource(R.color.colorPrimary800);
    }

    private void setChips(RealEstate realEstate) {
        if (realEstate != null) {
            for (int z = 0; z < chipGroup.getChildCount(); z++) {
                Chip chip = (Chip) chipGroup.getChildAt(z);
                if (realEstate.getPointsOfInterest().contains(chip.getText())) {
                    chip.setChecked(true);
                }
            }
        }

        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked())
                pointsOfInterestList.add(chip.getText().toString());

            chip.setOnCheckedChangeListener((view, isChecked) -> {
                if (isChecked) {
                    pointsOfInterestList.add(view.getText().toString());
                } else
                    pointsOfInterestList.remove(String.valueOf(view.getText()));
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void setMainPhotoButton() {
        mainPhotoButton.setOnClickListener(v -> cameraMainPermission());
    }

    @AfterPermissionGranted(RC_MAIN_CAMERA)
    public void cameraMainPermission() {
        if (!EasyPermissions.hasPermissions(this, CAMERA_PERMS)) {
            EasyPermissions.requestPermissions(this, "a", RC_MAIN_CAMERA, CAMERA_PERMS);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, RC_MAIN_CAMERA);
        }
    }

    private void setMainFileButton() {
        mainFileButton.setOnClickListener(v -> fileMainPermission());
    }

    @AfterPermissionGranted(RC_MAIN_FILE)
    public void fileMainPermission() {
        if (!EasyPermissions.hasPermissions(this, STORAGE_PERMS)) {
            EasyPermissions.requestPermissions(this, "b", RC_MAIN_FILE, STORAGE_PERMS);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RC_MAIN_FILE);
        }
    }

    private void setRoomsPhotoButton() {
        roomsPhotoButton.setOnClickListener(v -> cameraRoomPermission());
    }

    @AfterPermissionGranted(RC_ROOM_CAMERA)
    public void cameraRoomPermission() {
        if (!EasyPermissions.hasPermissions(this, CAMERA_PERMS)) {
            EasyPermissions.requestPermissions(this, "a", RC_ROOM_CAMERA, CAMERA_PERMS);
        } else {
            if (!roomPhotoDescriptionInput.getText().toString().isEmpty()) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RC_ROOM_CAMERA);
            } else
                Toast.makeText(this, "Complete the field first", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRoomsFileButton() {
        roomsFileButton.setOnClickListener(v -> fileRoomPermission());
    }

    @AfterPermissionGranted(RC_ROOM_FILE)
    public void fileRoomPermission() {
        if (!EasyPermissions.hasPermissions(this, STORAGE_PERMS)) {
            EasyPermissions.requestPermissions(this, "b", RC_ROOM_FILE, STORAGE_PERMS);
        } else {
            if (!roomPhotoDescriptionInput.getText().toString().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RC_ROOM_FILE);
            } else
                Toast.makeText(this, "Complete the field first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_MAIN_FILE) {
            if (resultCode == RESULT_OK) {
                uri = data.getData();
                realEstateViewModel.uploadImageInStorage(uri).observe(this, uuid -> {
                    mainPicture = uuid;
                    Glide.with(this).load(Utils.setUrl(mainPicture)).into(mainPhoto);
                });
                setViewForMainPhoto();
            } else
                Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show();

        } else if (requestCode == RC_MAIN_CAMERA) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                uri = Utils.convertBitmapToUri(bitmap, this);
                realEstateViewModel.uploadImageInStorage(uri).observe(this, uuid -> {
                    mainPicture = uuid;
                    Glide.with(this).load(Utils.setUrl(mainPicture)).into(mainPhoto);
                });
                setViewForMainPhoto();
            } else
                Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show();

        } else if (requestCode == RC_ROOM_FILE) {
            if (resultCode == RESULT_OK) {
                realEstateViewModel.uploadImageInStorage(data.getData()).observe(this, uuid -> {
                    RoomsPhotos roomsPhotos = new RoomsPhotos(uuid,
                            Objects.requireNonNull(roomPhotoDescriptionInput.getText()).toString()
                    );
                    addRoomsPhotos(roomsPhotos);
                    roomPhotoDescriptionInput.getText().clear();
                });
                roomsPhotosRV.setVisibility(View.VISIBLE);
            } else
                Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show();

        } else if (requestCode == RC_ROOM_CAMERA) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                realEstateViewModel.uploadImageInStorage(Utils.convertBitmapToUri(bitmap, this)).observe(this, uuid -> {
                    RoomsPhotos roomsPhotos = new RoomsPhotos(uuid,
                            roomPhotoDescriptionInput.getText().toString()
                    );
                    Log.e("handleResponse: ", roomsPhotos.getDescription().toString());
                    addRoomsPhotos(roomsPhotos);
                    roomPhotoDescriptionInput.getText().clear();
                });
                roomsPhotosRV.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setViewForMainPhoto() {
        mainPhotoButton.setVisibility(View.GONE);
        mainFileButton.setVisibility(View.GONE);
        mainPhoto.setVisibility(View.VISIBLE);
        deleteMainPhotoButton.setVisibility(View.VISIBLE);
    }

    private void initListRoomsPhotos() {
        roomsPhotosAdapter = new RoomsPhotosAdapter(roomsPhotosList, true);
        roomsPhotosRV.setAdapter(roomsPhotosAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addRoomsPhotos(RoomsPhotos roomsPhotos) {
        roomsPhotosList.add(roomsPhotos);
        roomsPhotosAdapter.notifyDataSetChanged();
    }

    private void setDeleteMainPhotoButton() {
        deleteMainPhotoButton.setOnClickListener(v -> {
            mainPhotoButton.setVisibility(View.VISIBLE);
            mainFileButton.setVisibility(View.VISIBLE);
            mainPhoto.setImageDrawable(null);
            mainPhoto.setVisibility(View.GONE);
            deleteMainPhotoButton.setVisibility(View.GONE);
        });
    }

    private void setValidationButton(RealEstate realEstate) {
        String id;
        String recordDate;

        if (realEstate == null) {
            id = UUID.randomUUID().toString();
            recordDate = Utils.getTodayDate();
            isCreate = true;
        } else {
            id = realEstate.getId();
            recordDate = realEstate.getRecordDate();
            mainPicture = realEstate.getMainPhoto();
            isCreate = false;
        }

        validationButton.setOnClickListener(v -> {
            if (String.valueOf(typeInput.getText()).equals("")
                    || String.valueOf(descriptionInput.getText()).equals("")
                    || mainPicture == null
                    || String.valueOf(priceInput.getText()).equals("")
                    || String.valueOf(surfaceInput.getText()).equals("")
                    || roomsPhotosList.size() == 0
                    || String.valueOf(roomsInput.getText()).equals("")
                    || String.valueOf(bathroomsInput.getText()).equals("")
                    || String.valueOf(bedroomsInput.getText()).equals("")
                    || String.valueOf(streetInput.getText()).equals("")
                    || String.valueOf(cityInput.getText()).equals("")
                    || String.valueOf(postalCodeInput.getText()).equals("")
                    || String.valueOf(countryInput.getText()).equals("")
            ) {
                Toast.makeText(this, "some fields are empty", Toast.LENGTH_LONG).show();
            } else {
                String type = String.valueOf(typeInput.getText());
                String description = String.valueOf(descriptionInput.getText());
                int dollarPrice = Integer.parseInt(String.valueOf(priceInput.getText()));
                double surface = Double.parseDouble(String.valueOf(surfaceInput.getText()));
                int rooms = Integer.parseInt(String.valueOf(roomsInput.getText()));
                int bathrooms = Integer.parseInt(String.valueOf(bathroomsInput.getText()));
                int bedrooms = Integer.parseInt(String.valueOf(bedroomsInput.getText()));
                String street = String.valueOf(streetInput.getText());
                String city = String.valueOf(cityInput.getText());
                String postalCode = String.valueOf(postalCodeInput.getText());
                String country = String.valueOf(countryInput.getText());
                double latitude = Utils.geolocate(this, (street + ", " + city)).getLatitude();
                double longitude = Utils.geolocate(this, (street + ", " + city)).getLongitude();

                if (realEstate != null && realEstate.isSold()) {
                    saleDate = realEstate.getSaleDate();
                    isSold = realEstate.isSold();
                } else if (realEstate != null && soldCheckBox.isChecked()) {
                    saleDate = Utils.getTodayDate();
                    isSold = true;
                } else {
                    isSold = false;
                    saleDate = "";
                }

                if (latitude != 0) {
                    RealEstate newRealEstate = new RealEstate.Builder(id)
                            .type(type)
                            .descriptions(description)
                            .mainPhoto(mainPicture)
                            .dollarPrice(dollarPrice)
                            .euroPrice(Utils.convertDollarToEuro((int) dollarPrice))
                            .isSold(isSold)
                            .surface(surface)
                            .roomsPhotosList(roomsPhotosList)
                            .rooms(rooms)
                            .bathrooms(bathrooms)
                            .bedrooms(bedrooms)
                            .pointsOfInterest(pointsOfInterestList)
                            .street(street)
                            .city(city)
                            .postalCode(postalCode)
                            .country(country)
                            .latitude(latitude)
                            .longitude(longitude)
                            .recordDate(recordDate)
                            .saleDate(saleDate)
                            .lastEditDate(Utils.getTodayDate())
                            .estateAgent(Utils.getFirebaseUser().getDisplayName())
                            .build();
                    realEstateViewModel.createRealEstate(newRealEstate);
                    sendNotification();
                    finish();
                    Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(this, "check the adress", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendNotification() {
        Notifications.launchWorker(this, isCreate);
    }
}
