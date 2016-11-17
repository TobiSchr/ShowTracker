package tobis.showtracker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by TobiX on 04.09.2016.
 * Main View which has the nav drawer and a placeholder for fragment
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView nvDrawer;
    private NavigationView.OnNavigationItemSelectedListener navViewListener;
    private Bundle args; //be careful, reset to null after usage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate", "mainactivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        args = null;
        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        nvDrawer.setNavigationItemSelectedListener(
                navViewListener = new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flContent, new WatchListFragment())
                    .commit();
        }
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_watchlist:
                fragmentClass = WatchListFragment.class;
                break;
            case R.id.nav_addnew:
                fragmentClass = AddNewSeasonFragment.class;
                break;
            case R.id.nav_link:
                //TODO replace
                fragmentClass = WatchListFragment.class;
                break;
            case R.id.nav_version:
                //TODO replace
                fragmentClass = WatchListFragment.class;
                break;
            default:
                fragmentClass = WatchListFragment.class;
        }
        swapFragment(fragmentClass, args);

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i("onNewIntent", "mainactivity");
        Bundle extras = intent.getExtras();
        /* owner
         * 0 = MainActivity
         * 1 = addNewSeasonFragment
         * 2 = WatchListFragment*/
        int owner = extras.getInt("owner");
        switch (owner){
            case 1: //addNewSeasonFragment click on fab_add
                args = new Bundle();
                args.putStringArray("season", extras.getStringArray("season"));
                navViewListener.onNavigationItemSelected(nvDrawer.getMenu().getItem(0));
                args = null;
                break;
            case 2:
                if (extras.getBoolean("switchToAddNew")) {
                    navViewListener.onNavigationItemSelected(nvDrawer.getMenu().getItem(1));
                }
                break;
            default:
                Log.e("Main_OnNewIntent", intent.toString());
                break;
        }
    }

    private void swapFragment(Class fragmentClass, Bundle args){
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(args != null)
            fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
