package tobis.showtracker;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by TobiS on 02.08.2016.
 *
 * Custom Adapter with Episodes for RecycleView
 */

class EpisodeRecycleAdapter extends RecyclerView.Adapter<EpisodeRecycleAdapter.ViewHolder> {
    private ArrayList<Episode> episodes = new ArrayList<>();
    private int counterOfActiveSwitches = 0;
    private Context mContext;

    public int getCounterOfActiveSwitches() {
        return counterOfActiveSwitches;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    EpisodeRecycleAdapter(Context context, ArrayList<Episode> myDataset) {
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

        ViewHolder(View v) {
            super(v);
            textViewName = (TextView)v.findViewById(R.id.showname);
            textViewNumbers = (TextView)v.findViewById(R.id.seasonepisodenumbers);
            textViewDate = (TextView)v.findViewById(R.id.date);
            imageViewEye = (ImageView) v.findViewById(R.id.eye_image);
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
                Episode item = episodes.get(itemPosition);
                FloatingActionButton fab = (FloatingActionButton) mRecyclerView.getRootView().findViewById(R.id.fab);
                ImageView eye_image = (ImageView) view.findViewById(R.id.eye_image);

                if (item.isWatchedStatus()) {
                    //true => seen
                    item.setWatchedStatus(false);
                    eye_image.setImageResource(R.drawable.ic_eye_unseen_v2);
                    counterOfActiveSwitches--;
                    view.setBackgroundColor(Color.TRANSPARENT);
                    if (counterOfActiveSwitches <= 0 && fab.getVisibility() == View.VISIBLE) {
                        fab.hide();
                    }

                } else {
                    //false => unseen
                    item.setWatchedStatus(true);
                    eye_image.setImageResource(R.drawable.ic_eye_seen_v2);
                    counterOfActiveSwitches++;
                    view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorSelected));
                    if (counterOfActiveSwitches > 0 && fab.getVisibility() != View.VISIBLE) {
                        fab.show();
                    }
                }
            }
        });
        return new ViewHolder(v);
    }

    public void unselectAllItems(RecyclerView mRecyclerView) {
        FloatingActionButton fab = (FloatingActionButton) mRecyclerView.getRootView().findViewById(R.id.fab);

        for (int itemPos = 0; itemPos < getItemCount(); itemPos++) {
            Episode item = episodes.get(itemPos);
            if (item.isWatchedStatus()) {
                //true => seen
                View view = mRecyclerView.getChildAt(itemPos);
                ImageView eye_image = (ImageView) view.findViewById(R.id.eye_image);

                //perform changes for unselected state
                item.setWatchedStatus(false);
                eye_image.setImageResource(R.drawable.ic_eye_unseen_v2);
                counterOfActiveSwitches--;
                view.setBackgroundColor(Color.TRANSPARENT);
            }
        }
        fab.hide();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textViewName.setText(episodes.get(pos).getShowName());
        holder.textViewNumbers.setText(episodes.get(pos).getSeasonEpisodeAsString());
        holder.textViewDate.setText(episodes.get(pos).getDateAsString());

        /*holder.imageViewEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //click on image toggles the state of the switch
                //switch state change listener will change the image
                RelativeLayout itemlayout = (RelativeLayout) v.getParent();
                ((SwitchCompat) itemlayout.findViewById(R.id.switch_watched)).toggle();
            }
        });

        holder.switchCompatWatched.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RelativeLayout item_layout = (RelativeLayout)buttonView.getParent();
                ImageView eye_image = (ImageView) item_layout.findViewById(R.id.eye_image);
                FloatingActionButton fab = (FloatingActionButton) item_layout.getRootView().findViewById(R.id.fab);
                if(isChecked){
                    //seen
                    eye_image.setImageResource(R.drawable.ic_eye_seen_v2);
                    counterOfActiveSwitches++;
                    if (counterOfActiveSwitches > 0 && fab.getVisibility() != View.VISIBLE) {
                        fab.show();
                    }
                }else{
                    //unseen
                    eye_image.setImageResource(R.drawable.ic_eye_unseen_v2);
                    counterOfActiveSwitches--;
                    if (counterOfActiveSwitches <= 0 && fab.getVisibility() == View.VISIBLE) {
                        fab.hide();
                    }
                }
            }
        });
        */

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return episodes.size();
    }
}