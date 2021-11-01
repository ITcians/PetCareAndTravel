package com.pet.app.controller.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.pet.app.R;
import com.pet.app.models.PetModel;
import com.pet.app.views.hegiene.HegieneActivity;
import com.ramotion.foldingcell.FoldingCell;

import java.util.List;
import java.util.Locale;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetHolder> {

    private final Context context;
    private final List<PetModel> pets;

    public PetAdapter(Context context, List<PetModel> pets) {
        this.context = context;
        this.pets = pets;
    }


    @NonNull
    @Override
    public PetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PetHolder(LayoutInflater.from(context).inflate(R.layout.pet_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PetHolder holder, int position) {
        PetModel pet = pets.get(position);
        Glide.with(context).load(pet.getPetImage()).fitCenter().into(holder.petImage);
        holder.title.setText(pet.getPetName());
        holder.address.setText(pet.getPetAddress());
        holder.price.setText(String.valueOf(pet.getPetPrice()));
        holder.foldingCell.setOnClickListener(view -> {
            FoldingCell cell = (FoldingCell) view;
            cell.toggle(false);
            setChild(holder, pet);
        });
        holder.open.setOnClickListener(view -> {
            holder.foldingCell.toggle(false);
            setChild(holder, pet);
        });
    }


    @Override
    public int getItemCount() {
        return pets.size();
    }


    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    void setChild(PetHolder petHolder, PetModel pet) {
        if (pet != null) {
            petHolder.age.setText(String.valueOf(pet.getPetAge()));
            petHolder.specie.setText(String.valueOf(pet.getPetSpecie()));
            petHolder.gender.setText(String.valueOf(pet.getPetGender()));
            petHolder.height.setText(String.valueOf(pet.getPetHeight()));
            petHolder.weight.setText(String.valueOf(pet.getPetWeight()));
            petHolder.bodyPrice.setText(String.valueOf(pet.getPetPrice()));

            petHolder.location.setText(pet.getLat() + "," + pet.getLang());
            petHolder.location.setOnClickListener(view1 -> {
                //open map
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", pet.getLat(),
                        pet.getLang());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            });

            TextView name = petHolder.bodyHead.findViewById(R.id.petHeaderName);
            name.setText(String.valueOf(pet.getPetName()));

            TextView address = petHolder.bodyHead.findViewById(R.id.petHeaderAddress);
            address.setText(String.valueOf(pet.getPetAddress()));

            TextView price = petHolder.bodyHead.findViewById(R.id.petHeaderPrice);
            price.setText(String.valueOf(pet.getPetPrice()));
            ImageButton btn = petHolder.bodyHead.findViewById(R.id.petHeaderNext);
            Glide.with(context).load(ActivityCompat.getDrawable(context,
                    R.drawable.ic_baseline_arrow_drop_up_24)).into(btn);
            Glide.with(context).load(pet.getPetImage()).fitCenter().into((ImageView)
                    petHolder.bodyHead.findViewById(R.id.petHeaderImage));
            btn.setOnClickListener(view -> petHolder.foldingCell.fold(false));

            if (pet.getUserToken() != null && pet.getUserToken().equals("sd")) {
                //replace with edit
                petHolder.buyButton.setText("Manage");
                petHolder.call.setIcon(ActivityCompat.getDrawable(context, R.drawable.ic_baseline_edit_24));
                petHolder.buyButton.setOnClickListener(view -> ActivityCompat.startActivity(context,
                        new Intent(context, HegieneActivity.class), null));
            }
        }

    }


    static class PetHolder extends RecyclerView.ViewHolder {
        private final FoldingCell foldingCell;
        private FrameLayout mainFrame, bodyFrame;
        private final TextView title, address, price, bodyPrice, location, specie, gender, height, weight, age;
        private final ImageView petImage;
        private final ImageButton open;
        private View bodyHead;
        private final ExtendedFloatingActionButton buyButton;
        private ExtendedFloatingActionButton call;

        public PetHolder(@NonNull View itemView) {
            super(itemView);
            foldingCell = itemView.findViewById(R.id.petFoldingCell);
            mainFrame = itemView.findViewById(R.id.petCardHeaderFrame);
            bodyFrame = itemView.findViewById(R.id.petCardBodyFrame);

            View body = itemView.findViewById(R.id.petCardBodyLayout);
            bodyPrice = body.findViewById(R.id.petBodyPrice);
            location = body.findViewById(R.id.petBodyLocation);
            buyButton = body.findViewById(R.id.petBodyBuy);
            call = body.findViewById(R.id.petBodyCall);
            specie = body.findViewById(R.id.petBodySpecie);
            gender = body.findViewById(R.id.petBodyGender);
            height = body.findViewById(R.id.petBodyHeight);
            weight = body.findViewById(R.id.petBodyWeight);
            age = body.findViewById(R.id.petBodyAge);
            bodyHead = body.findViewById(R.id.petBodyHead);

            View head = itemView.findViewById(R.id.petCardHeadLayout);
            title = head.findViewById(R.id.petHeaderName);
            address = head.findViewById(R.id.petHeaderAddress);
            price = head.findViewById(R.id.petHeaderPrice);
            petImage = head.findViewById(R.id.petHeaderImage);
            open = head.findViewById(R.id.petHeaderNext);
            body.findViewById(R.id.petBodyCardClose)
                    .setOnClickListener(view -> foldingCell.fold(false));
        }
    }
}
