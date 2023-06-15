package com.app.street_reform.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.street_reform.R;

public class ContactUsFragment extends Fragment {
    private ProgressDialog progDial_83;
    View ViewAccount_83;
    Context Cont_83;
    ImageView imgFb, imgInsta, imgTwt, imgYT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewAccount_83 = inflater.inflate(R.layout.fragment_contact_us, container, false);
        Cont_83 = container.getContext();

        imgFb = ViewAccount_83.findViewById(R.id.imgFb);
        imgInsta = ViewAccount_83.findViewById(R.id.imgInsta);
        imgTwt = ViewAccount_83.findViewById(R.id.imgTwt);
        imgYT = ViewAccount_83.findViewById(R.id.imgYT);

        imgFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = newFacebookIntent(Cont_83.getPackageManager(),"https://www.facebook.com/mtcitoman/");
                startActivity(intent);
            }
        });
        imgInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/mtcitoman/");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/invites/contact/?i=7m8m6atu1j4f&utm_content=pa1tm67")));
                }
            }
        });
        imgTwt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    Cont_83.getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/mtcitoman"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/mtcitoman"));
                }
                startActivity(intent);
            }
        });
        imgYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCbJ_L_mZoNyv931JPCYlRLQ")));
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

    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {

        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }
}
