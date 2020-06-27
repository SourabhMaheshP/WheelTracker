/*prefix m_ stands for member of a class*/

package com.example.wheeltracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import java.net.URLEncoder;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher,GoogleApiClient.OnConnectionFailedListener {

    private TextView sign_up;
    private EditText username, password;
    private Button log_in;
    private ProgressDialog progressDialog;
    private SignInButton signInButton;
    private GoogleSignInClient m_googlesigninclient = null;
    private static final int REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sign_up = findViewById(R.id.sign_up);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        log_in = findViewById(R.id.log_in);
        signInButton = findViewById(R.id.sign_in_button);

        progressDialog = new ProgressDialog(this);

        sign_up.setOnClickListener(this);
        log_in.setOnClickListener(this);
        signInButton.setOnClickListener(this);

        username.addTextChangedListener(this);
        password.addTextChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("ONSTOP","in stop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("ONPAUSE","in pause");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_in:
                //Toast.makeText(this,"in login",Toast.LENGTH_LONG).show();
                new Login(this, progressDialog).execute(username.getText().toString(), password.getText().toString());
                break;
            case R.id.sign_up:
                new CallIntent(MainActivity.this, SignUpActivity.class).callIntent();
                break;
            case R.id.sign_in_button:      //google sign in
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                m_googlesigninclient = GoogleSignIn.getClient(this,googleSignInOptions);
                Intent signInIntent = m_googlesigninclient.getSignInIntent();
                startActivityForResult(signInIntent, REQ_CODE);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        m_Toggle(username, password);
    } //call method


    private void m_Toggle(EditText uname, EditText pwd) {
        String u_name = uname.getText().toString();
        String p_wd = pwd.getText().toString();
        if (u_name.length() == 10 && p_wd.length() > InitConfig.DEFAULT_LENGTH) {
            log_in.setEnabled(true);
            log_in.setAlpha(1);
        } else {
            log_in.setEnabled(false);
            log_in.setAlpha((float) 0.5);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleResult(task);
        }
    }

    private void handleResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //check if the email exists
            new CheckSignInUser().execute(account.getDisplayName(),account.getEmail());
            Log.e("googlesign","signchecked");
            UserData userData = UserData.getInstance(MainActivity.this);
            userData.googleSignIn(m_googlesigninclient);
        } catch (ApiException e) {

            Log.e("APIERROR", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //async task
    private class CheckSignInUser extends AsyncTask<String,Void,Void> {
        private String m_email, m_name, m_pwd;
        private HashMap googleUser;

        CheckSignInUser()
        {
            googleUser = new HashMap();
        }
        @Override
        protected void onPreExecute(){
            progressDialog.setMessage("Sign in...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(String... strings) {

            m_name = strings[0];
            m_email = strings[1];
            m_pwd = "";
          try{
              String link = InitConfig.PATH + "checkemail.php";
              String data = URLEncoder.encode("email", "UTF-8") + "=" +
                      URLEncoder.encode(m_email, "UTF-8");
              data += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" +
                      URLEncoder.encode(m_pwd, "UTF-8");
              googleUser.putAll(new ConnectionDb(link,data).connectionGoogleSignIn());
          }catch (Exception ee)
          {
             Log.e("Exception:",ee.getMessage());
          }
          return null;
        }
        @Override
        protected void onPostExecute(Void v)
        {
            if(!googleUser.isEmpty() && googleUser.get("success")!=null )
            {
                if( googleUser.get("success").equals("success"))
                {
                    String googleUserPhone= (String) googleUser.get("phone");
                    new Login(MainActivity.this,new ProgressDialog(MainActivity.this)).execute(googleUserPhone,m_pwd);
                }
                else if(googleUser.get("success").equals("failed"))
                {
                    CallIntent sign_in_intent = new CallIntent(MainActivity.this,GoogleSignUp.class);
                    sign_in_intent.extraData("name",m_name);
                    sign_in_intent.extraData("email",m_email);
                    sign_in_intent.callIntent();
                }
            }
            else
                Toast.makeText(MainActivity.this,"Please check your internet connection!",Toast.LENGTH_LONG).show();

            progressDialog.cancel();
        }
    }
}


