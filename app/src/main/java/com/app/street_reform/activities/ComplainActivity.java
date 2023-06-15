package com.app.street_reform.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.street_reform.R;
import com.app.street_reform.models.ComplainModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComplainActivity extends BaseActivity{

    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final int PICK_IMAGE = 102;
    RelativeLayout layoutSelectPhoto;
    LocationManager locaMang_83;
    LocationListener locaList_83;
    ImageView CancelIcone, LocationIcone;
    TextView tvComplain, DisplayUserName,LocatinTV;
    EditText edtDescription, edtSeverity;
    Button btnSubmitt;
    ProgressDialog progDial_83;
    private Uri ImageUri;
    DatabaseReference DB_Ref_83;
    StorageReference mStorageReff ;
    StorageTask mUploadTask;
    public static List<String> urlListt_83;
    RecyclerView Recycler_Vieww;
    String userId="", key="";
    ComplainModel complainModel;
    double latit_83, long_83;
    boolean locFlag_83 = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        userId = MainActivity.ID_Userr;

        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        progDial_83 = new ProgressDialog(this);
        DB_Ref_83 = FirebaseDatabase.getInstance().getReference("Complaints");
        mStorageReff = FirebaseStorage.getInstance().getReference("Complaints/") ;

        urlListt_83 = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        Recycler_Vieww = findViewById(R.id.recyclerView2) ;
        Recycler_Vieww.setHasFixedSize(true); ;
        Recycler_Vieww.setLayoutManager(linearLayoutManager);
        LocatinTV = findViewById(R.id.LocaTV);
        layoutSelectPhoto = findViewById(R.id.layoutSelectPhoto);
        CancelIcone = findViewById(R.id.CancelIcone);
        LocationIcone = findViewById(R.id.LocationIcone);

        tvComplain = findViewById(R.id.tvComplain);
        DisplayUserName = findViewById(R.id.UsernameTV);
        edtDescription = findViewById(R.id.edtDescription);
        edtSeverity = findViewById(R.id.edtSeverity);
        btnSubmitt = findViewById(R.id.btnSubmitt);


        if(key.equals("new")){
            tvComplain.setText("Complain: "+MainActivity.complainType);
            DisplayUserName.setText("Username: "+MainActivity.ResdintUserName);
        }else if(key.equals("edit")){
            complainModel = MainActivity.model;

            tvComplain.setText("Complain: "+complainModel.getComplainType());
            DisplayUserName.setText("Username: "+complainModel.getUserName());

            urlListt_83 = complainModel.getUrlList();
            abc();

            edtDescription.setText(complainModel.getDescription());
            edtSeverity.setText(complainModel.getSeverity());

            latit_83 = complainModel.getLatitude();
            long_83 = complainModel.getLongitude();
            locFlag_83 = true;

            btnSubmitt.setText("Edit Complain");
        }

        CancelIcone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LocationIcone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
        layoutSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        return;
                    }
                }

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        btnSubmitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = edtDescription.getText().toString().trim();
                String severity = edtSeverity.getText().toString().trim();

                if(urlListt_83.size()<1){
                    Toast.makeText(getApplicationContext(), "Select any evidence screenshot!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(description)){
                    edtDescription.setError("Required");
                    edtDescription.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(severity)){
                    edtSeverity.setError("Required");
                    edtSeverity.requestFocus();
                    return;
                }
                if(!locFlag_83){
                    Toast.makeText(ComplainActivity.this, "Please select the desired location!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(key.equals("new")){
                    String id = DB_Ref_83.push().getKey();

                    String cDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
                    String cTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());

                    ComplainModel model = new ComplainModel(id,MainActivity.ID_Userr,MainActivity.ResdintUserName,MainActivity.ResidentEmail,urlListt_83,
                            MainActivity.complainType,description,severity,latit_83,long_83,cDate, cTime,"Pending");
                    DB_Ref_83.child(id).setValue(model);
                    Toast.makeText(ComplainActivity.this, "Complain submitted successfully", Toast.LENGTH_SHORT).show();
                    Recycler_Vieww.setAdapter(null);
                    latit_83 = 0.0;
                    long_83 = 0.0;
                    locFlag_83 = false;
                    edtDescription.setText("");
                    edtSeverity.setText("");
                    LocatinTV.setText("Get the user's current location");


                }else if(key.equals("edit")){
                    String id = complainModel.getId();

                    String cDate = complainModel.getDate();
                    String cTime = complainModel.getTime();

                    latit_83 = complainModel.getLatitude();
                    long_83 = complainModel.getLongitude();

                    ComplainModel model = new ComplainModel(id,complainModel.getUserId(),complainModel.getUserName(),complainModel.getUserEmail(),urlListt_83,
                            complainModel.getComplainType(),description,severity,latit_83,long_83,cDate, cTime,complainModel.getStatus());
                    DB_Ref_83.child(id).setValue(model);
                    Toast.makeText(ComplainActivity.this, "Complain edited successfully", Toast.LENGTH_SHORT).show();
                    Recycler_Vieww.setAdapter(null);
                    latit_83 = 0.0;
                    long_83 = 0.0;

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Acquired Permission", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE);
            } else {
                Toast.makeText(getApplicationContext(), "Refusal of Permission", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locaMang_83.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10000, locaList_83);
                    locaMang_83.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10000, locaList_83);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

                if (data.getClipData() != null) {
                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSlect = 0;

                    while (currentImageSlect < countClipData) {
                        ImageUri = data.getClipData().getItemAt(currentImageSlect).getUri();
                        Recycler_Vieww.setAdapter(null);
                        uploadImages(ImageUri);
                        currentImageSlect = currentImageSlect + 1;
                    }
                } else {
                    ImageUri = data.getData();
                    Recycler_Vieww.setAdapter(null);
                    uploadImages(ImageUri);
                }
        }
    }

    private  String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver() ;
        MimeTypeMap mime = MimeTypeMap.getSingleton() ;
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImages(Uri mImageUri) {
        showProgressDialog("Uploading pictures..");
            final StorageReference fileref = mStorageReff.child(System.currentTimeMillis() + "." + getExtension(mImageUri));
            mUploadTask = fileref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try {
                                urlListt_83.add(uri.toString());
                                hideProgressDialog();
                                abc();
                            } catch (Exception ex ){
                                Toast.makeText(getApplicationContext()  , "err" + ex.toString() , Toast.LENGTH_LONG).show();
                                hideProgressDialog();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                }
            });
    }

    public void abc(){
        Recycler_Vieww.setAdapter(null);
        ItemListAdapter imageAdapter = new ItemListAdapter(ComplainActivity.this, urlListt_83);
        Recycler_Vieww.setAdapter(imageAdapter);
    }

    public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ImageViewHolder>{
        private Context Contextm ;
        private List<String> Upload_mList;

        public ItemListAdapter(Context context , List<String> uploadList ) {
            Contextm = context ;
            Upload_mList = uploadList ;
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(Contextm).inflate(R.layout.image_layout , parent , false);
            return (new ImageViewHolder(v));
        }

        @Override
        public void onBindViewHolder(final ImageViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            final String uploadCurrent = Upload_mList.get(position);

            Picasso.with(Contextm).load(uploadCurrent).placeholder(R.drawable.holder).into(holder.itemPic);
            Picasso.with(Contextm).load(R.drawable.ic_close).placeholder(R.drawable.ic_close).into(holder.imgCancel);

            holder.imgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseStorage mStorage;
                    mStorage = FirebaseStorage.getInstance();
                    StorageReference imgRef = mStorage.getReferenceFromUrl(uploadCurrent);
                    imgRef.delete();
                    urlListt_83.remove(position);

                    notifyDataSetChanged();
                }
            });

        }
        @Override
        public int getItemCount() {
            return Upload_mList.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder{
            public ImageView itemPic;
            public ImageView imgCancel;

            public ImageViewHolder(View itemView) {
                super(itemView);

                itemPic = itemView.findViewById(R.id.img);
                imgCancel = itemView.findViewById(R.id.imgCancel);

            }
        }
    }

    private void getCurrentLocation() {

        showProgressDialog("Getting the user's current location...");
        locaMang_83 = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locaList_83 = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                latit_83 = location.getLatitude();
                long_83 = location.getLongitude();
                locFlag_83 = true;

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(latit_83,long_83, 1);

                    if (listAddresses != null && listAddresses.size() > 0) {
                        String address = "";

                        if (listAddresses.get(0).getAddressLine(0) != null) {
                            address += listAddresses.get(0).getAddressLine(0);
                            LocatinTV.setText("Location: "+address);
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
                            LocatinTV.setText("Location: "+address);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }



        };
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locaMang_83.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10000, locaList_83);
            locaMang_83.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10000, locaList_83);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locaMang_83.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10000, locaList_83);
                locaMang_83.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10000, locaList_83);
            }
        }
    }

}