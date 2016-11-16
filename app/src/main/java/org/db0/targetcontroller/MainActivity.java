package org.db0.targetcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.db0.targetcontroller.model.FiringSequence;
import org.db0.targetcontroller.util.ServoManager;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int BLUETOOTH_REQUEST_CODE = 12345;
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean btConnected = false;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!ServoManager.isBluetoothEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, BLUETOOTH_REQUEST_CODE);
        }

        connectButton = (Button) findViewById(R.id.button_connect);

        Realm realm = Realm.getDefaultInstance();

        Spinner spinner = (Spinner) findViewById(R.id.spinner_shooting_selector);
        SequenceSpinnerAdapter spinnerAdapter = new SequenceSpinnerAdapter(this, realm.where(FiringSequence.class).findAll());
        spinner.setAdapter(spinnerAdapter);
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
            if (ServoManager.getInstance().connect(getApplicationContext())) {
                btConnected = true;

                connectButton.setText(R.string.button_disconnect);
            } else {
                btConnected = false;
            }
        } else {
            ServoManager.getInstance().disconnect();

            connectButton.setText(R.string.button_connect);

            btConnected = false;
        }
    }

    public void startClick(View view) {
        ServoManager.getInstance().updateServoPosisions();

        Intent intent = new Intent(this, ShootingActivity.class);
        startActivity(intent);
    }

    private class SequenceSpinnerAdapter extends RealmBaseAdapter<FiringSequence> implements SpinnerAdapter {

        private final OrderedRealmCollection<FiringSequence> data;

        public SequenceSpinnerAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<FiringSequence> data) {
            super(context, data);

            this.data = data;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText(data.get(i).getName());
            return textView;
        }
    }
}
