package com.example.bluetoothchat.adapter;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.example.bluetoothchat.R;
import com.example.bluetoothchat.btactivity.ChatWindow;
import com.example.bluetoothchat.btactivity.DeviceList;
import com.example.bluetoothchat.config.Config;
import com.example.bluetoothchat.constants.AppConstants;
import com.example.bluetoothchat.constants.DialogBoxMessage;
import com.example.bluetoothchat.constants.ToastMessage;
import com.example.bluetoothchat.utils.CommonUtil;
import com.example.bluetoothchat.utils.DialogBoxUtil;
import java.util.List;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private LayoutInflater inflater;

    public DeviceListAdapter(@NonNull Context context, @NonNull List<BluetoothDevice> list) {
        super(context, R.layout.device_list_layout, list);
        context = getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.device_list_layout, null, true);
        TextView text = view.findViewById(R.id.deviceName);
        Context context = DeviceList.getContext();
        Activity activity = DeviceList.getActivity();
        ListView listView = activity.findViewById(R.id.deviceList);
        ProgressBar progressBar = activity.findViewById(R.id.progressInDevice);

        BluetoothDevice bluetoothDevice = getItem(position);
        try {
            CommonUtil.getDeviceType(view, bluetoothDevice);
            text.setText(bluetoothDevice.getName().toUpperCase());
        } catch (Exception ex) {
            Log.e(TAG, "Error while loading device list");
            DialogBoxUtil.exitAppOnError(DialogBoxMessage.UNABLE_TO_LOAD_DEVICE_LIST);
        }

        View textView = view.findViewById(R.id.layout);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonUtil.isBluetoothEnabled()) {
                    Config.getCurrentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogBoxUtil.exitAppOnError(DialogBoxMessage.UNABLE_TO_CONNECT);
                        }
                    });

                    return;
                }

                textView
                  .setBackground(ContextCompat.getDrawable(getContext(), R.drawable.received_msg));
                Config.getConnectThread(bluetoothDevice).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (android.os.Build.VERSION.SDK_INT > 25) {
                                    Toast.makeText(view.getContext(),
                                      ToastMessage.CONNECTING.getMessage(),
                                      Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.VISIBLE);
                                listView.setAlpha(.8f);
                            }
                        });

                        int connectTryCount = 0;
                        while (!Config.getConnectThread(bluetoothDevice).isConnected()) {
                            try {
                                Thread.sleep(3000);
                                connectTryCount++;
                                if (connectTryCount == AppConstants.CONNECT_TRY_LIMIT) {
                                    Config.getConnectThread().interrupt();
                                    Config.setConnectThreadAsNull();

                                    DeviceList.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (android.os.Build.VERSION.SDK_INT > 25) {
                                                Toast.makeText(view.getContext(),
                                                  ToastMessage.UNABLE_TO_CONNECT
                                                    .getMessage(),
                                                  Toast.LENGTH_SHORT).show();
                                            }
                                            Intent i = new Intent(
                                              DeviceList.getContext(),
                                              DeviceList.class);
                                            activity.finish();
                                            activity.overridePendingTransition(0, 0);
                                            activity.startActivity(i);
                                            activity.overridePendingTransition(0, 0);
                                        }
                                    });
                                    return;
                                }
                            } catch (InterruptedException e) {
                                Log.e(TAG, "Error while connecting to device");
                                DialogBoxUtil.exitAppOnError(DialogBoxMessage.UNABLE_TO_CONNECT);
                            }
                        }

                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (android.os.Build.VERSION.SDK_INT > 25) {
                                    Toast
                                      .makeText(view.getContext(),
                                        ToastMessage.CONNECTED_SUCCESSFULLY.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                }
                                activity.finish();
                                Intent intent = new Intent(context, ChatWindow.class);
                                activity.startActivity(intent);
                            }
                        });
                    }
                }).start();
            }
        });
        return view;
    }

}
