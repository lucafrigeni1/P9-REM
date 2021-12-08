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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.di.Injections;
import com.openclassrooms.realestatemanager.di.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.repository.RealEstateViewModel;
import com.openclassrooms.realestatemanager.ui.activity.CreateActivity;
import com.openclassrooms.realestatemanager.ui.activity.MainActivity;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RealEstateListFragment extends Fragment {

    ImageView emptyList;
    RecyclerView recyclerView;
    RealEstateViewModel viewModel;
    FloatingActionButton addButton;
    RealEstateAdapter realEstateAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_real_estate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(view);
        setViewModel();
        setAddButton();
        viewModel.getRealEstateList().observe(getViewLifecycleOwner(), this::setRealEstateList);
    }

    private void findViewById(View view) {
        emptyList = view.findViewById(R.id.empty_drawable);
        recyclerView = view.findViewById(R.id.real_estate_list);
        addButton = view.findViewById(R.id.add_button);
    }

    private void setViewModel() {
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this.getContext());
        this.viewModel = new ViewModelProvider(this.requireActivity(), viewModelFactory).get(RealEstateViewModel.class);
    }

    public void setRealEstateList(List<RealEstate> realEstateList) {
        Collections.sort(realEstateList, (o1, o2) -> Boolean.compare(o1.isSold(), o2.isSold()));

        realEstateAdapter = new RealEstateAdapter(realEstateList,
                ((MainActivity) Objects.requireNonNull(this.getActivity()))::launchDetailFragment);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(realEstateAdapter);

        if (realEstateList.isEmpty()) emptyList.setVisibility(View.VISIBLE);
    }

    private void setAddButton() {
        addButton.setOnClickListener(v -> {
            if (Utils.isNetworkAvailable()) {
                Intent intent = new Intent(this.getContext(), CreateActivity.class);
                startActivity(intent);
            } else
                Toast.makeText(this.getContext(), R.string.error_no_internet, Toast.LENGTH_LONG).show();
        });
    }
}
