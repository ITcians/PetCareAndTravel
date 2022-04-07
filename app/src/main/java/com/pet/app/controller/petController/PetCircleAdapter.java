package com.pet.app.controller.petController;

import android.app.Activity;
import android.content.Intent;
import android.provider.AlarmClock;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pet.app.R;
import com.pet.app.models.PetModel;
import com.pet.app.resources.UserSession;
import com.pet.app.views.hegiene.HegieneActivity;
import com.pet.app.views.pets.EditPetActivity;
import com.pet.app.views.travel.TravelActivity;

import org.json.JSONException;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PetCircleAdapter extends RecyclerView.Adapter<PetCircleAdapter.PetCircleHolder> {

    Activity context;
    List<PetModel> pets;

    public PetCircleAdapter(Activity context, List<PetModel> pets) {
        this.context = context;
        this.pets = pets;
    }

    @NonNull
    @Override
    public PetCircleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PetCircleHolder(LayoutInflater.from(context).inflate(R.layout.pet_card_circle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PetCircleHolder holder, int position) {
        PetModel petModel = pets.get(position);
        Glide.with(context).load(petModel.getPetImage()).into(holder.image);
        holder.title.setText(String.valueOf(petModel.getPetName()));
        holder.title.setOnClickListener(view -> {
            //open editing activity
            Intent intent = new Intent(context, EditPetActivity.class);
            try {
                intent.putExtra("pet", petModel.toJson().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ActivityCompat.startActivity(context, intent, null);
        });
        holder.hygieneButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, HegieneActivity.class);
            intent.putExtra("petId", String.valueOf(petModel.getId()));
            ActivityCompat.startActivity(context, intent, null);
        });
        holder.alarmButton.setOnClickListener(view -> {
            Intent openClockIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
            openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(openClockIntent);
        });
        holder.travelButton.setOnClickListener(v -> {
            TravelActivity.petModel = petModel;
            Intent intent = new Intent(context, TravelActivity.class);
            ActivityCompat.startActivity(context, intent, null);
        });

        //buyer seller logic
        if (UserSession.getSession(context).getAccountType().equals("Buyer")) {
            //disable selling options
            holder.sellSwitch.setVisibility(View.GONE);
        } else {
            if (Double.parseDouble(petModel.getPetPrice()) != 0) {
                //not for sale
                holder.sellSwitch.setChecked(true);
                holder.sellSwitch.setText("Ad is live with price PKR" + petModel.getPetPrice());
            }
            holder.sellSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    //ask for price and publish
                    askForPrice(petModel, holder);
                } else {
                    //disable
                    // set price to 0
                    petModel.setPetPrice(String.valueOf(0));
                    petModel.setPetImage(null);
                    holder.sellSwitch.setText(context.getString(R.string.sell_this_pet));
                    new PetController(context).updatePetPrice(petModel);
                }
            });


        }

    }

    void askForPrice(PetModel petModel, PetCircleHolder holder) {
        EditText editText = new EditText(context);
        editText.setHint(context.getString(R.string.pet_price));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(context)
                .setTitle("Pet selling price in PKR")
                .setView(editText)
                .setPositiveButton("Publish", (dialog, which) -> {
                    try {
                        String price = editText.getText().toString();
                        if (price.isEmpty()) {
                            Toast.makeText(context, "Invalid price!", Toast.LENGTH_SHORT).show();
                            holder.sellSwitch.setChecked(false);
                        } else {
                            double amount = Double.parseDouble(price);
                            //update price and publish ad
                            petModel.setPetPrice(String.valueOf(amount));
                            petModel.setPetImage(null);
                            new PetController(context).updatePetPrice(petModel);
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Only numeric number are allowed!", Toast.LENGTH_SHORT).show();
                        holder.sellSwitch.setChecked(false);
                    }
                })
                .setNegativeButton("Close", (dialog, which) -> {
                    dialog.cancel();
                    holder.sellSwitch.setChecked(false);
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    static class PetCircleHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView title;
        Button travelButton, hygieneButton;
        ImageButton alarmButton;

        SwitchCompat sellSwitch;

        public PetCircleHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.petCardCircleImage);
            title = itemView.findViewById(R.id.petCardCircleTitle);
            travelButton = itemView.findViewById(R.id.petCircleTravelButton);
            hygieneButton = itemView.findViewById(R.id.petCircleHygieneButton);
            alarmButton = itemView.findViewById(R.id.petCircleAlarmButton);

            sellSwitch = itemView.findViewById(R.id.petCircleCardSwitch);
        }
    }
}
