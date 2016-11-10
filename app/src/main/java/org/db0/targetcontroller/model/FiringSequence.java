package org.db0.targetcontroller.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * @author Timo/QVIK
 * @since 06.11.2016
 */

public class FiringSequence extends RealmObject {
    private String name;
    private RealmList<FiringSequenceItem> sequence;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<FiringSequenceItem> getSequence() {
        return sequence;
    }

    public void setSequence(RealmList<FiringSequenceItem> sequence) {
        this.sequence = sequence;
    }

    public void addSequenceItem(FiringSequenceItem item) {
        this.sequence.add(item);
    }

    public void removeSequenceItem(int item) {
        this.sequence.remove(item);
    }
}
