package org.db0.targetcontroller;

import android.app.Application;

import org.db0.targetcontroller.model.FiringSequence;
import org.db0.targetcontroller.model.FiringSequenceItem;
import org.db0.targetcontroller.util.ServoManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Timo/QVIK
 * @since 06.11.2016
 */

public class TargetControllerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        initrealm();

        ServoManager.init();
    }

    private void initrealm() {
        // init realm with default sequence
        Realm realm = Realm.getDefaultInstance();

        FiringSequence firingSequence = realm.where(FiringSequence.class).findFirst();

        if (firingSequence == null) {
            realm.beginTransaction();
            FiringSequence sequence = realm.createObject(FiringSequence.class);
            sequence.setName("Ilmaolympiapistooli 2 x 10s, 2 x 8s, 2 x 6s");

            FiringSequenceItem sequenceItem = new FiringSequenceItem(30, 7, 10, true);
            realm.copyToRealm(sequenceItem);
            sequence.addSequenceItem(sequenceItem);

            sequenceItem = new FiringSequenceItem(30, 7, 10, true);
            realm.copyToRealm(sequenceItem);
            sequence.addSequenceItem(sequenceItem);

            sequenceItem = new FiringSequenceItem(30, 7, 8, true);
            realm.copyToRealm(sequenceItem);
            sequence.addSequenceItem(sequenceItem);

            sequenceItem = new FiringSequenceItem(30, 7, 8, true);
            realm.copyToRealm(sequenceItem);
            sequence.addSequenceItem(sequenceItem);

            sequenceItem = new FiringSequenceItem(30, 7, 6, true);
            realm.copyToRealm(sequenceItem);
            sequence.addSequenceItem(sequenceItem);

            sequenceItem = new FiringSequenceItem(30, 7, 6, true);
            realm.copyToRealm(sequenceItem);
            sequence.addSequenceItem(sequenceItem);

            realm.commitTransaction();
        }

        realm.close();
    }
}
