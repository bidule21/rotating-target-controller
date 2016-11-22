package org.db0.targetcontroller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.db0.targetcontroller.model.FiringSequence;
import org.db0.targetcontroller.model.FiringSequenceItem;
import org.db0.targetcontroller.util.ServoManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ShootingActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ShootingSequenceAdapter adapter;

    private boolean doubleBackToExitPressedOnce = false;
    private Handler quitHandler = new Handler();
    private static final int BACKPRESS_DELAY = 2000;
    private Toast quitToast;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shooting);

        viewPager = (ViewPager) findViewById(R.id.shooting_viewpager);

        Realm realm = Realm.getDefaultInstance();
        FiringSequence firingSequence = realm.where(FiringSequence.class).findFirst();
        adapter = new ShootingSequenceAdapter(getSupportFragmentManager(), firingSequence.getSequence());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int lastPosition = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ShootingFragment fragment = (ShootingFragment) adapter.instantiateItem(viewPager, lastPosition);
                ServoManager.getInstance().getBus().unregister(fragment);

                fragment = (ShootingFragment) adapter.instantiateItem(viewPager, position);
                ServoManager.getInstance().getBus().register(fragment);

                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ShootingFragment fragment = (ShootingFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
        ServoManager.getInstance().getBus().register(fragment);

        quitToast = Toast.makeText(this, R.string.message_doubleclick_to_exit, Toast.LENGTH_SHORT);
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
            return ShootingFragment.newInstance(item.getLoad(), item.getPrepare(), item.getFire(), item.isPauseAfter());
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            ServoManager.getLoadTimer().cancel();
            ServoManager.getPrepareTimer().cancel();
            ServoManager.getFiringTimer().cancel();

            quitToast.cancel();
        } else {
            this.doubleBackToExitPressedOnce = true;

            quitToast.show();

            quitHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, BACKPRESS_DELAY);
        }
    }
}
