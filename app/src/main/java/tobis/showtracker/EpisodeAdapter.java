package tobis.showtracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by TobiX on 29.07.2016.
 *
 * Custom Adapter to use layouts with multiple textviews items in Listviews
 */
class EpisodeAdapter extends ArrayAdapter<Episode> {
    private final Context context;
    private final ArrayList<Episode> data;
    private final int layoutResourceId;

    EpisodeAdapter(Context context, int layoutResourceId, ArrayList<Episode> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public @NonNull  View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textViewName = (TextView)row.findViewById(R.id.showname);
            holder.textViewNumbers = (TextView)row.findViewById(R.id.seasonepisodenumbers);
            holder.textViewDate = (TextView)row.findViewById(R.id.date);
            holder.imageViewEye = (ImageView) row.findViewById(R.id.eye_image);
            holder.switchCompatWatched = (SwitchCompat)row.findViewById(R.id.switch_watched);


            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Episode episode = data.get(position);

        holder.textViewName.setText(episode.getShowName());
        holder.textViewNumbers.setText(episode.getSeasonEpisodeAsString());
        holder.textViewDate.setText(episode.getDateAsString());

        holder.imageViewEye.setOnClickListener(new View.OnClickListener() {
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
                RelativeLayout itemlayout = (RelativeLayout)buttonView.getParent();
                ImageView eye_image = (ImageView) itemlayout.findViewById(R.id.eye_image);
                if(isChecked){
                    //seen
                    eye_image.setImageResource(R.drawable.ic_eye_seen_v2);
                }else{
                    //unseen
                    eye_image.setImageResource(R.drawable.ic_eye_unseen_v2);
                }
            }
        });

        return row;
    }

    private static class ViewHolder
    {
        TextView textViewName;
        TextView textViewNumbers;
        TextView textViewDate;
        ImageView imageViewEye;
        SwitchCompat switchCompatWatched;
    }
}