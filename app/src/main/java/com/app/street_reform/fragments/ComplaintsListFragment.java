package com.app.street_reform.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.street_reform.R;
import com.app.street_reform.activities.MainActivity;
import com.app.street_reform.activities.ViewComplainDetailsActivity;
import com.app.street_reform.models.ComplainModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComplaintsListFragment extends Fragment {
    private ProgressDialog progDial_83;
    View ViewAccount_83;
    Context Cont_83;
    RecyclerView Recycler_Vieww;
    TextView Text_Vieew, T_vieew;
    DatabaseReference DB_Ref_83;
    List<ComplainModel> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewAccount_83 = inflater.inflate(R.layout.fragment_complain_list, container, false);
        Cont_83 = container.getContext();

        showProgressDialog();

        DB_Ref_83 = FirebaseDatabase.getInstance().getReference("Complaints");
        list = new ArrayList<>();

        Recycler_Vieww = ViewAccount_83.findViewById(R.id.recyclerView);
        Recycler_Vieww.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Cont_83,1);
        Recycler_Vieww.setLayoutManager(gridLayoutManager);

        Text_Vieew = ViewAccount_83.findViewById(R.id.textView);
        T_vieew = ViewAccount_83.findViewById(R.id.tv);

        return ViewAccount_83;
    }

    @Override
    public void onStart() {
        super.onStart();

        DB_Ref_83.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                Text_Vieew.setText("");
                Recycler_Vieww.setAdapter(null);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ComplainModel model = snapshot.getValue(ComplainModel.class);
                    if(MainActivity.ID_Userr.equals(model.getUserId())){
                        list.add(model);
                    }
                }
                if(list.size()>0){
                    Collections.reverse(list);
                    EventsListAdapter adapter = new EventsListAdapter(Cont_83, list);
                    Recycler_Vieww.setAdapter(adapter);
                }else {
                    Text_Vieew.setText("No complaints registered!");
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressDialog();
                Toast.makeText(Cont_83, "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ImageViewHolder>{
        private Context Contextm ;
        private List<ComplainModel> Upload_mList;

        public EventsListAdapter(Context context , List<ComplainModel> uploadList ) {
            Contextm = context ;
            Upload_mList = uploadList ;
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(Contextm).inflate(R.layout.complain_list_layout, parent , false);
            return (new ImageViewHolder(v));
        }

        @Override
        public void onBindViewHolder(final ImageViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            final ComplainModel complain = Upload_mList.get(position);

            holder.Complain_Typetv.setText("Type: "+complain.getComplainType());
            holder.Timee_TV.setText("Time: "+complain.getTime());
            holder.Datee_TV.setText("Date: "+complain.getDate());
            holder.Status_TV.setText("Status: "+complain.getStatus());

            if(complain.getStatus().equals("Received")){
                holder.circle1.setBackgroundResource(R.drawable.circle_green);
            }else if(complain.getStatus().equals("Executed")){
                holder.circle2.setBackgroundResource(R.drawable.circle_red);
            }else if(complain.getStatus().equals("Processed")){
                holder.circle3.setBackgroundResource(R.drawable.circle_filled);
            }

            holder.Details_Photoo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.model = complain;
                    Intent intent = new Intent(Cont_83, ViewComplainDetailsActivity.class);
                    intent.putExtra("key","user");
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
            return Upload_mList.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder{
            public TextView Complain_Typetv;
            public TextView Timee_TV;
            public TextView Datee_TV;
            public TextView Status_TV;
            public RelativeLayout circle1;
            public RelativeLayout circle2;
            public RelativeLayout circle3;
            public ImageView Details_Photoo;

            public ImageViewHolder(View itemView) {
                super(itemView);

                Complain_Typetv = itemView.findViewById(R.id.tvComplainType);
                Timee_TV = itemView.findViewById(R.id.tvTime);
                Datee_TV = itemView.findViewById(R.id.tvDate);
                Status_TV = itemView.findViewById(R.id.tvStatus);
                circle1 = itemView.findViewById(R.id.circle1);
                circle2 = itemView.findViewById(R.id.circle2);
                circle3 = itemView.findViewById(R.id.circle3);
                Details_Photoo = itemView.findViewById(R.id.imgDetails);
            }
        }
    }

    public void showProgressDialog() {
        if (progDial_83 == null) {
            progDial_83 = new ProgressDialog(Cont_83);
            progDial_83.setMessage("Loading items..");
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
