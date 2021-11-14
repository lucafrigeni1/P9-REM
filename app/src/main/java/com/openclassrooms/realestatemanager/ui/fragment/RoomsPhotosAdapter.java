package com.openclassrooms.realestatemanager.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.models.RoomsPhotos;

import java.util.List;

public class RoomsPhotosAdapter extends RecyclerView.Adapter<RoomsPhotosAdapter.RoomsPhotosViewHolder> {

    private final List<RoomsPhotos> roomsPhotosList;
    private final boolean isEditing;

    public RoomsPhotosAdapter(List<RoomsPhotos> roomsPhotosList, boolean isEditing) {
        this.roomsPhotosList = roomsPhotosList;
        this.isEditing = isEditing;
    }

    @NonNull
    @Override
    public RoomsPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_real_estate_photo, parent, false);
        return new RoomsPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsPhotosViewHolder holder, int position) {
        holder.bind(roomsPhotosList.get(position));
    }

    @Override
    public int getItemCount() {
        return roomsPhotosList.size();
    }

    public class RoomsPhotosViewHolder extends RecyclerView.ViewHolder {

        private final ImageView photoView;
        private final TextView descriptionView;
        private final ImageButton deleteButton;

        public RoomsPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.item_photo);
            descriptionView = itemView.findViewById(R.id.item_text_view);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        public void bind(RoomsPhotos roomsPhotos) {
            Glide.with(photoView).load(Utils.setPhotoUrl(roomsPhotos.getUrl())).centerCrop().into(photoView);
            descriptionView.setText(roomsPhotos.getDescription());

            if (!isEditing) deleteButton.setVisibility(View.GONE);
            else deleteButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(v -> {
                roomsPhotosList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
        }
    }
}
