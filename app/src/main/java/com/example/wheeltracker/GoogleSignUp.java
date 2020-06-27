package com.example.wheeltracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GoogleSignUp extends AppCompatActivity implements TextWatcher {

    private EditText signupphone;
    private Button signupnext;
    private String phone,name,email,pwd;
    private ProgressDialog progressDialog;
    private static int flag =0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_up);

        Toolbar custom_toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(custom_toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //when back button is pressed go to Mainactivity.class

        custom_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData userData = UserData.getInstance(GoogleSignUp.this);
                GoogleSignInClient m_googleSignInClient;
                m_googleSignInClient = userData.getGoogleSignIn();
                if(m_googleSignInClient!=null)
                {
                    m_googleSignInClient.signOut().addOnCompleteListener( GoogleSignUp.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            new CallIntent(GoogleSignUp.this, MainActivity.class).callIntent();
                            finish();
                        }
                    });
                }
            }
        });
        progressDialog = new ProgressDialog(this);
        name = getIntent().getExtras().getString("name");
        email = getIntent().getExtras().getString("email");
        pwd = "";
        signupphone = findViewById(R.id.signuphone);
        signupnext = findViewById(R.id.signupnext);
        signupphone.addTextChangedListener(this);
        signupnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = signupphone.getText().toString();
                if(phone.length()!=10 )
                {
                    flag=1;
                    signupphone.setHint("Invalid Phone Number");
                    signupphone.setText("");
                    signupphone.setBackgroundResource(R.drawable.error_border);
                }
                if(flag == 1) {
                    Toast.makeText(GoogleSignUp.this, "Enter Fields To Continue", Toast.LENGTH_SHORT).show();
                    flag = 0;
                }
                else {

                    new Register(GoogleSignUp.this,progressDialog).execute(name, email, phone, pwd);
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
            if(s == signupphone.getEditableText()){
            signupphone.setBackgroundResource(R.drawable.rounded_border);
        }
    }

}
