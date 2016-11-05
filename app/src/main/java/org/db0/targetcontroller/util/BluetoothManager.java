package org.db0.targetcontroller.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.db0.targetcontroller.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * @author Timo/QVIK
 * @since 05.11.2016
 */

public class BluetoothManager {
    private static final String TAG = BluetoothManager.class.getSimpleName();

    private static final String DEFAULT_BT_DEVICE_NAME = "HC-06";
    private static final UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static BluetoothManager instance;
    private Set<BluetoothDevice> bondedDevices;
    private BluetoothSocket btSocket;
    private OutputStream outStream;

    private BluetoothManager() {

    }

    public static BluetoothManager getInstance() {
        if (instance == null) {
            synchronized (BluetoothManager.class) {
                if (instance == null) {
                    instance = new BluetoothManager();
                }
            }
        }

        return instance;
    }

    /**
     * Is bluetooth enabled
     *
     * @return true if enabled, false if not supported by hardware or not enabled
     */
    public static boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public void connect(Context context) {
        bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();

        for (BluetoothDevice device : bondedDevices) {
            if (device.getName().equals(DEFAULT_BT_DEVICE_NAME)) {
                try {
                    btSocket = device.createRfcommSocketToServiceRecord(BT_UUID);
                    btSocket.connect();
                    Toast.makeText(context, R.string.bluetooth_connected, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    try {
                        btSocket.close();
                    } catch (IOException e1) {
                        Log.e(TAG, "error closing socket", e1);
                    }
                    Log.e(TAG, "error opening socket", e);
                    Toast.makeText(context, R.string.bluetooth_connection_failed, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void setPosition(int number) {
        if (number < 0 || number > 180) {
            throw new IllegalArgumentException("Number out of bounds");
        }
        try {
            btSocket.getOutputStream().write((byte)(number & 0xFF));
        } catch (IOException e) {
            Log.e(TAG, "error sending position", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "error sending position, socket not ready", e);
        }
    }
}
