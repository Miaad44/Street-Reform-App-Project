package com.app.street_reform.activities;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.street_reform.R;
import com.app.street_reform.models.UserModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends BaseActivity {

    EditText edtCivil, Username_sign_83, Email_sign_83, Pass_sign_83,ConfirmPass_sign_83;
    TextView login_RT;
    Button Btn_sign_83;
    DatabaseReference DB_Ref_83;
    String EPattern_83 ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth Auth_83;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        DB_Ref_83 = FirebaseDatabase.getInstance().getReference("Users");
        Auth_83 = FirebaseAuth.getInstance();

        edtCivil = findViewById(R.id.edtCivil);
        Email_sign_83 = findViewById(R.id.Email_EditText_83);
        Username_sign_83 = findViewById(R.id.Name_EditText_83);
        Pass_sign_83 = findViewById(R.id.pass_EditText_83);
        ConfirmPass_sign_83 = findViewById(R.id.Confpass_EditText_83);
        login_RT = findViewById(R.id.tw_account);
        Btn_sign_83 = findViewById(R.id.btn_Regis);

        login_RT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten_83 = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(inten_83);
            }
        });

        Btn_sign_83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SingUp_perform_83();
            }
        });
    }

    //method
    private void SingUp_perform_83() {
        String Email_83 = Email_sign_83.getText().toString().trim();
        String UserName_83 = Username_sign_83.getText().toString().trim();
        String civilNo_83 = edtCivil.getText().toString().trim();
        String password_83 = Pass_sign_83.getText().toString().trim();
        String confirmPass_83 = ConfirmPass_sign_83.getText().toString().trim();

        if(civilNo_83.isEmpty()||UserName_83.isEmpty()){
            edtCivil.setError("Please write your civil number");
            edtCivil.requestFocus();
            return;
        }
        if(civilNo_83.length() < 8 ) {
            edtCivil.setError("Enter Not less than 8 number in civil No");
            edtCivil.requestFocus();
            return;
        }
        if(!Email_83.matches(EPattern_83)){
            Email_sign_83.setError("Please Enter correct email");
            Email_sign_83.requestFocus();
            return;
        }
        if(password_83.isEmpty()||password_83.length() < 6 ) {
            Pass_sign_83.setError("Enter Not less than 6 number in password");
            Pass_sign_83.requestFocus();
            return;
        }
        if(!password_83.equals(confirmPass_83)) {
            ConfirmPass_sign_83.setError("password not match both filed");
            ConfirmPass_sign_83.requestFocus();
            return;
        }else {
            showProgressDialog("Please wait while sign up...");
            Auth_83.createUserWithEmailAndPassword(Email_83,password_83).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        DataSave(civilNo_83,UserName_83,Email_83,password_83);
                    }else {
                        hideProgressDialog();
                        Toast.makeText(SignUpActivity.this,"Enter correct email and password"+task.getException(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void DataSave(String civilNo_83, String UserName_83, String Email_83, String password_83) {
        Auth_83.signInWithEmailAndPassword(Email_83, password_83).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    final FirebaseUser firebaseUser = Auth_83.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    UserModelClass model = new UserModelClass(userId,civilNo_83,UserName_83,Email_83);
                    DB_Ref_83.child(userId).setValue(model);

                    hideProgressDialog();
                    Toast.makeText(SignUpActivity.this,"Successfully Sign up ",Toast.LENGTH_LONG).show();

                    SendToNextPag_83();

                }
            }
        });
    }
    private void SendToNextPag_83() {
        Intent intent_83=new Intent(SignUpActivity.this, MainActivity.class);
        intent_83.putExtra("key","custom");
        intent_83.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_83);
        finish();
    }
}