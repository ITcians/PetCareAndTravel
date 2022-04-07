package com.pet.app.views.orders;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pet.app.R;
import com.pet.app.controller.Reque;
import com.pet.app.resources.Apis;
import com.pet.app.resources.UserSession;

import java.util.HashMap;
import java.util.Map;

public class OrderCancelDialog extends BottomSheetDialog {
    final OrderModel orderModel;
    ProgressDialog progressDialog;
    public OrderCancelDialog(@NonNull Context context,OrderModel model) {
        super(context);
        this.orderModel=model;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(getContext());
        textView.setTextSize(20);
        textView.setText("Are you sure to cancel this order ?");
        textView.setPadding(16, 16, 16, 16);

        LinearLayout layout=new LinearLayout(getContext());
        layout.setPadding(1616,16,16,16);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(textView);

        Button exitButton = new Button(getContext());
        exitButton.setText("Exit");
        exitButton.setTextColor(ActivityCompat.getColor(getContext(), R.color.white));
        exitButton.setOnClickListener(onCancel);
        exitButton.setBackgroundColor(ActivityCompat.getColor(getContext(), R.color.red));
        layout.addView(exitButton);

        Button confirm = new Button(getContext());
        confirm.setText("Confirm");
        confirm.setOnClickListener(onConfirm);
        confirm.setBackgroundColor(ActivityCompat.getColor(getContext(), R.color.teal_700));
        confirm.setTextColor(ActivityCompat.getColor(getContext(), R.color.white));
        layout.addView(confirm);
        setContentView(layout);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait....");

    }

    View.OnClickListener onCancel = view -> this.cancel();
    View.OnClickListener onConfirm = view -> {
        // progressDialog.show();
        new Thread(this::cancelOrder).start();
        this.cancel();

        Toast.makeText(getContext(), "Your order has canceled!", Toast.LENGTH_SHORT).show();
    };

    void cancelOrder() {
        StringRequest request = new StringRequest(Request.Method.POST, Apis.orderCancel,
                response -> System.out.println(response + " JAAN "),
                error -> System.out.println(" JAAN " + error.getLocalizedMessage())) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("orderId", String.valueOf(orderModel.getId()));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(Apis.kAuth, "Bearer " + UserSession.getSession(getContext()).getToken());
                return map;
            }
        };
        Reque.getInstance(getContext()).addToRequestQueue(request);
    }
}
