package com.yogeshojha.blooddonation;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import customfonts.MyEditText;
import customfonts.MyTextView;

import static android.R.attr.defaultValue;

public class signup2 extends AppCompatActivity {
    private Boolean flag = false;
    public  ProgressBar pb;
    public MyEditText locationinp;
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        Spinner spinner = (Spinner) findViewById(R.id.blood_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        locationinp = (MyEditText) findViewById(R.id.inploc);
        MyTextView getloc = (MyTextView) findViewById(R.id.getloc);
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);
    }
    public void getlocation(View v)
    {
        flag = displayGpsStatus();
        if (flag) {
            pb.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    pb.setVisibility(View.INVISIBLE);
                    locationinp.setText("HosurRd,WellingtonParadise,Singasandra,Bengaluru,Karnataka560068");
                }
            }, 2500);
        }
        else
        {
            alertbox("Gps Status!!", "Your GPS is: OFF");
        }
    }
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disabled, Please turn it on to get the location automatically.")
                .setCancelable(false)
                .setTitle("Gps Status")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void sgn3(View v)
    {

        String aname = getIntent().getStringExtra("Name");
        MyEditText text = (MyEditText) findViewById(R.id.phone);
        String phone = text.getText().toString();

        MyEditText text1 = (MyEditText) findViewById(R.id.inploc);
        String loctn = text1.getText().toString();

        Spinner sp = (Spinner) findViewById(R.id.blood_spinner);
        String spn = sp.getSelectedItem().toString();
        aname.replaceAll("\\s+","");
        spn.replaceAll("\\s+","");
        URL = "http://kyampus.in/blood/add.php?name="+aname+"&phone="+phone+"&location="+loctn+"&blood="+spn;
        System.out.println(URL);
        new RequestTask().execute(URL);
        String URL_SMS = "http://kyampus.in/blood/sms.php?name="+aname+"&number="+phone;
        new RequestTask().execute(URL_SMS);
        Intent iinent= new Intent(signup2.this,signup3.class);
        startActivity(iinent);
    }
    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
        }
    }
}
