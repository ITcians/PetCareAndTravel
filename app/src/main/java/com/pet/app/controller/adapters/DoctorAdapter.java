package com.pet.app.controller.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.pet.app.R;
import com.pet.app.models.doctor.DoctorModel;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorView> {

    Context context;
    List<DoctorModel> doctors;

    public DoctorAdapter(Context context, List<DoctorModel> doctors) {
        this.context = context;
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public DoctorView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorView(LayoutInflater.from(context).inflate(R.layout.doctor_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorView holder, int position) {
        DoctorModel doc = doctors.get(position);
        holder.title.setText(doc.getDoctorName());
        holder.spec.setText(doc.getSpecification());
        holder.address.setText(doc.getDoctorAddress());
        holder.callButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + doc.getContact()));
            ActivityCompat.startActivity(context, intent, null);
        });
        holder.msgButton.setOnClickListener(v -> {
            String url = "https://api.whatsapp.com/send?phone=+923312780076&text=Hey!";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            ActivityCompat.startActivity(context, i, null);
        });
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    static class DoctorView extends RecyclerView.ViewHolder {
        TextView title, spec, address;
        ExtendedFloatingActionButton callButton, msgButton;

        public DoctorView(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.doctorName);
            spec = itemView.findViewById(R.id.doctorSpec);
            address = itemView.findViewById(R.id.doctorAddress);
            callButton = itemView.findViewById(R.id.doctorCall);
            msgButton = itemView.findViewById(R.id.doctorMessage);
        }
    }
}
