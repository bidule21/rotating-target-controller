package org.db0.targetcontroller.model;

import io.realm.RealmObject;

/**
 * @author Timo/QVIK
 * @since 06.11.2016
 */

public class FiringSequenceItem extends RealmObject {
    private int load;
    private int prepare;
    private int fire;
    private boolean pauseAfter;

    public FiringSequenceItem() {
    }

    public FiringSequenceItem(int load, int prepare, int fire, boolean pauseAfter) {
        this.load = load;
        this.prepare = prepare;
        this.fire = fire;
        this.pauseAfter = pauseAfter;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public int getPrepare() {
        return prepare;
    }

    public void setPrepare(int prepare) {
        this.prepare = prepare;
    }

    public int getFire() {
        return fire;
    }

    public void setFire(int fire) {
        this.fire = fire;
    }

    public boolean isPauseAfter() {
        return pauseAfter;
    }

    public void setPauseAfter(boolean pauseAfter) {
        this.pauseAfter = pauseAfter;
    }
}
