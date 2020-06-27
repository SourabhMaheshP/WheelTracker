package com.example.wheeltracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.net.URLEncoder;

public class Register extends AsyncTask<String,String,String>
{
    private Context m_context;
    private ProgressDialog m_progressDialog;
    private AlertDialog.Builder build;
    private String name,email,phone,pwd;
    private UserData userData;
    Register(Context context ,ProgressDialog progressDialog)
    {
        m_context = context ;
        userData = UserData.getInstance(m_context);
        m_progressDialog = progressDialog;
        build  = new AlertDialog.Builder(m_context);
    }

    @Override
    protected void onPreExecute(){
        m_progressDialog.setMessage("Loading...");
        m_progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

         name = strings[0];
         email =strings[1];
         phone =strings[2];
         pwd  = strings[3];

        Log.e("NAME",name);
        Log.e("Phone",phone);
        Log.e("Email",email);
        Log.e("pwd",pwd);

        try {
            String link = InitConfig.PATH + "register.php";
            String data = URLEncoder.encode("name", "UTF-8") + "=" +
                    URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                    URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" +
                    URLEncoder.encode(phone, "UTF-8");
            data += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" +
                    URLEncoder.encode(pwd, "UTF-8");
            Log.e("timee","times");
            String response = new ConnectionDb(link,data).connectionRegister();
            Log.e("REGRESPONSE",response);
            return response;
        }catch (Exception ee)
        {
            return "Exception: " + ee.getMessage();
        }

    }
    @Override
    protected void onPostExecute(String result){
        if(result!=null) {
            if (result.equals("duplicate")) {
                build.setTitle("Already Registered!");
                build.setMessage("Please Sign up with different account.").setCancelable(false)
                        .setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'tryagain' Button
                                dialog.cancel();
                            }
                        });
                build.create().show();
            } else if (result.equals("success")) {
                Log.e("USERDATA",""+userData.isGoogleSignIn());
                if(userData.isGoogleSignIn())   //user has signed in with google
                    new Login(m_context,new ProgressDialog(m_context)).execute(phone,pwd);
                else
                {
                    build.setTitle("Registered Successfully!");
                    build.setMessage("Please Log in to continue.").setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  call log in activity here
                                    new CallIntent(m_context, MainActivity.class).callIntent();
                                }
                            });
                    build.create().show();
                }

            } else if (result.equals("failed"))
                Toast.makeText(m_context, "Wait for sometime, or try again later", Toast.LENGTH_LONG).show();
        }
        else  //if fail
            Toast.makeText(m_context,"Please check your Internet Connection",Toast.LENGTH_LONG).show();
        m_progressDialog.dismiss();
    }
}