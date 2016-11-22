package org.db0.targetcontroller.model;

/**
 * @author Timo/QVIK
 * @since 22.11.2016
 */

public class LoadTimerAdvancedMessage {
    private int progress;

    public LoadTimerAdvancedMessage(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
