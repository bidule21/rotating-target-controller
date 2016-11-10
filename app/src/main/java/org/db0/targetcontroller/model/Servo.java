package org.db0.targetcontroller.model;

import io.realm.RealmObject;

/**
 * @author Timo/QVIK
 * @since 06.11.2016
 */

public class Servo extends RealmObject {
    private int visible;
    private int hidden;

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }
}
