package tobis.showtracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WatchListFragment extends Fragment {
    EpisodeJSON ejson;
    private static int sortingType; //0 = by Date, 1 = by Name
    private static List<Episode> watchList; //contains all unseen episodes
    private static EpisodeRecycleAdapter mAdapter;
    private static RecyclerView mRecyclerView;
    private static Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sortingType = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.watch_list_fragment, container, false);
        context = view.getContext();
        final FloatingActionButton fab_save = (FloatingActionButton) view.findViewById(R.id.fab_save);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.watchlistRV);
        /* improved performance if size of the layout doesnt change */
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, R.drawable.divider));

        //hide fab when scrolling up, so the last isnt hidden
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE //scrolling
                                && dy > 0) { //scrolling down
                            fab_save.hide();
                        } else {
                            //scroll up
                            if (mAdapter.getCounterOfActiveSwitches() > 0) { //1+ selected item(s)
                                fab_save.show();
                            }
                        }
                    }
                });

        //load watchlist from saved file
        ejson = new EpisodeJSON(context);
        watchList = ejson.readFromFile();

        List<Integer> usedIdList = new ArrayList<>();
        for (Episode e : watchList) {
            e.setWatchedStatus(false);
            usedIdList.add(e.getSeasonID());
        }

        int unusedId = 0;
        while (usedIdList.contains(unusedId)) {
            unusedId++;
        }

        WatchListFunctions wlFunc = new WatchListFunctions();

        //if was opened by addnewshowfragment
        Bundle args = getArguments();
        if(args != null){
            String season[] = args.getStringArray("season");
            if(season != null){
                watchList.addAll(wlFunc.getEpisodesfromSeasonString(season, unusedId));
                sortWatchList();
                writeWatchListToEJSON();
            }
        }

        //load released episodes of watchlist in releasedEpisodeList
        //releasedEpisodeList = wlFunc.getReleasedEpisodeList(watchList);
        //mAdapter = new EpisodeRecycleAdapter(context, watchList);
        //mRecyclerView.setAdapter(mAdapter);
        updateList(null);

        if (fab_save != null) {
            fab_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Episode> removeList = new ArrayList<>();
                    for (Episode watchListEpisode : watchList) {
                        if (watchListEpisode.isWatchedStatus()) {
                            //save all marked as watched episodes into removeList
                            removeList.add(watchListEpisode);
                        }
                    }
                    mAdapter.unselectAllItems(mRecyclerView);
                    watchList.removeAll(removeList);
                    updateList(null);
                    writeWatchListToEJSON();
                }
            });
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.watchlist_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_new:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("owner", 2);
                intent.putExtra("switchToAddNew", true);
                startActivity(intent);
                return true;
            case R.id.action_sort_by_name:
                sortingType = 1;
                sortWatchList();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_sort_by_date:
                sortingType = 0;
                sortWatchList();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_unselect_all:
                mAdapter.unselectAllItems(mRecyclerView);
                return true;
            default:
                Log.e("optionItemSelected", "unknown" + String.valueOf(id));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /**********************************************************************
     * Additional Functions
     **********************************************************************/
    /**********************************************************************
     * Compare Functions for sorting
     **********************************************************************/
    private static void sortWatchList() {
        switch (sortingType) {
            case 0:
                Collections.sort(watchList, comparator_date);
                break;
            case 1:
                Collections.sort(watchList, comparator_name);
                break;
            default:
                break;
        }
    }

    private static Comparator<Episode> comparator_name = new Comparator<Episode>() {
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

    private static Comparator<Episode> comparator_date = new Comparator<Episode>() {
        @Override
        public int compare(Episode lhs, Episode rhs) {
            int compare_score;
            compare_score = lhs.getDate().compareTo(rhs.getDate());
            return compare_score;
        }
    };

    /**
     * writes watchlist in a file with json
     */
    private void writeWatchListToEJSON() {
        try {
            ejson.writeToFile(watchList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * update list with changedList and sort it
     */
    public static void updateList(List<Episode> changedList) {
        if (changedList != null)
            watchList = changedList;
        mAdapter = new EpisodeRecycleAdapter(context, watchList);
        sortWatchList();
        mRecyclerView.setAdapter(mAdapter);
    }
}
