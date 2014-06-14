package com.example.DimiManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.net.ConfigProvider;
import com.net.LoginSession;


/**
 * Created by 현성 on 2014-05-10.
 */
public class LoginScreen extends Activity{

    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    int check;
    private String ID;
    private String Pass;

    @Override
    protected void onCreate(Bundle savedInstanceSaved)
    {
        super.onCreate(savedInstanceSaved);
        setContentView(R.layout.loginpage);


        /* Sceneario : Register screen -> input all data -> click Register button -> Register and login -> main page */
        final EditText editID = (EditText)findViewById(R.id.editID);

        final EditText editPass = (EditText)findViewById(R.id.editPass);

        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        check = ConfigProvider.getIntData(getApplicationContext(), "Initkey", -1);
        final LoginScreen ls = this;

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID = editID.getText().toString();
                Pass = editPass.getText().toString();
                if(ls.check == 3)//To Login
                {
                    LoginSession.GetInstance().SetLoginHandler(new MessageHandler());
                    LoginSession.GetInstance().Login(ID, Pass);
                }
            }
        });
    }
    public class MessageHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            String data = (String)msg.obj;
            Toast tst = Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG);
            tst.show();

            if(data.matches(".*succeeded.*")) {
                ConfigProvider.putIntData(getApplicationContext(), "Initkey", 2);
                ConfigProvider.putStringData(getApplicationContext(), "Name", LoginSession.GetInstance().GetName());
                ConfigProvider.putStringData(getApplicationContext(), "sNum", LoginSession.GetInstance().GetSNum());
                startActivity(new Intent(getApplicationContext(), ContentScreen.class));
                finish();
            }
        }
    }

}
