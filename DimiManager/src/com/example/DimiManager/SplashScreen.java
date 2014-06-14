package com.example.DimiManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.net.ConfigProvider;

/**
 * Created by 현성 on 2014-05-10.
 */
public class SplashScreen extends Activity{
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    int check;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        check = ConfigProvider.getIntData(this, "Initkey", 1);
        final SplashScreen temp = this;

        if(checkPlayServices())
        {
            Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    if(temp.check == 2)
                        startActivity(new Intent(temp, ContentScreen.class));
                    else if(temp.check == 3)
                        startActivity(new Intent(temp, LoginScreen.class));
                    else if(temp.check == 1)
                        startActivity(new Intent(temp, RegisterScreen.class));
                    else
                    {
                        Toast message = Toast.makeText(getApplicationContext(), "Application Error", Toast.LENGTH_LONG);
                        message.show();
                    }
                    finish();
                }
            };
            handler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        checkPlayServices();
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "Application Error!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }
}
