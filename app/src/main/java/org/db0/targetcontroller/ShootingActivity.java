package org.db0.targetcontroller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.db0.targetcontroller.model.FiringSequence;
import org.db0.targetcontroller.model.FiringSequenceItem;
import org.db0.targetcontroller.util.SecondCountDownTimer;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ShootingActivity extends AppCompatActivity {
    private ViewPager viewPager;

    private SecondCountDownTimer prepareTimer;
    private SecondCountDownTimer firingTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shooting);

        viewPager = (ViewPager) findViewById(R.id.shooting_viewpager);

        Realm realm = Realm.getDefaultInstance();
        FiringSequence firingSequence = realm.where(FiringSequence.class).findFirst();
        ShootingSequenceAdapter adapter = new ShootingSequenceAdapter(getSupportFragmentManager(), firingSequence.getSequence());

        viewPager.setAdapter(adapter);
    }

    private class ShootingSequenceAdapter extends FragmentStatePagerAdapter {
        private List<FiringSequenceItem> items = new ArrayList<>();

        public ShootingSequenceAdapter(FragmentManager fm, List<FiringSequenceItem> items) {
            super(fm);
            this.items.addAll(items);
        }

        public ShootingSequenceAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FiringSequenceItem item = items.get(position);
            return ShootingFragment.newInstance(item.getPrepare(), item.getFire(), item.isPauseAfter());
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

    public void next() {
        if (viewPager != null) {
            int max = viewPager.getAdapter().getCount();
            int next = viewPager.getCurrentItem() + 1;
            viewPager.setCurrentItem(Math.min(next, max));
        }
    }
}
