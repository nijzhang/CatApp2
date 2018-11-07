package com.example.a15625.catapp;

import android.Manifest;
import android.graphics.drawable.Icon;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.ls.LSException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ImageView imageView_test1 ;
    ImageView imageView_test2 ;
    ImageView imageView_test3 ;
    Button start_btn;
    String[] str = new String[10];
    int i = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_btn = (Button)findViewById(R.id.button);
        imageView_test1 = (ImageView)findViewById(R.id.image_view1);
        imageView_test2 = (ImageView)findViewById(R.id.image_view2);
        imageView_test3 = (ImageView)findViewById(R.id.image_view3);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();

            }
        });
    }
    public void fetchData()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().
                            url("https://api.thecatapi.com/api/images/get?format=xml&size=med&results_per_page=3")
                            .build();
                    Response response = okHttpClient.newCall(request).execute();
                    String responseData = response.body().string();
                    getXMLJPG(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getXMLJPG(String xml_String)
    {
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xml_String));
            int eventtype = xmlPullParser.getEventType();
            String url = "";
            while (eventtype !=xmlPullParser.END_DOCUMENT)
            {
                String nodeName = xmlPullParser.getName();
                switch (eventtype)
                {
                    case XmlPullParser.START_TAG:{
                        if("url".equals(nodeName))
                        {
                            url = xmlPullParser.nextText();
                            //arrayList.add(url);
                            str[i++] = url;
                        }
                    }

                }
                eventtype = xmlPullParser.next();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showResponse();
    }

    private void showResponse()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(str[0]).into(imageView_test1);
                Picasso.get().load(str[1]).into(imageView_test2);
                Picasso.get().load(str[2]).into(imageView_test3);
                Log.i("arr",str[2]);
                Log.i("arr",str[1]);
                Log.i("arr",str[0]);

            }
        });
    }
}
