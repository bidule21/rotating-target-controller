package org.db0.targetcontroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.db0.targetcontroller.model.Servo;
import org.db0.targetcontroller.util.BluetoothManager;

import io.realm.Realm;

public class SettingsActivity extends AppCompatActivity {
    private boolean targetVisible = true;
    private TextView tickHidden;
    private TextView tickVisible;
    private SeekBar seekBarVisible;
    private SeekBar seekBarHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tickHidden = (TextView) findViewById(R.id.seekbar_tick_hidden);
        tickHidden.setVisibility(View.VISIBLE);
        tickVisible = (TextView) findViewById(R.id.seekbar_tick_visible);
        tickVisible.setVisibility(View.INVISIBLE);

        seekBarVisible = (SeekBar) findViewById(R.id.seekbar_visible);
        final TextView seekBarVisibleValue = (TextView) findViewById(R.id.seekbar_value_visible);
        seekBarVisible.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean userChanged) {
                seekBarVisibleValue.setText(String.format(getResources().getConfiguration().locale, "%d", value));

                if (userChanged) {
                    BluetoothManager.getInstance().sendPosition(value);
                }

                tickVisible.setVisibility(View.VISIBLE);
                tickHidden.setVisibility(View.INVISIBLE);
                targetVisible = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarHidden = (SeekBar) findViewById(R.id.seekbar_hidden);
        final TextView seekBarHiddenValue = (TextView) findViewById(R.id.seekbar_value_hidden);
        seekBarHidden.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean userChanged) {
                seekBarHiddenValue.setText(String.format(getResources().getConfiguration().locale, "%d", value));

                if (userChanged) {
                    BluetoothManager.getInstance().sendPosition(value);
                }

                tickVisible.setVisibility(View.INVISIBLE);
                tickHidden.setVisibility(View.VISIBLE);
                targetVisible = false;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Realm realm = Realm.getDefaultInstance();
        Servo servo = realm.where(Servo.class).findFirst();

        if (servo != null) {
            seekBarHidden.setProgress(servo.getHidden());
            seekBarHiddenValue.setText(String.format(getResources().getConfiguration().locale, "%d", servo.getHidden()));

            seekBarVisible.setProgress(servo.getVisible());
            seekBarVisibleValue.setText(String.format(getResources().getConfiguration().locale, "%d", servo.getVisible()));
        }

        realm.close();
    }

    public void testClick(View view) {
        if (targetVisible) {
            BluetoothManager.getInstance().sendPosition(seekBarHidden.getProgress());

            tickVisible.setVisibility(View.INVISIBLE);
            tickHidden.setVisibility(View.VISIBLE);
            targetVisible = false;
        } else {
            BluetoothManager.getInstance().sendPosition(seekBarVisible.getProgress());

            targetVisible = true;
            tickVisible.setVisibility(View.VISIBLE);
            tickHidden.setVisibility(View.INVISIBLE);
        }
    }

    public void saveClick(View view) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Servo servo = realm.where(Servo.class).findFirst();
        if (servo == null) {
            servo = realm.createObject(Servo.class);
        }
        servo.setHidden(seekBarHidden.getProgress());
        servo.setVisible(seekBarVisible.getProgress());
        realm.commitTransaction();
        realm.close();
    }
}
