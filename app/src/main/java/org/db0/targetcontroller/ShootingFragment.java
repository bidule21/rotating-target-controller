package org.db0.targetcontroller;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.db0.targetcontroller.util.SecondCountDownTimer;

import java.util.Locale;


public class ShootingFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PREPARE = "prepare";
    private static final String ARG_FIRE = "fire";
    private static final String ARG_PAUSEAFTER = "pauseafter";

    private int prepare;
    private int fire;
    private boolean pauseAfter;

    private SecondCountDownTimer prepareTimer;
    private SecondCountDownTimer firingTimer;

    public ShootingFragment() {
    }

    public static ShootingFragment newInstance(int prepare, int fire, boolean pauseAfter) {
        ShootingFragment fragment = new ShootingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PREPARE, prepare);
        args.putInt(ARG_FIRE, fire);
        args.putBoolean(ARG_PAUSEAFTER, pauseAfter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prepare = getArguments().getInt(ARG_PREPARE);
            fire = getArguments().getInt(ARG_FIRE);
            pauseAfter = getArguments().getBoolean(ARG_PAUSEAFTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shooting, container, false);

        final TextView prepareTime = (TextView) view.findViewById(R.id.prepare_time);
        prepareTime.setText(String.format(Locale.getDefault(), "%d", prepare));
        final ProgressBar prepareProgress = (ProgressBar) view.findViewById(R.id.progress_wait);
        prepareProgress.setMax(prepare);
        prepareProgress.setProgress(prepare);


        final TextView firingTime = (TextView) view.findViewById(R.id.firing_time);
        firingTime.setText(String.format(Locale.getDefault(), "%d", fire));
        final ProgressBar firingProgress = (ProgressBar) view.findViewById(R.id.progress_shoot);
        firingProgress.setMax(prepare);
        firingProgress.setProgress(prepare);

        Button startButton = (Button) view.findViewById(R.id.button_start_sequence);
        startButton.setOnClickListener(this);


        prepareTimer = new SecondCountDownTimer(prepare) {
            @Override
            public void onTick(int progress) {
                prepareProgress.setProgress(progress);
                prepareTime.setText(String.format(Locale.getDefault(), "%d", progress));
            }

            @Override
            public void onFinish() {
                prepareProgress.setProgress(0);
                prepareTime.setText("0");
                firingTimer.start();
            }
        };

        firingTimer = new SecondCountDownTimer(fire) {
            @Override
            public void onTick(int progress) {
                firingProgress.setProgress(progress);
                firingTime.setText(String.format(Locale.getDefault(), "%d", progress));
            }

            @Override
            public void onFinish() {
                firingProgress.setProgress(0);
                firingTime.setText("0");
                if (getActivity() != null) {
                    ((ShootingActivity) getActivity()).next();
                }
            }
        };

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_start_sequence) {
            prepareTimer.start();
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        prepareTimer.cancel();
        firingTimer.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();

        prepareTimer.cancel();
        firingTimer.cancel();
    }
}
