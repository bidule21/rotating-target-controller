package org.db0.targetcontroller;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private boolean targetVisible = true;
    private TextView tickHidden;
    private TextView tickVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tickHidden = (TextView) findViewById(R.id.seekbar_tick_hidden);
        tickHidden.setVisibility(View.VISIBLE);
        tickVisible = (TextView) findViewById(R.id.seekbar_tick_visible);
        tickVisible.setVisibility(View.INVISIBLE);

        SeekBar seekBarVisible = (SeekBar) findViewById(R.id.seekbar_visible);
        final TextView seekBarVisibleValue = (TextView) findViewById(R.id.seekbar_value_visible);
        seekBarVisible.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarVisibleValue.setText(String.format(getResources().getConfiguration().locale, "%d", i));
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

        SeekBar seekBarHidden = (SeekBar) findViewById(R.id.seekbar_hidden);
        final TextView seekBarHiddenValue = (TextView) findViewById(R.id.seekbar_value_hidden);
        seekBarHidden.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarHiddenValue.setText(String.format(getResources().getConfiguration().locale, "%d", i));
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

    }

    public void testClick(View view) {
        if (targetVisible) {
            tickVisible.setVisibility(View.INVISIBLE);
            tickHidden.setVisibility(View.VISIBLE);
            targetVisible = false;
        } else {
            targetVisible = true;
            tickVisible.setVisibility(View.VISIBLE);
            tickHidden.setVisibility(View.INVISIBLE);
        }
    }

    public void saveClick(View view) {

    }
}