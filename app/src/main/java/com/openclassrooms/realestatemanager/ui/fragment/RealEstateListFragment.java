package com.openclassrooms.realestatemanager.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.di.Injections;
import com.openclassrooms.realestatemanager.di.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.repository.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.activity.CreateActivity;
import com.openclassrooms.realestatemanager.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RealEstateListFragment extends Fragment {

    ImageView emptyList;
    RecyclerView recyclerView;
    RealEstateViewModel viewModel;
    FloatingActionButton addButton;

    List<RealEstate> realEstates = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_real_estate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyList = view.findViewById(R.id.empty_drawable);
        recyclerView = view.findViewById(R.id.real_estate_list);
        addButton = view.findViewById(R.id.add_button);
        setAddButton();
        setViewModel();
        RealEstateAdapter adapter = new RealEstateAdapter(realEstates, ((MainActivity) Objects.requireNonNull(this.getActivity()))::launchDetailFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        if (!realEstates.isEmpty()){
            emptyList.setVisibility(View.GONE);
        }
    }

    public void setRealEstateList(List<RealEstate> realEstateList) {
        realEstates = realEstateList;
    }

    private void setAddButton() {
        addButton.setOnClickListener(v -> {
            if (Utils.isInternetAvailable(Objects.requireNonNull(this.getContext()))) {
                Intent intent = new Intent(this.getContext(), CreateActivity.class);
                startActivity(intent);
            } else
                Toast.makeText(this.getContext(), "aaa", Toast.LENGTH_LONG).show();
        });
    }

    private void setViewModel() {
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this.getContext());
        this.viewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel.class);
    }
}