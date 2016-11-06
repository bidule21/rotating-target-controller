package org.db0.targetcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.db0.targetcontroller.util.BluetoothManager;

public class MainActivity extends AppCompatActivity {

    private static final int BLUETOOTH_REQUEST_CODE = 12345;
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean btConnected = false;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!BluetoothManager.isBluetoothEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, BLUETOOTH_REQUEST_CODE);
        }

        connectButton = (Button) findViewById(R.id.button_connect);
    }

    public void settingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BLUETOOTH_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "bluetooth started");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, R.string.open_bluetooth_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void connectClick(View view) {
        if (!btConnected) {
            if (BluetoothManager.getInstance().connect(getApplicationContext())) {
                btConnected = true;

                connectButton.setText(R.string.button_disconnect);
            } else {
                btConnected = false;
            }
        } else {
            BluetoothManager.getInstance().disconnect();

            connectButton.setText(R.string.button_connect);

            btConnected = false;
        }
    }

    public void startClick(View view) {
    }
}
