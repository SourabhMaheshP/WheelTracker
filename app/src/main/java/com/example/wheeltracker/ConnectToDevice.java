package com.example.wheeltracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import java.net.URLEncoder;

public class ConnectToDevice extends AsyncTask<String,String,String> {

    private String SENSOR_ID;
    private Context m_context;
    ProgressDialog m_progressDialog;
    private AlertDialog.Builder build;
    ConnectToDevice(Context context,ProgressDialog progressDialog)
    {
        m_context = context ;
        m_progressDialog = progressDialog;
        build  = new AlertDialog.Builder(m_context);
    }
    @Override
    protected void onPreExecute() {
        m_progressDialog.setMessage("Please Wait!");
        m_progressDialog.setTitle("Connecting...");
        m_progressDialog.setCancelable(false);
        m_progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        SENSOR_ID = strings[0];
        String status_src = "1";
        String response1,response2="";
        Log.e("sensor_id", SENSOR_ID);

        try {
            String link = InitConfig.PATH + "change_status_src_ANDROID.php";
            String data = URLEncoder.encode("sensor_id", "UTF-8") + "=" +
                    URLEncoder.encode(SENSOR_ID, "UTF-8");
            data += "&" + URLEncoder.encode("status_src", "UTF-8") + "=" +
                    URLEncoder.encode(status_src, "UTF-8");
           response1 = new ConnectionDb(link, data).connectToDevice();
           if(response1.equals("success"))
           {
               int count = 0;
               while(!response2.equals("success") && count < 350)
               {

                   String link1 = InitConfig.PATH + "check_status_dest_ANDROID.php";
                   String data1 = URLEncoder.encode("sensor_id", "UTF-8") + "=" +
                           URLEncoder.encode(SENSOR_ID, "UTF-8");
                   response2 = new ConnectionDb(link1, data1).connectToDevice();
                   count++;
               }
           }
           else
               return  null;

           return response2; //success or failed status

        } catch (Exception ee) {
            return "Error " + ee.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {

        if(s!=null)
        {
            if(s.equals("success")) //if status_src == 1(success) and status_dest == 1(success)
            {
                build.setIcon(R.drawable.correct);
                build.setTitle("Connected Successfully!");
                build.setMessage("\tClick OK to continue.").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //redirect to the map activity and remove dialog.cancel()
                                //now at this stage we have one coordinate
                                new CallIntent(m_context,ConnectToMapActivity.class).callIntent();
                                dialog.cancel();
                            }
                        });
                build.create().show();
            }
            else if(s.equals("failed")) {
                String status_src = "0";
                String response = "";
                try {
                    String link = InitConfig.PATH + "change_status_src_ANDROID.php";
                    String data = URLEncoder.encode("sensor_id", "UTF-8") + "=" +
                            URLEncoder.encode(SENSOR_ID, "UTF-8");
                    data += "&" + URLEncoder.encode("status_src", "UTF-8") + "=" +
                            URLEncoder.encode(status_src, "UTF-8");
                    response = new ConnectionDb(link, data).connectToDevice();
                } catch (Exception ee) {
                    Log.e("Connecttodevice1", ee.getMessage());
                }
                if (response.equals("success"))
                {
                    build.setTitle("Connection Failed!");
                    build.setMessage("Please Try Again").setCancelable(false)
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    build.create().show();
                }
                else
                    Log.e("connecttodevice2","error in response");
            }
        }
        else {
            Toast.makeText(m_context, "Error, Try Again!", Toast.LENGTH_LONG).show();
        }
        m_progressDialog.dismiss();
    }
}
