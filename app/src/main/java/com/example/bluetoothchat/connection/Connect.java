package com.example.bluetoothchat.connection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetoothchat.MainActivity;
import com.example.bluetoothchat.R;
import com.example.bluetoothchat.config.Config;

public class Connect extends AppCompatActivity {

    Button accept, request;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        context = getApplicationContext();

        //handler=new Handler();

        initialise();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accept.setAlpha(0.5f);
                Config.getAcceptThread().start();
                Toast.makeText(view.getContext(), "Waiting for devices...", Toast.LENGTH_SHORT).show();
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

    }

    private void initialise() {
        accept = findViewById(R.id.accept);
        request = findViewById(R.id.request);
    }

//    public Handler getHandler(){
//        //return handler;
//    }

    public static Context getContext() {
        return context;
    }


}