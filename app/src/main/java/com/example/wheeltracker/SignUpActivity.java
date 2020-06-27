package com.example.wheeltracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher {

    EditText m_name,m_phone,m_pwd,m_confirm_pwd,m_email;
    Button next ;
    ProgressDialog progressDialog;
    private static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static  int flag = 0;
    String name,phone,email,pwd,confirm_pwd;

    @SuppressLint({"ResourceAsColor", "RestrictedApi"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //setting up the custom toolbar
        Toolbar custom_toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(custom_toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //when back button is pressed go to Mainactivity.class

        custom_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CallIntent(SignUpActivity.this,MainActivity.class).callIntent();
                finish();
            }
        });
        //logic
        m_name = findViewById(R.id.name);
        m_phone = findViewById(R.id.phone);
        m_pwd = findViewById(R.id.sign_up_pwd);
        m_confirm_pwd = findViewById(R.id.confirm_pwd);
        m_email = findViewById(R.id.email);
        next = findViewById(R.id.sign_up);
        progressDialog=new ProgressDialog(this);

        m_name.addTextChangedListener(this);
        m_phone.addTextChangedListener(this);
        m_pwd.addTextChangedListener(this);
        m_confirm_pwd.addTextChangedListener(this);
        m_email.addTextChangedListener(this);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sign_up)
        {
            name = m_name.getText().toString();
            phone = m_phone.getText().toString();
            pwd = m_pwd.getText().toString();
            confirm_pwd = m_confirm_pwd.getText().toString();
            email = m_email.getText().toString();
            if(name.isEmpty() || !check(name) || name.length()<4)
            {
                flag = 1;
                m_name.setHint("Invalid Name");
                m_name.setText("");
                m_name.setBackgroundResource(R.drawable.error_border);
            }
            if(phone.length()!=10 )
            {
                flag=1;
                m_phone.setHint("Invalid Phone Number");
                m_phone.setText("");
                m_phone.setBackgroundResource(R.drawable.error_border);
            }
            if(email.isEmpty() || !email.matches(emailPattern))
            {
                flag=1;
                m_email.setHint("Invalid Email");
                m_email.setText("");
                m_email.setBackgroundResource(R.drawable.error_border);
            }
            if(pwd.length() < 4)
            {
                flag = 1;
                m_pwd.setText("");
                m_confirm_pwd.setText("");
                m_pwd.setHintTextColor(Color.RED);
                m_pwd.setHint("Password too short");
            }
            if (!confirm_pwd.equals(pwd) || confirm_pwd.isEmpty()) {
                flag=1;
                m_confirm_pwd.setText("");
                m_confirm_pwd.setHintTextColor(Color.RED);
                m_confirm_pwd.setHint("Password doesn't match");
            }
            if(flag == 1) {
                Toast.makeText(this, "Enter Fields To Continue", Toast.LENGTH_SHORT).show();
                flag = 0;
            }
           //else Register the user
           else
                new Register(SignUpActivity.this, progressDialog).execute(name, email, phone, pwd);

        }
    }
    private boolean check(String str)   //check if only alpha
    {
        char[] strr = str.toCharArray();
        for (char c:strr)
        {
            if(!Character.isAlphabetic(c) && c != ' ')
                return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        new CallIntent(SignUpActivity.this,MainActivity.class).callIntent();
        finish();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {

       if(s == m_name.getEditableText())
       {
           m_name.setBackgroundResource(R.drawable.rounded_border);
       }
       else if(s == m_phone.getEditableText()){
           m_phone.setBackgroundResource(R.drawable.rounded_border);
       }
       else if(s == m_email.getEditableText()){
           m_email.setBackgroundResource(R.drawable.rounded_border);
       }
       else if(s == m_pwd.getEditableText())
       {
           m_pwd.setBackgroundResource(R.drawable.rounded_border);
       }
       else if(s == m_confirm_pwd.getEditableText())
       {
           m_confirm_pwd.setBackgroundResource(R.drawable.rounded_border);
       }
    }
}


