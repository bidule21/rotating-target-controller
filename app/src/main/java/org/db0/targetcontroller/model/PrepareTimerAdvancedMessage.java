package org.db0.targetcontroller.model;

/**
 * @author Timo/QVIK
 * @since 16.11.2016
 */

public class PrepareTimerAdvancedMessage {
    private int progress;

    public PrepareTimerAdvancedMessage(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
