package com.example.wheeltracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;

public class Logout {

    private Context m_context;
    private ProgressDialog m_progressDialog;
    private AlertDialog.Builder build;
    private UserData userData;
    private GoogleSignInClient m_googleSignInClient;
    Logout(Context context)
    {
        m_context = context ;
        build  = new AlertDialog.Builder(m_context);
        m_progressDialog = new ProgressDialog(m_context);
        userData = UserData.getInstance(m_context);
    }
    void start()
    {
        build.setTitle("Are you sure you want to Log Out?").setCancelable(false).setPositiveButton("Log Out",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //IMPORTANT :
                // for logout change src_status and dest_src to 0 and clear the key from shared preference
                // clear user data hashmap

                m_googleSignInClient = userData.getGoogleSignIn();
                if(m_googleSignInClient!=null)
                {
                    Log.e("GOOGLELOGIN","remove login");

                    m_googleSignInClient.signOut().addOnCompleteListener((Activity) m_context, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            clearSharedPrefsData();
                            String sensor_id =userData.getValue("sensor_id").toString();
                            new RemoveLoginDetails().execute(sensor_id);
//                        new CallIntent(m_context, MainActivity.class).callIntent();
                        }
                    });
                }
                else
                {
                    clearSharedPrefsData();
                    String sensor_id =userData.getValue("sensor_id").toString();
                    new RemoveLoginDetails().execute(sensor_id);
                }
                dialog.dismiss();
            }
        })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        build.create().show();
    }
    private void clearSharedPrefsData() //clear the persistent data
    {
        Log.e("in clear","clear");
        SharedPreferences prefs = m_context.getSharedPreferences("LoginData",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
    private class RemoveLoginDetails extends AsyncTask<String,String,String>
    {
        private AlertDialog.Builder m_build;
        RemoveLoginDetails()
        {
            m_build  = new AlertDialog.Builder(m_context);
        }
        @Override
        protected void onPreExecute() {
            m_progressDialog.setMessage("Please Wait!");
            m_progressDialog.setTitle("Signing Out...");
            m_progressDialog.setCancelable(false);
            m_progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String response1,response2;
            String SENSOR_ID = strings[0];
            String status_src = "0",status_dest ="0" ;
            try {
                String link = InitConfig.PATH + "change_status_src_ANDROID.php";
                String data = URLEncoder.encode("sensor_id", "UTF-8") + "=" +
                        URLEncoder.encode(SENSOR_ID, "UTF-8");
                data += "&" + URLEncoder.encode("status_src", "UTF-8") + "=" +
                        URLEncoder.encode(status_src, "UTF-8");
                response1 = new ConnectionDb(link, data).logout();

                String link1 = InitConfig.PATH + "change_status_dest.php";
                String data1 = URLEncoder.encode("sensor_id", "UTF-8") + "=" +
                        URLEncoder.encode(SENSOR_ID, "UTF-8");
                data1 += "&" + URLEncoder.encode("status_dest", "UTF-8") + "=" +
                        URLEncoder.encode(status_dest, "UTF-8");
                response2 = new ConnectionDb(link1, data1).logout();
                if(response1.equals("success") && response2.equals("success"))
                    return response1;

            }catch(Exception ee)
            {
                Log.e("Exception1",ee.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null)
            {
                if(s.equals("success")) {

                    //clear all data
                    userData.clearData();
                    userData.del();
                    new CallIntent(m_context, MainActivity.class).callIntentWithFlag(
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );
                }
                else
                {

                    m_build.setTitle("Login Failed!");
                    m_build.setIcon(R.drawable.sad);
                    m_build.setMessage("Try Again.").setCancelable(false)
                            .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'tryagain' Button
                                    dialog.cancel();
                                }
                            });
                    m_build.create().show();
                }
            }
            else
            {
                m_build.setTitle("Login Failed!");
                m_build.setIcon(R.drawable.sad);
                m_build.setMessage("Please check your internet connection.").setCancelable(false)
                        .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'tryagain' Button
                                dialog.cancel();
                            }
                        });
                m_build.create().show();
            }
            m_progressDialog.dismiss();
        }
    }
}
