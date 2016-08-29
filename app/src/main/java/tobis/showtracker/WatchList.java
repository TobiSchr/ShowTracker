package tobis.showtracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WatchList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private EpisodeRecycleAdapter mAdapter;
    private ArrayList<Episode> watchList;
    private ArrayList<Episode> markedWatchedList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mRecyclerView = (RecyclerView) findViewById(R.id.watchlistRV);
        /* improved performance if size of the layout doesnt change */
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));

        mRecyclerView.
                addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE //scrolling
                                && dy > 0) { //scrolling down
                            fab.hide();
                        } else {
                            //scroll up
                            if (mAdapter.getCounterOfActiveSwitches() > 0) { //1+ selected item(s)
                                fab.show();
                            }
                        }
                    }
                });

        watchList = new ArrayList<>();
        //TODO replace with dynamic method
        LocalDate ld = LocalDate.now();
        watchList.add(new Episode("Game of Thrones", 6, 9, ld, false));
        ld = ld.plusDays(1);
        watchList.add(new Episode("Game of Thrones", 7, 2, ld, false));
        ld = ld.plusDays(1);
        watchList.add(new Episode("Game of Thrones", 6, 10, ld, false));
        ld = ld.plusWeeks(1);
        watchList.add(new Episode("Game of Thrones", 6, 8, ld, false));
        ld = ld.plusWeeks(1);
        watchList.add(new Episode("Breaking Bad", 5, 10, ld, false));
        ld = ld.plusDays(1);
        watchList.add(new Episode("House of Cards", 3, 1, ld, false));
        ld = ld.plusDays(1);
        watchList.add(new Episode("Grey's Anatomy", 13, 23, ld, false));
        ld = ld.plusDays(1);
        watchList.add(new Episode("One Piece", 1, 388, ld, false));
        ld = ld.plusWeeks(2);
        watchList.add(new Episode("One Piece", 1, 389, ld, false));
        ld = ld.plusDays(1);
        watchList.add(new Episode("Game of Thrones", 7, 1, ld, false));
        ld = ld.plusDays(1);
        watchList.add(new Episode("Shameless", 8, 1, ld, false));

        mAdapter = new EpisodeRecycleAdapter(getApplicationContext(), watchList);
        mRecyclerView.setAdapter(mAdapter);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Episode> moveList = new ArrayList<>();
                    for (Episode watchListEpisode : watchList) {
                        if (watchListEpisode.isWatchedStatus()) {
                            //save all marked as watched episodes into moveList
                            moveList.add(watchListEpisode);
                        }
                    }
                    ArrayList<Episode> alreadySeenList = new ArrayList<>(); //TODO move to proper place
                    //move all episodes of moveList to alreadySeen and remove from watchList
                    //alreadySeenList.addAll(moveList); //crashes the app
                    mAdapter.unselectAllItems(mRecyclerView);
                    watchList.removeAll(moveList);
                    mAdapter.notifyDataSetChanged();
                    //TODO
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
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
        if (id == R.id.action_sort_by_name) {
            Comparator<Episode> comparator_name = new Comparator<Episode>() {
                @Override
                public int compare(Episode lhs, Episode rhs) {
                    int compare_score;
                    compare_score = lhs.getShowName().compareToIgnoreCase(rhs.getShowName());
                    compare_score *= 100; //NN00 Namedif00
                    compare_score += lhs.getSeasonNumber() - rhs.getSeasonNumber();
                    compare_score *= 1000; //NNSS000 NamedifSeasonnumdiff00
                    compare_score += lhs.getEpisodeNumber() - rhs.getEpisodeNumber();
                    return compare_score; //NNSSEEE NamedifSeasondifEpisodedif
                }
            };
            Collections.sort(watchList, comparator_name);
            //TODO keep selection
            mAdapter.notifyDataSetChanged();
            return true;
        }

        if (id == R.id.action_sort_by_date) {

            Comparator<Episode> comparator_date = new Comparator<Episode>() {
                @Override
                public int compare(Episode lhs, Episode rhs) {
                    int compare_score;
                    compare_score = lhs.getDate().compareTo(rhs.getDate());
                    return compare_score;
                }
            };
            Collections.sort(watchList, comparator_date);
            //TODO keep selection
            mAdapter.notifyDataSetChanged();
            return true;
        }

        if (id == R.id.action_unselect_all) {
            mAdapter.unselectAllItems(mRecyclerView);
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
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
