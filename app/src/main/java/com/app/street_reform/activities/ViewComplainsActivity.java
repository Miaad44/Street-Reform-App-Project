package com.app.street_reform.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.street_reform.R;
import com.app.street_reform.fragments.ComplaintsListFragment;
import com.app.street_reform.models.ComplainModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewComplainsActivity extends AppCompatActivity {
    private ProgressDialog progDial_83;
    RecyclerView Recycler_Vieww;
    TextView Text_Vieew, T_vieew;
    DatabaseReference DB_Ref_83;
    public static ComplainModel modelComplain;
    List<ComplainModel> list;
    Spinner spnTypeComplain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complains);

        showProgressDialog();

        DB_Ref_83 = FirebaseDatabase.getInstance().getReference("Complaints");
        list = new ArrayList<>();

        Recycler_Vieww = findViewById(R.id.recyclerView);
        Recycler_Vieww.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        Recycler_Vieww.setLayoutManager(gridLayoutManager);
        spnTypeComplain = findViewById(R.id.spnType);
        Text_Vieew = findViewById(R.id.textView);
        T_vieew = findViewById(R.id.tv);

        List<String> list = new ArrayList<>();
        list.add("Pending");
        list.add("Received");
        list.add("Executed");
        list.add("Processed");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String >(this,android.R.layout.simple_list_item_1,list);
        spnTypeComplain.setAdapter(adapter);

        spnTypeComplain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = list.get(position);
                loadData(type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

//        String type = spnType.getSelectedItem().toString();
//        loadData(type);

    }

    private void loadData(String type) {
        DB_Ref_83.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                Text_Vieew.setText("");
                Recycler_Vieww.setAdapter(null);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ComplainModel model = snapshot.getValue(ComplainModel.class);
                    if(type.equals(model.getStatus())){
                        list.add(model);
                    }
                }
                if(list.size()>0){
                    Collections.reverse(list);
                    EventsListAdapter adapter = new EventsListAdapter(ViewComplainsActivity.this, list);
                    Recycler_Vieww.setAdapter(adapter);
                }else {
                    Text_Vieew.setText("No complaints registered!");
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ImageViewHolder>{
        private Context mcontext ;
        private List<ComplainModel> muploadList;

        public EventsListAdapter(Context context , List<ComplainModel> uploadList ) {
            mcontext = context ;
            muploadList = uploadList ;
        }

        @Override
        public EventsListAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mcontext).inflate(R.layout.complain_list_layout, parent , false);
            return (new EventsListAdapter.ImageViewHolder(v));
        }

        @Override
        public void onBindViewHolder(final EventsListAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            final ComplainModel complain = muploadList.get(position);

            holder.tvComplainType.setText("Type: "+complain.getComplainType());
            holder.tvUser.setVisibility(View.VISIBLE);
            holder.tvUser.setText("Complain by: "+complain.getUserName());
            holder.tvTime.setText("Time: "+complain.getTime());
            holder.tvDate.setText("Date: "+complain.getDate());
            holder.tvStatus.setText("Status: "+complain.getStatus());


            holder.circle1.setVisibility(View.GONE);
            holder.circle2.setVisibility(View.GONE);
            holder.circle3.setVisibility(View.GONE);

            holder.imgDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modelComplain = complain;
                    Intent intent = new Intent(getApplicationContext(), ViewComplainDetailsActivity.class);
                    intent.putExtra("key","admin");
                    startActivity(intent);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return muploadList.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder{
            public TextView tvComplainType;
            public TextView tvUser;
            public TextView tvTime;
            public TextView tvDate;
            public TextView tvStatus;
            public RelativeLayout circle1;
            public RelativeLayout circle2;
            public RelativeLayout circle3;
            public ImageView imgDetails;

            public ImageViewHolder(View itemView) {
                super(itemView);

                tvComplainType = itemView.findViewById(R.id.tvComplainType);
                tvUser = itemView.findViewById(R.id.tvUser);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                circle1 = itemView.findViewById(R.id.circle1);
                circle2 = itemView.findViewById(R.id.circle2);
                circle3 = itemView.findViewById(R.id.circle3);
                imgDetails = itemView.findViewById(R.id.imgDetails);
            }
        }
    }

    public void showProgressDialog() {
        if (progDial_83 == null) {
            progDial_83 = new ProgressDialog(this);
            progDial_83.setMessage("Loading data..");
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