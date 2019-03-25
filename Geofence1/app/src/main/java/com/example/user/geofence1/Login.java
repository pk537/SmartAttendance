package com.example.user.geofence1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multidots.fingerprintauth.FingerPrintAuthHelper;

/**
 * Created by User on 23/04/2017.
 */

public class Login extends AppCompatActivity  {
    //FingerPrintAuthHelper mFingerPrintAuthHelper;
    String telephone;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Intent in = getIntent();
        String message= in.getStringExtra("message");
        TextView textMessage= (TextView) findViewById(R.id.textMessage);
        textMessage.setText(message);


    LinearLayout rl;
    DigitalClock clk;


        rl = (LinearLayout)findViewById(R.id.rl);

        clk = new DigitalClock(Login.this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                ((int) ActionBar.LayoutParams.WRAP_CONTENT, (int) ActionBar.LayoutParams.WRAP_CONTENT);

        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        clk.setLayoutParams(params);
        clk.setTextSize(40);

        rl.addView(clk);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        telephone= telephonyManager.getDeviceId();

    }

    public void login(View v)
    {

            Intent in = new Intent(getBaseContext(), Fingerprint.class);
            in.putExtra("imei", telephone);
            startActivity(in);

    }



}
