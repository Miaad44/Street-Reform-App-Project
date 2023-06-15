package com.app.street_reform.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.street_reform.R;

public class AboutUsFragment extends Fragment {
    private ProgressDialog progDial_83;
    View ViewAccount_83;
    Context Cont_83;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewAccount_83 = inflater.inflate(R.layout.fragment_about_us, container, false);
        Cont_83 = container.getContext();


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
