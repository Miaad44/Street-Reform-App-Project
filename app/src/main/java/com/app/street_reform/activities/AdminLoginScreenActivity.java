package com.app.street_reform.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.street_reform.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginScreenActivity extends BaseActivity {

    Button Admin_butn;
    EditText Adminn_ETKey;
    DatabaseReference DB_Ref_83;
    String Key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        DB_Ref_83 = FirebaseDatabase.getInstance().getReference("AdminKey").child("Key");

        Admin_butn = (Button) findViewById(R.id.btnSignIn);
        Adminn_ETKey = (EditText) findViewById(R.id.edtKey);

        showProgressDialog("please wait..Loading..");
        load_Credentials();

        Admin_butn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String str = Adminn_ETKey.getText().toString();
                Toast.makeText(AdminLoginScreenActivity.this, "str: "+str, Toast.LENGTH_SHORT).show();

                if(!isConnectionAvailable(AdminLoginScreenActivity.this)){
                    final Snackbar snackbar = Snackbar.make(v, "Please Check your network!!!!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                    return;
                }

                if (TextUtils.isEmpty(str)) {
                    Adminn_ETKey.setError("Please Enter login ID!!!");
                    Adminn_ETKey.requestFocus();
                    return;
                }
                if(str.equals(Key)){
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    finish();
                }else {
                    Adminn_ETKey.setError("Error...Incorrect ID!!!");
                    Adminn_ETKey.requestFocus();
                }
            }
        });
    }

    private void load_Credentials() {
        DB_Ref_83.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Key = (String) dataSnapshot.getValue(String.class);
                hideProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }});
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //This function checks wifi is on or off or data connection of the mobile phone.
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}