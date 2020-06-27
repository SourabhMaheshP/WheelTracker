package com.example.wheeltracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.net.URLEncoder;
import java.util.HashMap;

public class ConnectToMapActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private Button track;
    private HashMap location;
    //this function will change status_src and status_dest to 0
    @Override
    public void onBackPressed() {
        new ClearStatus().execute(UserData.getInstance(ConnectToMapActivity.this)
                .getValue("sensor_id").toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_map);
        track = findViewById(R.id.track);
        progressDialog = new ProgressDialog(this);

        Toolbar custom_toolbar = findViewById(R.id.custom_toolbar3);
        setSupportActionBar(custom_toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        custom_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            //on nav click the status_src and status_dest is changed to 0
            public void onClick(View v) {
                    new ClearStatus().execute(UserData.getInstance(ConnectToMapActivity.this)
                            .getValue("sensor_id").toString());
            }
        });
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new GetLocation().execute();
            }
        });
    }

    private class GetLocation extends AsyncTask<String,Void,Void>
    {

        GetLocation()
        {
            location = new HashMap();
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please Wait!");
            progressDialog.setTitle("Getting your location..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String sensor_id = UserData.getInstance(ConnectToMapActivity.this).getValue("sensor_id").toString();
            Log.e("newsensorid",sensor_id);
            try
            {
                String link = InitConfig.PATH + "getlocation.php";
                String data = URLEncoder.encode("sensor_id", "UTF-8") + "=" +
                        URLEncoder.encode(sensor_id, "UTF-8");
               location.putAll(new ConnectionDb(link, data).getLocation());
            }catch (Exception ee)
            {
               Log.e("indoinbackground","caught");
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
        //    super.onPostExecute(s);
            if(location.get("success")!=null)
            {
                if(location.get("success").equals("success"))
                {
                    String lat = (String)location.get("latitude");
                    String lon = (String)location.get("longitude");
                    Intent map_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+lat+","+lon));
                    map_intent.setPackage("com.google.android.apps.maps");
                    startActivity(map_intent);
                }
                else if(location.get("success").equals("failed"))
                {
                    Toast.makeText(ConnectToMapActivity.this,"Failed To Connect Map!",Toast.LENGTH_LONG).show();
                }
            }
            progressDialog.dismiss();
        }
    }
    private class ClearStatus extends AsyncTask<String,String,String>
    {
        private AlertDialog.Builder m_build;
        ClearStatus()
        {
            m_build  = new AlertDialog.Builder(ConnectToMapActivity.this);
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
                if(s.equals("success"))
                {
                    new CallIntent(ConnectToMapActivity.this, ConnectActivity.class).callIntentWithFlag(
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );
                }
//                    new CallIntent(ConnectToMapActivity.this,ConnectActivity.class).callIntent();
                else if(s.equals("failed"))
                    Toast.makeText(ConnectToMapActivity.this,"Try Again!",Toast.LENGTH_LONG).show();
            }
            else //s == null
                Toast.makeText(ConnectToMapActivity.this,"Please Check your Internet Connection!",Toast.LENGTH_LONG).show();
        }
    }
}
