package com.app.street_reform.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.street_reform.R;
import com.app.street_reform.activities.MainActivity;
import com.app.street_reform.activities.ComplainActivity;

public class HomeFragment extends Fragment {
    private ProgressDialog progDial_83;
    View ViewAccount_83;
    Context Cont_83;
    Button btnLighting, btnRoads, btnTrafficLights, btnAbandonedCars, btnBuilding, btnCleanliness;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewAccount_83 = inflater.inflate(R.layout.fragment_home, container, false);
        Cont_83 = container.getContext();

        btnLighting = ViewAccount_83.findViewById(R.id.btnLighting);
        btnRoads = ViewAccount_83.findViewById(R.id.btnRoads);
        btnTrafficLights = ViewAccount_83.findViewById(R.id.btnTrafficLights);
        btnAbandonedCars = ViewAccount_83.findViewById(R.id.btnAbandonedCars);
        btnBuilding = ViewAccount_83.findViewById(R.id.btnBuilding);
        btnCleanliness = ViewAccount_83.findViewById(R.id.btnCleanliness);

        btnLighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.complainType="Lighting";
                Intent intent = new Intent(Cont_83, ComplainActivity.class);
                intent.putExtra("key","new");
                startActivity(intent);
            }
        });
        btnRoads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.complainType="Roads";
                Intent intent = new Intent(Cont_83, ComplainActivity.class);
                intent.putExtra("key","new");
                startActivity(intent);
            }
        });
        btnTrafficLights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.complainType="Traffic lights";
                Intent intent = new Intent(Cont_83, ComplainActivity.class);
                intent.putExtra("key","new");
                startActivity(intent);
            }
        });
        btnAbandonedCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.complainType="Abandoned cars";
                Intent intent = new Intent(Cont_83, ComplainActivity.class);
                intent.putExtra("key","new");
                startActivity(intent);
            }
        });
        btnBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.complainType="Building under construction";
                Intent intent = new Intent(Cont_83, ComplainActivity.class);
                intent.putExtra("key","new");
                startActivity(intent);
            }
        });
        btnCleanliness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.complainType="Public Cleanliness";
                Intent intent = new Intent(Cont_83, ComplainActivity.class);
                intent.putExtra("key","new");
                startActivity(intent);
            }
        });

        return ViewAccount_83;
    }

    public void showProgressDialog() {
        if (progDial_83 == null) {
            progDial_83 = new ProgressDialog(Cont_83);
            progDial_83.setMessage("Loading Data...");
            progDial_83.setIndeterminate(true);
        }
        progDial_83.show();
    }

    public void hideProgressDialog() {
        if (progDial_83 != null && progDial_83.isShowing()) {
            progDial_83.dismiss();
        }
    }
}
