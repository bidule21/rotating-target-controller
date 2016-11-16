package org.db0.targetcontroller.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Bus;

import org.db0.targetcontroller.R;
import org.db0.targetcontroller.ShootingActivity;
import org.db0.targetcontroller.model.FiringTimerAdvancedMessage;
import org.db0.targetcontroller.model.FiringTimerFinishedMessage;
import org.db0.targetcontroller.model.PrepareTimerAdvancedMessage;
import org.db0.targetcontroller.model.PrepareTimerFinishedMessage;
import org.db0.targetcontroller.model.Servo;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import io.realm.Realm;

/**
 * @author Timo/QVIK
 * @since 05.11.2016
 */

public class ServoManager {
    private static final String TAG = ServoManager.class.getSimpleName();

    private static final String DEFAULT_BT_DEVICE_NAME = "HC-06";
    private static final UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static ServoManager instance;

    private static SecondCountDownTimer prepareTimer;
    private static SecondCountDownTimer firingTimer;

    private static int hidden = 0;
    private static int visible = 90;

    private Set<BluetoothDevice> bondedDevices;
    private BluetoothSocket btSocket;
    private BluetoothDevice targetDevice;

    private static Bus bus = new Bus();

    private ServoManager() {

    }

    public static ServoManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ServoManager not init()ed");
        }

        return instance;
    }

    public static void init() {
        instance = new ServoManager();

        prepareTimer = new SecondCountDownTimer() {
            @Override
            public void onTick(int progress) {
                bus.post(new PrepareTimerAdvancedMessage(progress));
            }

            @Override
            public void onFinish() {
                bus.post(new PrepareTimerFinishedMessage());
            }
        };

        firingTimer = new SecondCountDownTimer() {
            @Override
            public void onTick(int progress) {
                bus.post(new FiringTimerAdvancedMessage(progress));
            }

            @Override
            public void onFinish() {
                bus.post(new FiringTimerFinishedMessage());
            }
        };

        Realm realm = Realm.getDefaultInstance();
        Servo servo = realm.where(Servo.class).findFirst();
        if (servo != null) {
            hidden = servo.getHidden();
            visible = servo.getVisible();
        }
        realm.close();
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

    public boolean connect(Context context) {
        bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();

        for (BluetoothDevice device : bondedDevices) {
            if (device.getName().equals(DEFAULT_BT_DEVICE_NAME)) {
                try {
                    targetDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(device.getAddress());
                    btSocket = targetDevice.createRfcommSocketToServiceRecord(BT_UUID);
                    btSocket.connect();
                    Toast.makeText(context, R.string.bluetooth_connected, Toast.LENGTH_SHORT).show();

                    return true;
                } catch (IOException e) {
                    try {
                        btSocket.close();
                    } catch (IOException e1) {
                        Log.e(TAG, "error closing socket", e1);

                        return false;
                    }
                    Log.e(TAG, "error opening socket", e);
                    Toast.makeText(context, R.string.bluetooth_connection_failed, Toast.LENGTH_LONG).show();

                    return false;
                }
            }
        }

        if (targetDevice == null) {
            Toast.makeText(context, R.string.bluetooth_no_device, Toast.LENGTH_LONG).show();

            return false;
        }

        return true;
    }

    public void disconnect() {
        if (btSocket != null && btSocket.isConnected()) {
            try {
                btSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "error closing socket", e);
            }
        }

        targetDevice = null;
        bondedDevices = null;
    }

    public void sendPosition(int number) {
        if (number < 0 || number > 180) {
            throw new IllegalArgumentException("Number out of bounds");
        }
        try {
            btSocket.getOutputStream().write((byte) (number & 0xFF));
        } catch (IOException e) {
            Log.e(TAG, "error sending position", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "error sending position, socket not ready", e);
        }
    }

    public void updateServoPosisions() {
        Realm realm = Realm.getDefaultInstance();
        Servo servo = realm.where(Servo.class).findFirst();
        if (servo != null) {
            hidden = servo.getHidden();
            visible = servo.getVisible();
        }
        realm.close();
    }

    public void showTarget() {
        sendPosition(visible);
    }

    public void hideTarget() {
        sendPosition(hidden);
    }

    public Bus getBus() {
        return bus;
    }

    public static SecondCountDownTimer getPrepareTimer() {
        return prepareTimer;
    }

    public static SecondCountDownTimer getFiringTimer() {
        return firingTimer;
    }
}
