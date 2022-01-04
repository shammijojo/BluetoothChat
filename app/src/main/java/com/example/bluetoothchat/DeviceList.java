package com.example.bluetoothchat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DeviceList extends ArrayAdapter<BluetoothDevice> {

    LayoutInflater inflater;

    public DeviceList(@NonNull Context context, @NonNull List<BluetoothDevice> list) {
        super(context,R.layout.device_list_layout,list);
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(R.layout.device_list_layout,null,true);
        TextView text = view.findViewById(R.id.deviceName);

        BluetoothDevice bluetoothDevice=getItem(position);
        text.setText(bluetoothDevice.getName());
        return view;
    }
}
