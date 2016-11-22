package org.db0.targetcontroller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.db0.targetcontroller.model.FiringTimerAdvancedMessage;
import org.db0.targetcontroller.model.FiringTimerFinishedMessage;
import org.db0.targetcontroller.model.LoadTimerAdvancedMessage;
import org.db0.targetcontroller.model.LoadTimerFinishedMessage;
import org.db0.targetcontroller.model.PrepareTimerAdvancedMessage;
import org.db0.targetcontroller.model.PrepareTimerFinishedMessage;
import org.db0.targetcontroller.util.ServoManager;

import java.util.Locale;


public class ShootingFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_LOAD = "load";
    private static final String ARG_PREPARE = "prepare";
    private static final String ARG_FIRE = "fire";
    private static final String ARG_PAUSEAFTER = "pauseafter";

    private int load;
    private int prepare;
    private int fire;
    private boolean pauseAfter;

    private View rootView;
    private ProgressBar loadProgress;
    private TextView loadTime;
    private ProgressBar prepareProgress;
    private TextView prepareTime;
    private ProgressBar firingProgress;
    private TextView firingTime;

    private boolean sequenceStarted = false;
    private boolean loaded = false;

    private Button startButton;

    public ShootingFragment() {
    }

    public static ShootingFragment newInstance(int load, int prepare, int fire, boolean pauseAfter) {
        ShootingFragment fragment = new ShootingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LOAD, load);
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
            load = getArguments().getInt(ARG_LOAD);
            prepare = getArguments().getInt(ARG_PREPARE);
            fire = getArguments().getInt(ARG_FIRE);
            pauseAfter = getArguments().getBoolean(ARG_PAUSEAFTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shooting, container, false);

        loadTime = (TextView) rootView.findViewById(R.id.load_time);
        loadTime.setText(String.format(Locale.getDefault(), "%d", load));
        loadProgress = (ProgressBar) rootView.findViewById(R.id.progress_load);
        loadProgress.setMax(load);
        loadProgress.setProgress(load);

        prepareTime = (TextView) rootView.findViewById(R.id.prepare_time);
        prepareTime.setText(String.format(Locale.getDefault(), "%d", prepare));
        prepareProgress = (ProgressBar) rootView.findViewById(R.id.progress_wait);
        prepareProgress.setMax(prepare);
        prepareProgress.setProgress(prepare);


        firingTime = (TextView) rootView.findViewById(R.id.firing_time);
        firingTime.setText(String.format(Locale.getDefault(), "%d", fire));
        firingProgress = (ProgressBar) rootView.findViewById(R.id.progress_shoot);
        firingProgress.setMax(fire);
        firingProgress.setProgress(fire);

        startButton = (Button) rootView.findViewById(R.id.button_start_sequence);
        startButton.setOnClickListener(this);
        Button showTarget = (Button) rootView.findViewById(R.id.button_show_target);
        showTarget.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_start_sequence) {
            // hide show button
            if (loaded) {
                sequenceStarted = true;
                rootView.findViewById(R.id.button_show_target).setVisibility(View.INVISIBLE);

                ServoManager.getInstance().hideTarget();

                ServoManager.getPrepareTimer().start(prepare);
                view.setVisibility(View.GONE);
            } else {
                ServoManager.getLoadTimer().start(load);

                loaded = true;
                startButton.setText(R.string.button_start_sequence);
            }
        }
        if (view.getId() == R.id.button_show_target) {
            if (!sequenceStarted) {
                ServoManager.getInstance().showTarget();
            }
        }
    }

    @Subscribe
    public void loadTimeAdvanced(LoadTimerAdvancedMessage message) {
        if (getUserVisibleHint()) {
            loadProgress.setProgress(message.getProgress());
            loadTime.setText(String.format(Locale.getDefault(), "%d", message.getProgress()));
        }
    }

    @Subscribe
    public void loadTimeFinished(LoadTimerFinishedMessage message) {
        if (getUserVisibleHint()) {
            loadProgress.setProgress(0);
            loadTime.setText("0");
        }
    }
    @Subscribe
    public void prepareTimeAdvanced(PrepareTimerAdvancedMessage message) {
        if (getUserVisibleHint()) {
            prepareProgress.setProgress(message.getProgress());
            prepareTime.setText(String.format(Locale.getDefault(), "%d", message.getProgress()));
        }
    }

    @Subscribe
    public void prepareTimeFinished(PrepareTimerFinishedMessage message) {
        if (getUserVisibleHint()) {
            prepareProgress.setProgress(0);
            prepareTime.setText("0");
            ServoManager.getFiringTimer().start(fire);

            ServoManager.getInstance().showTarget();
        }
    }

    @Subscribe
    public void firingTimeAdvanced(FiringTimerAdvancedMessage message) {
        if (getUserVisibleHint()) {
            firingProgress.setProgress(message.getProgress());
            firingTime.setText(String.format(Locale.getDefault(), "%d", message.getProgress()));
        }
    }

    @Subscribe
    public void firingTimeFinished(FiringTimerFinishedMessage message) {
        if (getUserVisibleHint()) {
            ServoManager.getInstance().hideTarget();

            firingProgress.setProgress(0);
            firingTime.setText("0");

            if (getActivity() != null) {
                ((ShootingActivity) getActivity()).next();
            }

            sequenceStarted = true;
            rootView.findViewById(R.id.button_show_target).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            ServoManager.getInstance().getBus().unregister(this);
        } catch (IllegalArgumentException e) {
            // ignored
        }
    }
}
