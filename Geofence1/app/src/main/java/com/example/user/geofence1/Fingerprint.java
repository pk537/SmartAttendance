package com.example.user.geofence1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by User on 25/04/2017.
 */

public class Fingerprint extends AppCompatActivity implements FingerPrintAuthCallback {
    FingerPrintAuthHelper mFingerPrintAuthHelper;
    String hour,minute,telephone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in =getIntent();
        Toast.makeText(this, "finger", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.fingerprint);
        Calendar cal = Calendar.getInstance();
        int hr = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
         hour=String.valueOf(hr);
         minute=String.valueOf(min);
        telephone= in.getStringExtra("imei");
        /*TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

         telephone= telephonyManager.getDeviceId();*/
        mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start finger print authentication
        mFingerPrintAuthHelper.startAuth();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFingerPrintAuthHelper.stopAuth();
    }

    @Override
    public void onNoFingerPrintHardwareFound() {
        Toast.makeText(this, "No fingerprint found!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoFingerPrintRegistered() {

    }



    @Override
    public void onBelowMarshmallow() {
        Toast.makeText(this, "Your device doesn't support fingerprint authentication!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
        Toast.makeText(this,"Success!",Toast.LENGTH_SHORT).show();


        mytask t=new mytask();
        t.execute(telephone,hour,minute);

    }

    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {
        switch (errorCode) {    //Parse the error code for recoverable/non recoverable error.
            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
                //Cannot recognize the fingerprint scanned.
                Toast.makeText(this, "Cannot recognize!", Toast.LENGTH_SHORT).show();
                break;
            case AuthErrorCodes.NON_RECOVERABLE_ERROR:
                //This is not recoverable error. Try other options for user authentication. like pin, password.
                break;
            case AuthErrorCodes.RECOVERABLE_ERROR:
                //Any recoverable error. Display message to the user.
                break;
        }
    }
    class mytask extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(Fingerprint.this, "Sending", "Plese wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            //HttpPost post = new HttpPost("http://10.0.2.2/MiniProject/store.php");
            HttpPost post = new HttpPost("http://192.168.43.50/MiniProject/store.php");
            String msg = "";
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("ID", params[0]));
            list.add(new BasicNameValuePair("Hour", params[1]));
            list.add(new BasicNameValuePair("Minute", params[2]));
            try {
                post.setEntity(new UrlEncodedFormEntity(list));
                client.execute(post);
                msg = "Successfully registered";
                pd.dismiss();
            }
            catch (Exception ex) {
                msg = ex.getMessage();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }

        //Toast.makeText(MainActivity.this, "Successfull", Toast.LENGTH_LONG).show();
    }
}
