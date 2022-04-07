package com.pet.app.views.orders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.Api;
import com.pet.app.R;
import com.pet.app.controller.Reque;
import com.pet.app.resources.Apis;
import com.pet.app.resources.UserSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFragment extends Fragment implements AdapterView.OnItemClickListener {


    public OrderFragment() {
        // Required empty public constructor
    }

    final Handler handler = new Handler();
    ArrayAdapter<String> adapter;
    ProgressDialog progressDialog;
    final List<String> orders = new ArrayList<>();
    final List<OrderModel> orderModels = new ArrayList<>();

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        this.view = view;
        new Thread(this::init).start();
        return view;
    }

    void init() {
        ListView listView = view.findViewById(R.id.orderList);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, orders);
        handler.post(() -> listView.setAdapter(adapter));
        loadOrders();
    }

    void loadOrders() {
        StringRequest request = new StringRequest(Request.Method.GET, Apis.getOrders,
                response -> {
                    try {
                        OrderModel[] model = OrderConverter.fromJsonString(response);
                        for (OrderModel order : model) {
                            String status = order.getSaleStatus() == 0 ? "Delivered" : "Pending";
                            orders.add(order.getPetName() + "\n"
                                    + "Amount: " + (order.getSaleFees() + order.getPayment())
                                    + "\nStatus: " + status + "\n"
                                    + order.getCreatedAt()
                            );
                            orderModels.add(order);
                        }
                        handler.post(() -> {
                            progressDialog.cancel();
                            adapter.notifyDataSetChanged();
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler.post(() -> {
                        progressDialog.cancel();
                    });
                },
                error -> {
                    handler.post(() -> {
                        progressDialog.cancel();
                        Toast.makeText(getContext(), String.valueOf(error.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
                    });
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(Apis.kAuth, "Bearer " + UserSession.getSession(getContext()).getToken());
                return map;
            }
        };
        Reque.getInstance(getContext()).addToRequestQueue(request);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //cancel order
        if (orderModels.get(position).getSaleStatus() == 1) {
            OrderCancelDialog dialog = new OrderCancelDialog(getContext(), orderModels.get(position));
            dialog.show();
        }
    }
}