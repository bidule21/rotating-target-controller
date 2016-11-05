package org.db0.targetcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.db0.targetcontroller.util.BluetoothManager;

public class MainActivity extends AppCompatActivity {

    private static final int BLUETOOTH_REQUIEST_CODE = 12345;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothManager bluetoothManager = BluetoothManager.getInstance();

        if (!BluetoothManager.isBluetoothEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, BLUETOOTH_REQUIEST_CODE);
        }

        bluetoothManager.connect(getApplicationContext());
    }

    public void settingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BLUETOOTH_REQUIEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "bluetooth started");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, R.string.open_bluetooth_error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
