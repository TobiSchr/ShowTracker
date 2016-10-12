package tobis.showtracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TobiS on 02.08.2016.
 *
 * Custom Adapter with Episodes for RecycleView
 */

class EpisodeRecycleAdapter extends RecyclerView.Adapter<EpisodeRecycleAdapter.ViewHolder> {
    private List<Episode> episodes = new ArrayList<>();
    private int counterOfActiveSwitches = 0;
    private Context mContext;

    int getCounterOfActiveSwitches() {
        return counterOfActiveSwitches;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    EpisodeRecycleAdapter(Context context, List<Episode> myDataset) {
        super();
        this.episodes = myDataset;
        mContext = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView textViewName, textViewNumbers, textViewDate;
        ImageView imageViewEye;
        View LayoutView;

        ViewHolder(View v) {
            super(v);
            textViewName = (TextView)v.findViewById(R.id.showname);
            textViewNumbers = (TextView)v.findViewById(R.id.seasonepisodenumbers);
            textViewDate = (TextView)v.findViewById(R.id.date);
            imageViewEye = (ImageView) v.findViewById(R.id.eye_image);
            LayoutView = v;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EpisodeRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView mRecyclerView = (RecyclerView) view.getRootView().findViewById(R.id.watchlistRV);
                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                //int itemPosition = mRecyclerView.indexOfChild(view);
                Episode episodeItem = episodes.get(itemPosition);
                FloatingActionButton fab = (FloatingActionButton) mRecyclerView.getRootView().findViewById(R.id.fab_save);

                if (episodeItem.isWatchedStatus()) {
                    //was true(selected/watched) -> set to false(unselected/unwatched)
                    episodeItem.setWatchedStatus(false);
                    counterOfActiveSwitches--;
                    setDesignToUnselected(view);
                    if (counterOfActiveSwitches <= 0 && fab.getVisibility() == View.VISIBLE) {
                        fab.hide();
                    }
                } else {
                    //was false(unselected/unwatched) -> set to true(selected/watched)
                    episodeItem.setWatchedStatus(true);
                    counterOfActiveSwitches++;
                    setDesignToSelected(view);
                    if (counterOfActiveSwitches > 0 && fab.getVisibility() != View.VISIBLE) {
                        fab.show();
                    }
                }
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //TODO replace with xml
                RecyclerView mRecyclerView = (RecyclerView) view.getRootView().findViewById(R.id.watchlistRV);
                int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                final Episode episodeItem = episodes.get(itemPosition);
                final int interval = episodeItem.getInterval();
                final LocalDate episodeDate = episodeItem.getDate();

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                String title = "Add break for season\n'" + episodeItem.getShowName() + " " + episodeItem.getSeasonEpisodeAsString() + "'";
                String msg = "Old Date: " + episodeItem.getDateAsString() + "\n" + "Select new Date";
                alert.setTitle(title);
                alert.setMessage(msg);

                // Set an EditText view to get user input
                String dateStringArray[] = new String[52];
                for (int i = 0; i < 52; i++) {
                    LocalDate ld = episodeDate.plusDays(interval * (i + 1));
                    String epStr = ld.toString("dd.MM.yy");
                    dateStringArray[i] = epStr;
                }
                final Spinner picker = new Spinner(mContext);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, dateStringArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                picker.setAdapter(adapter);
                alert.setView(picker);
                alert.setCancelable(true);
                alert.setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int days = (picker.getSelectedItemPosition() + 1) * interval;
                                for (Episode e : episodes) {
                                    if (e.getSeasonID() == episodeItem.getSeasonID()
                                            && !e.getDate().isBefore(episodeItem.getDate())) {
                                        e.setDate(e.getDate().plusDays(days));
                                    }
                                }
                                //TODO after removing releasedEL, call watchlist here
                                WatchListFragment.updateList(episodes);
                            }
                        });
                alert.show();
                return true; //return true so no onClick event happens
            }
        });
        return new ViewHolder(v);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return episodes.size();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Episode episode = episodes.get(pos);
        holder.textViewName.setText(episode.getShowName());
        holder.textViewNumbers.setText(episode.getSeasonEpisodeAsString());
        holder.textViewDate.setText(episode.getDateAsString());

        /*********set Text Color*****************/
        LocalDate today = new LocalDate();
        LocalDate epDate = episode.getDate();
        int color;
        if (epDate.compareTo(today) > 0) {
            color = ContextCompat.getColor(mContext, R.color.colorUnreleased);
        } else {
            color = ContextCompat.getColor(mContext, R.color.colorReleased);
        }
        holder.textViewName.setTextColor(color);
        holder.textViewNumbers.setTextColor(color);
        holder.textViewDate.setTextColor(color);
        holder.imageViewEye.setColorFilter(color);
        View view = holder.LayoutView;

        /*********set Background*****************/
        if (episode.isWatchedStatus()) {
            setDesignToSelected(view);
        } else {
            setDesignToUnselected(view);
        }
    }

    /**
     * unselects all items of mRecyclerView
     * unselect means remove background color,
     *
     * @param mRecyclerView in which recyclerview the items shall be unselected
     */
    void unselectAllItems(RecyclerView mRecyclerView) {
        FloatingActionButton fab = (FloatingActionButton) mRecyclerView.getRootView().findViewById(R.id.fab_save);

        for (int itemPos = 0; itemPos < getItemCount(); itemPos++) {
            Episode item = episodes.get(itemPos);
            if (item.isWatchedStatus()) {
                //true => seen
                View view = mRecyclerView.getChildAt(itemPos);
                if (view == null)//dont know if this fixes the problem //TODO better nullpointer fix
                    break;

                //perform changes for unselected state
                item.setWatchedStatus(false);
                counterOfActiveSwitches--;
                setDesignToUnselected(view);
            }
        }
        fab.hide();
    }

    private void setDesignToSelected(View view) {
        //false => unseen
        ImageView eye_image = (ImageView) view.findViewById(R.id.eye_image);
        eye_image.setImageResource(R.drawable.ic_eye_seen_v2);
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorSelected));
    }

    private void setDesignToUnselected(View view) {
        //true => seen
        ImageView eye_image = (ImageView) view.findViewById(R.id.eye_image);
        eye_image.setImageResource(R.drawable.ic_eye_unseen_v2);
        view.setBackgroundColor(Color.TRANSPARENT);
    }
}