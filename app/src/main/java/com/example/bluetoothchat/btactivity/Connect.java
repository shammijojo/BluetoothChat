package com.example.bluetoothchat.btactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetoothchat.R;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.DialogBoxMessage;
import com.example.bluetoothchat.constants.ToastMessage;
import com.example.bluetoothchat.utils.CommonUtil;
import com.example.bluetoothchat.utils.DialogBoxUtil;

public class Connect extends AppCompatActivity {

    private Button accept, request;
    private ProgressBar progressBar;
    private static Context context;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twotone_bluetooth_searching_24);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_connect);
        initialise();

        try {
            if (!CommonUtil.isBluetoothEnabled()) {
                overridePendingTransition(0, 0);
                DialogBoxUtil.confirmBluetoothEnable();
            }
        } catch (Exception ex) {
            DialogBoxUtil.errorDialogBox(DialogBoxMessage.ERROR_OCCURRED, 0);
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
                Toast.makeText(view.getContext(), ToastMessage.WAITING_FOR_DEVICES.toString(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        DialogBoxUtil.confirmAppExit(getContext());
    }

    private void initialise() {
        context = this.getApplicationContext();
        accept = findViewById(R.id.accept);
        request = findViewById(R.id.request);
        progressBar = findViewById(R.id.progress);
    }


    public static Context getContext() {
        return context;
    }

    public static Activity getActivity() {
        return (Activity) context;
    }

}