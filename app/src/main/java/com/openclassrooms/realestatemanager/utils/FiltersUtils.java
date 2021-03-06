package com.openclassrooms.realestatemanager.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.slider.RangeSlider;
import com.openclassrooms.realestatemanager.App;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstate;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

public class FiltersUtils {

    public static float initFiltersValues(List<RealEstate> realEstatesList, String comparator, boolean isMinimum) {
        int lastItem = realEstatesList.size() - 1;
        float value = 0;
        switch (comparator) {
            case "price":
                Collections.sort(realEstatesList, new RealEstate.PriceComparator());
                if (isMinimum) {
                    if (Utils.isConvertedInEuro) value = realEstatesList.get(0).getEuroPrice();
                    else value = realEstatesList.get(0).getDollarPrice();
                } else {
                    if (Utils.isConvertedInEuro) value = realEstatesList.get(lastItem).getEuroPrice() + 1;
                    else value = realEstatesList.get(lastItem).getDollarPrice() + 1;
                }
                break;

            case "surface":
                Collections.sort(realEstatesList, new RealEstate.SurfaceComparator());
                if (isMinimum) value = (float) realEstatesList.get(0).getSurface();
                else value = (float) realEstatesList.get(lastItem).getSurface() + 1;
                break;

            case "rooms":
                Collections.sort(realEstatesList, new RealEstate.RoomsComparator());
                if (isMinimum) value = realEstatesList.get(0).getRooms();
                else value = realEstatesList.get(lastItem).getRooms() + 1;
                break;

            case "bathrooms":
                Collections.sort(realEstatesList, new RealEstate.BathRoomsComparator());
                if (isMinimum) value = realEstatesList.get(0).getBathrooms();
                else value = realEstatesList.get(lastItem).getBathrooms() + 1;
                break;

            case "bedrooms":
                Collections.sort(realEstatesList, new RealEstate.BedRoomsComparator());
                if (isMinimum) value = realEstatesList.get(0).getBedrooms();
                else value = realEstatesList.get(lastItem).getBedrooms() + 1;
                break;
        }
        return value;
    }

    public static void setSlider(RangeSlider slider, float minValue, float maxValue, boolean isStep, boolean isFormat) {
        slider.setValueFrom(minValue);
        slider.setValueTo(maxValue);
        slider.setValues(minValue, maxValue);
        if (isStep) slider.setStepSize(1);
        if (isFormat) {
            slider.setLabelFormatter(value -> {
                NumberFormat currencyformat = NumberFormat.getCurrencyInstance();
                if (Utils.isConvertedInEuro) {
                    currencyformat.setCurrency(Currency.getInstance("EUR"));
                } else
                    currencyformat.setCurrency(Currency.getInstance("USD"));
                return currencyformat.format(value);
            });
        }
    }

    public static void setTypeFilter(Context context, AutoCompleteTextView typeFilter) {
        typeFilter.getText().clear();
        String[] types = new String[]{"HOUSE", "FLAT", "STUDIO", "DUPLEX", "TRIPLEX"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_item, types);
        typeFilter.setAdapter(adapter);
        typeFilter.setDropDownBackgroundResource(R.color.colorPrimary800);
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    public static Chip chipGenerator(String chipText, Boolean isUpper, Context context) {
        Chip chip = new Chip(context);

        if (isUpper != null){
            if (isUpper) chip.setText(context.getString(R.string.upper, chipText));
            else chip.setText(context.getString(R.string.smaller, chipText));
        } else chip.setText(chipText);

        chip.setChipBackgroundColorResource(R.color.colorPrimary800);
        chip.setTextColor(R.color.colorPrimary50);
        return chip;
    }

    public static String searchPOI(List<String> POI){
        StringBuilder searchPOI = new StringBuilder("%");
        for (String poi : POI){
            searchPOI.append(poi).append("%");
        }
        return searchPOI.toString();
    }
}
