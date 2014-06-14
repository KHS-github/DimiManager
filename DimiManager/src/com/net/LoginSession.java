package com.net;

import com.example.DimiManager.LoginScreen;
import com.example.DimiManager.RegisterScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by 현성 on 2014-05-15.
 */
public class LoginSession {
    private String _ID = "";
    private String _Pass = "";
    private String _Name = "";
    private String _studentNum = "";
    private RegisterScreen.MessageHandler _regHandler;
    private LoginScreen.MessageHandler _loginHandler;

    private static LoginSession _Instance = new LoginSession();

    public void SetRegisterHandler(RegisterScreen.MessageHandler appcon)
    {
        _regHandler = appcon;
    }
    public void SetLoginHandler(LoginScreen.MessageHandler appcon){_loginHandler = appcon;}

    public void SetID(String ID){_ID = ID;}
    public void SetPass(String Pass){_Pass = Pass;}
    public String GetID(){return _ID;}
    public String GetPass(){return _Pass;}
    public String GetName(){return _Name;}
    public String GetSNum(){return _studentNum;}

    public static LoginSession GetInstance()
    {
        return _Instance;
    }

    final public int RegisterDataParse(String Data)
    {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(Data.getBytes());
            Document doc = builder.parse(is);
            Element element = doc.getDocumentElement();
            String msg;
            NodeList items = element.getElementsByTagName("registerOK");
            try
            {
                msg = items.item(0).getTextContent();
            }
            catch(NullPointerException ex1)
            {
                element.normalize();
                items = element.getElementsByTagName("error");
                //msg = items.item(0).getTextContent();
                msg = element.getChildNodes().item(0).getTextContent();
            }
            _regHandler.handleMessage(_regHandler.obtainMessage(0, msg));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return -500;
    }

    public void LoginParse(String Data)
    {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(Data.getBytes());
            Document doc = builder.parse(is);
            Element element = doc.getDocumentElement();
            String msg;
            NodeList items = element.getElementsByTagName("name");
            try {
                _Name = items.item(0).getTextContent();
                _studentNum = element.getElementsByTagName("studentNum").item(0).getTextContent();
                msg = "Login succeeded";
            }
            catch(NullPointerException e)
            {
                msg = element.getTextContent();
            }
            _loginHandler.handleMessage(_loginHandler.obtainMessage(0, msg));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public StringBuffer DataCollect(HttpURLConnection urlConn)
    {
        StringBuffer xmlData = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                xmlData.append(line);
            }
            br.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return xmlData;
    }

    public int RegisterUser(String ID, String Pass, String Name, String sNum, String group, String regID)
    {
        final String strURL = "http://thedeblur.com/~cpd/register.php"+"?username="+ID+"&passwd="+Pass+"&name="+Name+"&sNum="+sNum+"&group="+ group+"&regID="+regID;
        Thread sockThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(strURL);
                    HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
                    urlConn.connect();
                    if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        RegisterDataParse(DataCollect(urlConn).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        sockThread.run();
        return -500;
    }

    public int Logout()
    {
        return -500;
    }

    public int Login(String ID, String Pass)
    {
        try {
            final String urlText = "http://thedeblur.com/~cpd/login.php"+"?id="+ID+"&pwd="+Pass;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(urlText);
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        urlConn.connect();
                        if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            LoginParse(DataCollect(urlConn).toString());
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -500;
    }
}
