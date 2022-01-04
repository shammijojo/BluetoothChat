package com.example.bluetoothchat.connection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.bluetoothchat.Config;
import com.example.bluetoothchat.MainActivity;
import com.example.bluetoothchat.R;

public class Connect extends AppCompatActivity {

    Button accept,request;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        context=getApplicationContext();

        //handler=new Handler();

        initialise();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.getAcceptThread().start();

            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }

    private void initialise() {
        accept=findViewById(R.id.accept);
        request=findViewById(R.id.request);
    }

//    public Handler getHandler(){
//        //return handler;
//    }

    public static Context getContext(){
        return context;
    }


}