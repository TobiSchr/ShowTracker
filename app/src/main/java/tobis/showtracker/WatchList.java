package tobis.showtracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class WatchList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv = (ListView)findViewById(R.id.watchlistLV);

        //TODO FILL VALUES DYNAMIC FROM SAVEFILE
        /*
        ArrayList<String>values = new ArrayList<>();
        values.add("GoT - s6e10 - 10.06.2016");
        values.add("Robot - s2e2 - 20.07.2016");
        values.add("Robot - s2e3 - 27.07.2016");
        values.add("Greys - s13e1 - 24.09.2016");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.single_listitem_with_switch, R.id.text1, values);
        lv.setAdapter(adapter);
        */

        //TODO use this if episodeadapter is working
        LocalDate ld = LocalDate.now();
        ArrayList<Episode>episodeList = new ArrayList<>();
        episodeList.add(new Episode("GoT", 6, 9, ld));
        ld = ld.plusDays(1);
        episodeList.add(new Episode("GoT", 6, 10, ld));
        ld = ld.plusWeeks(1);
        episodeList.add(new Episode("BrBa", 5, 10, ld));
        ld = ld.plusDays(1);
        episodeList.add(new Episode("HoC", 3, 1, ld));
        ld = ld.plusDays(1);
        episodeList.add(new Episode("Greys", 13, 23, ld));
        ld = ld.plusDays(1);
        episodeList.add(new Episode("OP", 1, 388, ld));

        ArrayAdapter<Episode> adapter2 = new EpisodeAdapter(this, R.layout.single_listitem_with_switch_multitextview, episodeList);
        lv.setAdapter(adapter2);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemvalue = (String)lv.getItemAtPosition(position);
                Snackbar.make(view, itemvalue, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if(drawer != null){
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer != null){
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.watch_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
