package com.app.street_reform.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.app.street_reform.R;
import com.app.street_reform.fragments.AboutUsFragment;
import com.app.street_reform.fragments.ComplaintsListFragment;
import com.app.street_reform.fragments.ContactUsFragment;
import com.app.street_reform.fragments.HomeFragment;
import com.app.street_reform.fragments.ProfileFragment;
import com.app.street_reform.models.ComplainModel;
import com.app.street_reform.models.UserModelClass;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Menu menu;
    TextView DisplayFullName;
    TextView DisplayEmail;
    public static String ID_Userr="",civil_Number,ResidentEmail, ResdintUserName, key="";
    public static String complainType="";
    public static ComplainModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        if(key.equals("google")){
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
                // User is signed in
                ID_Userr = account.getId();

            }
        }else if(key.equals("custom")){
            ID_Userr = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        DisplayFullName =  view.findViewById(R.id.MenuUserName);
        DisplayEmail =  view.findViewById(R.id.MenuEmail);

        Fragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(key.equals("google")){
            showProgressDialog("Preparing functions..");
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
                // User is signed in
                ResdintUserName = account.getDisplayName();
                ResidentEmail = account.getEmail();

                DisplayFullName.setText(ResdintUserName);
                DisplayEmail.setText(ResidentEmail);



                hideProgressDialog();
            }
        }else if(key.equals("custom")){
            showProgressDialog("Preparing functions");
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModelClass modelClass = snapshot.getValue(UserModelClass.class);
                    civil_Number = modelClass.getCivilNo();
                    ResdintUserName = modelClass.getUserName();
                    ResidentEmail = modelClass.getEmail();

                    DisplayFullName.setText(ResdintUserName);
                    DisplayEmail.setText(ResidentEmail);

                    hideProgressDialog();

                    for(DataSnapshot snapshot1 : snapshot.getChildren()){}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {hideProgressDialog();}});
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        return true;
    }

    private void displaySelectedScreen(int itemId){
        Fragment fragment = null;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        switch(itemId){
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_complaints_list:
                fragment = new ComplaintsListFragment();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_contact:
                fragment = new ContactUsFragment();
                break;
            case R.id.nav_about:
                fragment = new AboutUsFragment();
                break;
            case R.id.nav_logout:
                if(key.equals("google")){
                    final GoogleSignInOptions googleSignInOptions =
                            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail()
                                    .requestProfile()
                                    .build();
                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this ,googleSignInOptions);
                    mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "There was a problem!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else if(key.equals("custom")){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
                break;
        }

        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
        }
    }
}