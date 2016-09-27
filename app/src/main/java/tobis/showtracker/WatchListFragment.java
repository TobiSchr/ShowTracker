package tobis.showtracker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.LocalDate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WatchListFragment extends Fragment {
    private EpisodeRecycleAdapter mAdapter;
    private List<Episode> watchList;
    private List<Episode> markedWatchedList;
    private RecyclerView mRecyclerView;
    private Context context;
    EpisodeJSON ejson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        mRecyclerView.
                addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        ejson = new EpisodeJSON(context);
        watchList = ejson.readFromFile();
        if (watchList == null) {
            watchList = new ArrayList<>();
            LocalDate ld = LocalDate.now();
            watchList.add(new Episode("Game of Thrones", 6, 9, ld, false));
            ld = ld.plusDays(1);
            watchList.add(new Episode("Game of Thrones", 7, 2, ld, false));
            ld = ld.plusDays(1);
            watchList.add(new Episode("Hodentorsion", 7, 2, ld, false));
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
        }
        mAdapter = new EpisodeRecycleAdapter(context, watchList);
        mRecyclerView.setAdapter(mAdapter);

        if (fab_save != null) {
            fab_save.setOnClickListener(new View.OnClickListener() {
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

                    try {
                        ejson.writeToFile(watchList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //TODO
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
}
