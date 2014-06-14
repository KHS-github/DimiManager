package com.example.DimiManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.net.ConfigProvider;
import com.net.LoginSession;

import java.io.IOException;

/**
 * Created by 현성 on 2014-05-15.
 */
public class RegisterScreen extends Activity {
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public final String SENDER_ID = "750979902648";

    GoogleCloudMessaging gcm;

    String regID;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        gcm = GoogleCloudMessaging.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerpage);

        final EditText editID = (EditText)findViewById(R.id.editRID);
        final EditText editPass = (EditText)findViewById(R.id.editRPass);
        final EditText editPassCheck = (EditText)findViewById(R.id.editPassCheck);
        final EditText editName = (EditText)findViewById(R.id.editName);
        final EditText editNum = (EditText)findViewById(R.id.editNum);
        final EditText editGroup = (EditText)findViewById(R.id.editGroup);

        Button registerBtn = (Button)findViewById(R.id.btnRegister);
        regID = ConfigProvider.getStringData(getApplicationContext(), "regID", "");
        if(regID == "") {
            registerInBackground();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = editID.getText().toString();
                String Pass = editPass.getText().toString();
                String PassCheck = editPassCheck.getText().toString();
                if(!Pass.equals((String)PassCheck)) {
                    Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                String Name = editName.getText().toString();
                String Num = editNum.getText().toString();
                String Group = editGroup.getText().toString();

                LoginSession.GetInstance().SetRegisterHandler(new MessageHandler());
                LoginSession.GetInstance().RegisterUser(ID, Pass, Name, Num, Group, regID);
            }
        });
    }

    private void registerInBackground() {
        try {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String msg = "";
                    try {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                        }
                        regID = gcm.register(SENDER_ID);

                        // You should send the registration ID to your server over HTTP,
                        // so it can use GCM/HTTP or CCS to send messages to your app.
                        // The request to your server should be authenticated if your app
                        // is using accounts.

                        // For this demo: we don't need to send it because the device
                        // will send upstream messages to a server that echo back the
                        // message using the 'from' address in the message.

                        // Persist the regID - no need to register again.
                        ConfigProvider.putStringData(getApplicationContext(), "regID", regID);
                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();
                        // If there is an error, don't just keep trying to register.
                        // Require the user to click a button again, or perform
                        // exponential back-off.
                    }
                    return msg;
                }
            }.execute(null, null, null);
            Thread.sleep(500);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String data = (String) msg.obj;
            Toast tst = Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG);
            tst.show();
            startActivity(new Intent(getApplicationContext(), LoginScreen.class));
            finish();
            ConfigProvider.putIntData(getApplicationContext(), "Initkey", 3);
        }
    }


    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}