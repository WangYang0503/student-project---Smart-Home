package smart_things.app.android.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import smart_things.app.android.KaaManager.KaaManager;
import smart_things.app.android.R;
import smart_things.app.android.gui.message.IntroductionNotification;

/**
 * shows all available devices
 * and a introductiondialog on the first run of the app
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DrawerLayout mDrawerLayout;

    private StatusFragment statusFragment;
    private CoffeeFragment coffeeFragment;
    private DoorFragment doorFragment;
    private CarFragment carFragment;
    private LightFragment lightFragment;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private static MainActivity thisInstance;


    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        doFirstRun();
        fixContainerVisibility();

        // save the MainActivity instance
        thisInstance = this;

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupTabIcons();

        // change the roomName of the app corresponding to the currently selected tab
        setTitle(R.string.app_name);
        tabLayout.addOnTabSelectedListener(createOnTabChangeListener());
        setInitialTabBarIconColor();

        KaaManager.connectToKaaServer();

    }

    /**
     * checks , if it's the first time the app runs
     * true: the introduction dialog will be shown
     */
    private void doFirstRun() {

        sharedPreferences = getSharedPreferences("MainActivity", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isFirstRun", true)) {
            IntroductionNotification.introductionMainDialog(MainActivity.this);

            editor = sharedPreferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_language) {
            Intent intent = new Intent(this, LanguageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_kaa) {
            //open Kaa administration page
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://172.24.1.10:8080/"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_license) {
            Intent intent = new Intent(this, LicenseActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    private void setupTabIcons() {
        //Setting up Tab icons
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_info_outline_black);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_local_cafe_black);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_lightbulb_black);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_home_black);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_car_black);
    }

    /**
     * creates a {@link android.widget.TabHost.OnTabChangeListener}
     * and sets app roomName corresponding to the currently selected tab
     * <p>
     * So, if the coffee tab is selected the actionBar roomName says "coffee" or a translation of that
     *
     * @return the {@link android.widget.TabHost.OnTabChangeListener} described above
     */
    private TabLayout.OnTabSelectedListener createOnTabChangeListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Drawable icon = tab.getIcon();
                if (icon != null) {
                    // 100% alpha
                    icon.setAlpha(255);
                }

                switch (tabLayout.getSelectedTabPosition()) {
                    case 1:
                        setTitle(R.string.coffee);
                        break;
                    case 2:
                        setTitle(R.string.light);
                        break;
                    case 3:
                        setTitle(R.string.doors);
                        break;
                    case 4:
                        setTitle(R.string.car);
                        break;
                    default:
                        setTitle(R.string.app_name);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Drawable icon = tab.getIcon();
                if (icon != null) {
                    // 70% alpha (as in material guideline): 255 (full) * .7 = 178.5
                    icon.setAlpha(178);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            switch (position) {
                case 0:
                    return statusFragment = new StatusFragment();
                case 1:
                    return coffeeFragment = new CoffeeFragment();
                case 2:
                    return lightFragment = new LightFragment();
                case 3:
                    return doorFragment = new DoorFragment();
                case 4:
                    return carFragment = new CarFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // commented out to remove the tab labels
            /*
            switch (position) {
                case 0:
                    return "Kaffee";
                case 1:
                    return "Licht";
                case 2:
                    return "Türe";
                case 3:
                    return "Auto";
            }
            */
            return null;
        }
    }

    /**
     * sets all tab icons colors to the color.icons_unselected color
     * except of the currently selected tab
     * instead of the default BLACK
     */
    private void setInitialTabBarIconColor() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tempTab = tabLayout.getTabAt(i);
            if (tempTab == null) {
                continue;
            }
            Drawable icon = tempTab.getIcon();
            if (icon == null) {
                continue;
            }
            icon.setTint(getResources().getColor(R.color.icons_selected));

            if (i == tabLayout.getSelectedTabPosition()) {
                icon.setAlpha(255);
            } else {
                // 70% alpha (as in material guideline): 255 (full) * .7 = 178.5
                icon.setAlpha(178);
            }
        }
    }

    /**
     * if the app bar is expanded, the bottom of the container is outside of the screen
     * with the added margin all its content is now visible again.
     * this is needed due to a bug in the android content library
     */
    private void fixContainerVisibility() {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ViewPager contentLayout = (ViewPager) findViewById(R.id.container);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) contentLayout.getLayoutParams();
                int height = toolbar.getMeasuredHeight() + verticalOffset;
                layoutParams.setMargins(0, 0, 0, height);
                contentLayout.requestLayout();
            }
        });
    }

    public static MainActivity getInstance() {
        return thisInstance;
    }

    public StatusFragment getStatusFragment() {
        return statusFragment;
    }

    public CoffeeFragment getCoffeeFragment() {
        return coffeeFragment;
    }

    public DoorFragment getDoorFragment() {
        return doorFragment;
    }

    public CarFragment getCarFragment() {
        return carFragment;
    }

    public LightFragment getLightFragment() {
        return lightFragment;
    }
}
