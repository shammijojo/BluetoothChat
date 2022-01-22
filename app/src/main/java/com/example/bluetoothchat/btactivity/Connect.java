package com.example.bluetoothchat.btactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetoothchat.R;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.utils.CommonUtil;

public class Connect extends AppCompatActivity {

    private Button accept, request;
    private TextView loadingMessage;
    private static ProgressBar progressBar;
    private View fullScreen;
    private static Activity context;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        initialise();

        try{
            if(!CommonUtil.isBluetoothEnabled()){
                overridePendingTransition(0,0);
                CommonUtil.confirmBluetoothEnable();
            }
        }
        catch (Exception ex){
            CommonUtil.errorDialogBox("Some error occurred!! Try again later",0);
        }


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
                finish();
                Intent i = new Intent(getApplicationContext(), DeviceList.class);
                startActivity(i);
            }
        });
    }

    private void initialise() {
        context = this;
        accept = findViewById(R.id.accept);
        request = findViewById(R.id.request);
        loadingMessage = findViewById(R.id.loadmessage);
        progressBar = findViewById(R.id.progress);
        fullScreen = findViewById(R.id.connect);
    }

    public static Activity getContext() {
        return context;
    }

}