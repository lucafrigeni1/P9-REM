package com.openclassrooms.realestatemanager.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;
import com.openclassrooms.realestatemanager.models.RealEstate;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RealEstateAdapter extends RecyclerView.Adapter<RealEstateAdapter.RealEstateViewHolder> {

    private final OnItemClickListener listener;
    private final List<RealEstate> realEstateList;
    private int selectedPosition = -1;

    interface OnItemClickListener {
        void onItemClick(RealEstate realEstate);
    }

    public RealEstateAdapter(List<RealEstate> realEstateList, OnItemClickListener listener) {
        this.realEstateList = realEstateList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RealEstateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_real_estate, parent, false);
        return new RealEstateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RealEstateViewHolder holder, int position) {
        holder.bind(realEstateList.get(position), listener);
        if (selectedPosition == position) {
            holder.item.setBackgroundResource(R.color.colorPrimary700);
        }
    }

    @Override
    public int getItemCount() {
        return realEstateList.size();
    }

    public class RealEstateViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout item;
        private final ImageView picture, soldIcon;
        private final TextView type, location, price;

        public RealEstateViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            picture = itemView.findViewById(R.id.real_estate_picture);
            soldIcon = itemView.findViewById(R.id.sold);
            type = itemView.findViewById(R.id.real_estate_type);
            location = itemView.findViewById(R.id.real_estate_location);
            price = itemView.findViewById(R.id.real_estate_price);
        }

        public void bind(RealEstate realEstate, OnItemClickListener listener) {
            Glide.with(picture).load(Utils.setPhotoUrl(realEstate.getMainPhoto())).centerCrop().into(picture);
            type.setText(realEstate.getType());
            location.setText(itemView.getContext().getString(R.string.location, realEstate.getStreet(), realEstate.getCity()));

            if (!realEstate.isSold()) soldIcon.setBackgroundResource(R.drawable.ic_baseline_not_sell_24);
            else soldIcon.setBackgroundResource(R.drawable.ic_baseline_sell_24);

            if (Utils.isConvertedInEuro) {
                price.setText(itemView.getContext().getString(R.string.euro_price_list,
                        NumberFormat.getNumberInstance(Locale.FRANCE).format(realEstate.getEuroPrice())));
            } else {
                price.setText(itemView.getContext().getString(R.string.dollar_price_list,
                        NumberFormat.getNumberInstance(Locale.FRANCE).format(realEstate.getDollarPrice())));
            }

            item.setOnClickListener(v -> {
                listener.onItemClick(realEstate);
                notifyItemChanged(selectedPosition);
                selectedPosition = getAdapterPosition();
                notifyItemChanged(selectedPosition);
            });
        }
    }
}
