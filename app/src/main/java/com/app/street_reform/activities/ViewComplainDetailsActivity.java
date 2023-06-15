package com.app.street_reform.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.street_reform.R;
import com.app.street_reform.models.ComplainModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class ViewComplainDetailsActivity extends AppCompatActivity{

    DatabaseReference DB_Ref_83;
    ComplainModel complainModel;
    List<String> urlListt_83;
    ViewPager viewPagerComplain;
    TabLayout indicatorComplain;
    ImageView Back_photo;
    TextView tvComplain, StatusComplain, ReportComplain, DescriptionComplain, tvSeverity, LocationTextView;
    Button But_Edit_83, But_Delete_83;
    String key="";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complain_details);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        viewPagerComplain = (ViewPager) findViewById(R.id.viewPager);
        indicatorComplain = (TabLayout) findViewById(R.id.indicator);

        Back_photo = findViewById(R.id.Back_photo);
        tvComplain = findViewById(R.id.tvComplain);
        StatusComplain = findViewById(R.id.tvStatus);
        ReportComplain = findViewById(R.id.tvReportBy);
        DescriptionComplain = findViewById(R.id.tvDescription);
        tvSeverity = findViewById(R.id.tvSeverity);
        LocationTextView = findViewById(R.id.tvLocation);
        But_Edit_83 = findViewById(R.id.btnEdit);
        But_Delete_83 = findViewById(R.id.btnDelete);

        if(key.equals("user")){
            complainModel = MainActivity.model;
        }else if(key.equals("admin")){
            complainModel = ViewComplainsActivity.modelComplain;
            But_Edit_83.setText("Change complain status");
            StatusComplain.setVisibility(View.VISIBLE);

            StatusComplain.setText("Complain Status: "+complainModel.getStatus());
        }

        DB_Ref_83 = FirebaseDatabase.getInstance().getReference("Complaints");

        urlListt_83 = complainModel.getUrlList();

        tvComplain.setText("Complain: "+complainModel.getComplainType());
        ReportComplain.setText("Complain by : "+complainModel.getUserName());
        DescriptionComplain.setText(complainModel.getDescription());
        tvSeverity.setText(complainModel.getSeverity());


        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> listAddresses = geocoder.getFromLocation(complainModel.getLatitude(), complainModel.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0) {
                String address = "";

                if (listAddresses.get(0).getAddressLine(0) != null) {
                    address += listAddresses.get(0).getAddressLine(0);
                    LocationTextView.setText(address);
                }else {
                    if (listAddresses.get(0).getThoroughfare() != null) {
                        address += listAddresses.get(0).getThoroughfare() + ", ";
                    }

                    if (listAddresses.get(0).getLocality() != null) {
                        address += listAddresses.get(0).getLocality() + ", ";
                    }

                    if (listAddresses.get(0).getAdminArea() != null) {
                        address += listAddresses.get(0).getAdminArea()+", ";
                    }

                    if (listAddresses.get(0).getCountryName() != null) {
                        address += listAddresses.get(0).getCountryName();
                    }
                    LocationTextView.setText(address);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        viewPagerComplain.setAdapter(new SliderAdapter(ViewComplainDetailsActivity.this, urlListt_83));
        indicatorComplain.setupWithViewPager(viewPagerComplain, true);

        Back_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        But_Edit_83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(key.equals("user")){
                    Intent intent = new Intent(getApplicationContext(), ComplainActivity.class);
                    intent.putExtra("key","edit");
                    startActivity(intent);
                }else if(key.equals("admin")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewComplainDetailsActivity.this);
                    builder.setTitle("Select?");
                    builder.setMessage("Do you want to change complain status to Received, Executed or Processed?").setPositiveButton("Received", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DB_Ref_83.child(complainModel.getId()).child("status").setValue("Received");
                            Toast.makeText(ViewComplainDetailsActivity.this, "Complain status marked as Received", Toast.LENGTH_SHORT).show();
                            StatusComplain.setText("Complain Status: Received");
                        }
                    }).setNegativeButton("Executed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DB_Ref_83.child(complainModel.getId()).child("status").setValue("Executed");
                            Toast.makeText(ViewComplainDetailsActivity.this, "Complain status marked as Executed", Toast.LENGTH_SHORT).show();
                            StatusComplain.setText("Complain Status: Executed");
                        }
                    }).setNeutralButton("Processed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DB_Ref_83.child(complainModel.getId()).child("status").setValue("Processed");
                            Toast.makeText(ViewComplainDetailsActivity.this, "Complain status marked as Processed", Toast.LENGTH_SHORT).show();
                            StatusComplain.setText("Complain Status: Processed");
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });
        But_Delete_83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewComplainDetailsActivity.this);
                builder.setTitle("Confirmation?");
                builder.setMessage("Are you sure to delete/cancel this complain?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DB_Ref_83.child(complainModel.getId()).removeValue();
                        Toast.makeText(ViewComplainDetailsActivity.this, "Complain deleted successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    private ProgressDialog progDial_83;
    public void showProgressDialog() {
        if (progDial_83 == null) {
            progDial_83 = new ProgressDialog(this);
            progDial_83.setMessage("Please wait..");
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

    public class SliderAdapter extends PagerAdapter {
        private Context context;
        private List<String> urlList;

        public SliderAdapter(Context context, List<String> urlList) {
            this.context = context;
            this.urlList = urlList;
        }

        @Override
        public int getCount() {
            return urlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_slider, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

            final String item = urlList.get(position);
            Picasso.with(context).load(item).placeholder(R.drawable.holder).into(imageView);
            ViewPager viewPager = (ViewPager) container;
            viewPager.addView(view, 0);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager viewPager = (ViewPager) container;
            View view = (View) object;
            viewPager.removeView(view);
        }
    }


}