package zhukic.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import zhukic.sectionedrecyclerview.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(this.viewPager);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(this.viewPager);

        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        this.adapter = new Adapter(getSupportFragmentManager());
        this.adapter.addFragment(new MovieFragment(), "By name");
        this.adapter.addFragment(new MovieFragment(), "By genre");
        this.adapter.addFragment(new MovieFragment(), "By decade");
        this.adapter.addFragment(new MovieFragment(), "Grid");
        viewPager.setAdapter(this.adapter);
    }

    @Override
    public void onClick(View v) {
        ((MovieFragment) this.adapter.getItem(viewPager.getCurrentItem())).onFabClick();
    }

    private static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            Bundle args = new Bundle();
            args.putInt("POSITION", mFragmentList.size());
            fragment.setArguments(args);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
