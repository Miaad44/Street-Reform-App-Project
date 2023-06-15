package com.app.street_reform.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private ProgressDialog progDial_83;
    public void showProgressDialog(String msg) {
        if (progDial_83 == null) {
            progDial_83 = new ProgressDialog(this);
            progDial_83.setMessage(msg);
            progDial_83.setCanceledOnTouchOutside(false);
            progDial_83.setIndeterminate(true);
        }
        progDial_83.show();
    }

    public void hideProgressDialog() {
        if (progDial_83 != null && progDial_83.isShowing()) {
            progDial_83.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}