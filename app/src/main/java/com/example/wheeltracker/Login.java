package com.example.wheeltracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.net.URLEncoder;
import java.util.HashMap;

public class Login extends AsyncTask<String,Void,Void>
{
    private Context m_context;
    private ProgressDialog m_progressDialog;
    private AlertDialog.Builder build;
    //user data
    private String phone;
    private String pwd;
    private UserData userdata;
    Login(Context context ,ProgressDialog progressDialog)
    {
        m_context = context ;
        m_progressDialog = progressDialog;
        build  = new AlertDialog.Builder(m_context);
        userdata = UserData.getInstance(m_context);
    }

    @Override
    protected void onPreExecute(){
        m_progressDialog.setMessage("Please Wait!");
        m_progressDialog.setTitle("Loading...");
        m_progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... strings) {

         phone = strings[0];
         pwd = strings[1];
         try {
                String link = InitConfig.PATH + "login.php";
                String data = URLEncoder.encode("phone", "UTF-8") + "=" +
                        URLEncoder.encode(phone, "UTF-8");
                data += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" +
                        URLEncoder.encode(pwd, "UTF-8");
                userdata.copyData(new ConnectionDb(link,data).connectionLogin());    //putAll func will copy return value of connection func
                userdata.putExtra("phone",phone);
                //copy the details of currently logged on user to userdata class

         }catch (Exception ee)
         {
             Log.e("Error","Login");
         }
         return null;
    }
    @Override
    protected void onPostExecute(Void v){
        //check status = result
            if(userdata.getValue("success")!=null)
            {
                if( userdata.getValue("success").equals("failed"))
                {
                    build.setTitle("Incorrect Credentials!");
                    build.setIcon(R.drawable.incorrect);
                    build.setMessage("Please check your username and password and try again.").setCancelable(false)
                            .setNegativeButton("Try Again",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'tryagain' Button
                                    dialog.cancel();
                                }
                            });
                    build.create().show();
                }
                else if(userdata.getValue("success").equals("success"))
                    new CallIntent(m_context,ConnectActivity.class).callIntent();
                else
                    Toast.makeText(m_context,"Please check your internet connection",Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(m_context,"Please check your internet connection",Toast.LENGTH_LONG).show();

            m_progressDialog.dismiss();
        }

}