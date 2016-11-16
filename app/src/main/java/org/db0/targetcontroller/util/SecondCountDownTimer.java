package org.db0.targetcontroller.util;

import android.os.Handler;

/**
 * @author Timo/QVIK
 * @since 16.11.2016
 */

public abstract class SecondCountDownTimer {
    private final int seconds;
    private TimerThread timer;
    private final Handler handler;

    /**
     * @param secondsToCountDown Total time in seconds you wish this timer to count down.
     */
    public SecondCountDownTimer(int secondsToCountDown) {
        seconds = secondsToCountDown;
        handler = new Handler();
        timer = new TimerThread(secondsToCountDown);
    }

    /**
     * This will cancel your current timer and start a new one.
     * This call will override your timer duration only one time.
     **/
    public SecondCountDownTimer start(int secondsToCountDown) {
        if (timer.getState() != Thread.State.NEW) {
            timer.interrupt();
            timer = new TimerThread(secondsToCountDown);
        }
        timer.start();
        return this;
    }

    /**
     * This will cancel your current timer and start a new one.
     **/
    public SecondCountDownTimer start() {
        return start(seconds);
    }

    public void cancel() {
        if (timer.isAlive()) timer.interrupt();
        timer = new TimerThread(seconds);
    }

    private Runnable getOnTickRunnable(final int second) {
        return new Runnable() {
            @Override
            public void run() {
                onTick(second);
            }
        };
    }

    private Runnable getFinishedRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                onFinish();
            }
        };
    }

    private class TimerThread extends Thread {

        private int count;

        private TimerThread(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            try {
                while (count != 0) {
                    handler.post(getOnTickRunnable(count--));
                    sleep(1000);
                }
            } catch (InterruptedException e) {
            }
            if (!isInterrupted()) {
                handler.post(getFinishedRunnable());
            }
        }
    }

    public abstract void onFinish();

    public abstract void onTick(int secondsUntilFinished);
}
