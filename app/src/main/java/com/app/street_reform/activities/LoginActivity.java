package com.app.street_reform.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.street_reform.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {

    EditText LoginEm_83, LoginPass_83;
    Button logBtn_83;
    TextView sig_RText_83, TVLogin_83;
    ImageView GoogleImg_83;
    TextView forgotP_83;
    FirebaseAuth Auth_83;
    FirebaseUser UserM_83;
    ProgressDialog progDial_83;
    String EPattern_83 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private GoogleSignInClient GoogleSign_83;
    public static final int CODE_REQ_83 = 1001;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestProfile()
                        .build();
        GoogleSign_83 = GoogleSignIn.getClient(this ,googleSignInOptions);

        LoginEm_83 = findViewById(R.id.Email_login_83);
        LoginPass_83 = findViewById(R.id.Password_login_83);
        logBtn_83 = findViewById(R.id.login_Btn_83);
        forgotP_83 = findViewById(R.id.FG_pass);
        sig_RText_83 = findViewById(R.id.signup_Text);
        TVLogin_83 = findViewById(R.id.LoginTV);
        GoogleImg_83 = findViewById(R.id.Google_logo);

        Auth_83=FirebaseAuth.getInstance();
        progDial_83 = new ProgressDialog(this);
        UserM_83 = Auth_83.getCurrentUser();

        TVLogin_83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminLoginScreenActivity.class));
            }
        });

        logBtn_83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerforLogin_83();
            }
        });
        sig_RText_83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        GoogleImg_83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IntSign_83 = GoogleSign_83.getSignInIntent();
                startActivityForResult(IntSign_83 , CODE_REQ_83);
            }
        });
// This code use for TextView (forget password)
        forgotP_83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder Dial_Builder_83=new AlertDialog.Builder(LoginActivity.this);
                View ViewDial_83 = getLayoutInflater().inflate(R.layout.reset_password, null);
                EditText BoxEmail_83 = ViewDial_83.findViewById(R.id.Reset_Email);
                Dial_Builder_83.setView(ViewDial_83);
                AlertDialog Dialog_83 = Dial_Builder_83.create();
                ViewDial_83.findViewById(R.id.button_Reset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String EmailUser_83 = BoxEmail_83.getText().toString();
                        if (TextUtils.isEmpty(EmailUser_83) && !Patterns.EMAIL_ADDRESS.matcher(EmailUser_83).matches()){
                            Toast.makeText(LoginActivity.this, "Enter registered email", Toast.LENGTH_SHORT).show();

                            return;
                        }
                        //this code for Reset Password, the user should enter her/his email
                        Auth_83.sendPasswordResetEmail(EmailUser_83).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    Dialog_83.dismiss();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });

                //code for cancel button
                ViewDial_83.findViewById(R.id.button_Cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog_83.dismiss();
                    }
                });


                if (Dialog_83.getWindow() != null) {
                    Dialog_83.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                Dialog_83.show();
            }
        });


    }

    private void PerforLogin_83() {
        String email = LoginEm_83.getText().toString();
        String password = LoginPass_83.getText().toString();

        if (!email.matches(EPattern_83)) {

            LoginEm_83.setError("Enter correct email");

        }else if (password.isEmpty() || password.length() < 6) {

            LoginPass_83.setError("Enter proper password");
        } else {

            progDial_83.setMessage("Wait please...");
            progDial_83.setTitle("Successfully Login");
            progDial_83.setCanceledOnTouchOutside(false);
            progDial_83.show();

            Auth_83.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progDial_83.dismiss();
                        SendToNextPag_83();
                    } else {
                        progDial_83.dismiss();
                        Toast.makeText(LoginActivity.this,"Enter correct email or password"+task.getException(),Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }

    private void SendToNextPag_83() {
        Toast.makeText(LoginActivity.this,"Login successful",Toast.LENGTH_LONG).show();
        Intent INT_83=new Intent(LoginActivity.this, MainActivity.class);
        INT_83.putExtra("key","custom");
        INT_83.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(INT_83);

    }
    //This method checks user login status..
    @Override
    protected void onStart() {
        super.onStart();

        if(isOnline()){
            Intent inten_83 = new Intent(getApplicationContext(), MainActivity.class);
            inten_83.putExtra("key","custom");
            startActivity(inten_83);
            finish();
        }else {
            // Check if user is signed in with Google Sign-In
            GoogleSignInAccount AccountSign_83 = GoogleSignIn.getLastSignedInAccount(this);
            if (AccountSign_83 != null) {
                // User is signed in

                Intent IntentGoog_83 = new Intent(getApplicationContext(), MainActivity.class);
                IntentGoog_83.putExtra("key","google");
                startActivity(IntentGoog_83);
                finish();
                // TODO: Update UI with signed-in account information.
            } else {
                // User is not signed in
                // TODO: Update UI to reflect this.
            }

        }
    }

    boolean isOnline(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int Code_Requ_83, int CodeResul_83, @Nullable Intent DataCode_83) {
        super.onActivityResult(Code_Requ_83, CodeResul_83, DataCode_83);
        if(Code_Requ_83 == CODE_REQ_83){
            showProgressDialog("Currently Signing In..");
            Task<GoogleSignInAccount> TaskAccount_83 = GoogleSignIn.getSignedInAccountFromIntent(DataCode_83);
            HGoogleSign_83(TaskAccount_83);
        }
    }

    private void HGoogleSign_83(Task<GoogleSignInAccount> TaskAccount_83) {
        try {
            GoogleSignInAccount TAccount_83 = TaskAccount_83.getResult(ApiException.class);
            Update_83(TAccount_83);
            hideProgressDialog();
        } catch (ApiException e) {
            e.printStackTrace();
            hideProgressDialog();

            Toast.makeText(getApplicationContext() , GoogleSignInStatusCodes.getStatusCodeString(e.getStatusCode()) + " " , Toast.LENGTH_LONG).show();
            Log.d("error" , GoogleSignInStatusCodes.getStatusCodeString(e.getStatusCode()));
            Log.d("error" , ""+e.getStatusCode());
        }
    }
    private void Update_83(GoogleSignInAccount account) {
        if(account !=null){
            Intent Update_Intent_83 = new Intent(getApplicationContext(), MainActivity.class);
            Update_Intent_83.putExtra("key","google");
            startActivity(Update_Intent_83);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
