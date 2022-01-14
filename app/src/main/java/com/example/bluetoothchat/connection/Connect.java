package com.example.bluetoothchat.connection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetoothchat.MainActivity;
import com.example.bluetoothchat.R;
import com.example.bluetoothchat.config.Config;

public class Connect extends AppCompatActivity {

    Button accept, request;
    TextView loadingMessage;
    private static ProgressBar progressBar;
    View fullScreen;
    private static Context context;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        context = getApplicationContext();

        initialise();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setAlpha(1f);

                accept.setClickable(false);
                request.setClickable(false);
                accept.setAlpha(.25f);

                Config.setAcceptThread().start();
                Toast.makeText(view.getContext(), "Waiting for devices...", Toast.LENGTH_LONG).show();
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
        loadingMessage=findViewById(R.id.loadmessage);
        progressBar=findViewById(R.id.progress);
        fullScreen=findViewById(R.id.connect);
    }


    public static Context getContext() {
        return context;
    }

    public static ProgressBar getProgressBar(){
        return progressBar;
    }

    public static Activity getActivity() {
        return activity;
    }

}