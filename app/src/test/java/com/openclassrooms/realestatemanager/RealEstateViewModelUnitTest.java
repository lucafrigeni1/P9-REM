package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.repository.RealEstateDataRepository;
import com.openclassrooms.realestatemanager.repository.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@RunWith(JUnit4.class)
public class RealEstateViewModelUnitTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    RealEstateViewModel viewModel;

    @Mock
    RealEstateDataRepository dataRepository;
    Executor executor;

    List<RealEstate> realEstateList = new ArrayList<>();

    RealEstate realEstateTest = new RealEstate.Builder(0)
            .type("HOUSE")
            .descriptions("description")
            .mainPhoto("mainPicture")
            .dollarPrice(1000).euroPrice(Utils.convertDollarToEuro(1000)).isSold(false)
            .surface(100)
            .roomsPhotosList(null)
            .rooms(3).bathrooms(1).bedrooms(1)
            .pointsOfInterest(null)
            .street("street").city("city").postalCode("postalCode").country("country")
            .latitude(0).longitude(0)
            .recordDate(Utils.getTodayDate()).saleDate(null).estateAgent("User")
            .build();

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        viewModel = new RealEstateViewModel(dataRepository, executor);
    }

    @Test
    public void getRealEstateWithSuccess() {
        MutableLiveData<RealEstate> data = new MutableLiveData<>();
        data.postValue(realEstateTest);

        when(dataRepository.getRealEstate(0)).thenReturn(data);
        viewModel.getRealEstate(0).observeForever(realEstate -> assertEquals(realEstate, realEstateTest));
    }

    @Test
    public void getRealEstateListWithSuccess() {
        realEstateList.add(realEstateTest);
        MutableLiveData<List<RealEstate>> data = new MutableLiveData<>();
        data.postValue(realEstateList);

        when(dataRepository.getRealEstateList(executor)).thenReturn(data);
        viewModel.getRealEstateList(true)
                .observeForever(realEstates -> assertEquals(realEstates, realEstateList));
    }
}
