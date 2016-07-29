package tobis.showtracker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by TobiX on 29.07.2016.
 */
public class EpisodeAdapter extends ArrayAdapter<Episode> {
    private final Context context;
    private final ArrayList<Episode> data;
    private final int layoutResourceId;

    public EpisodeAdapter(Context context, int layoutResourceId, ArrayList<Episode> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textView1 = (TextView)row.findViewById(R.id.text1);
            holder.textView2 = (TextView)row.findViewById(R.id.text2);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Episode episode = data.get(position);

        holder.textView1.setText(episode.getShowShortCut());
        holder.textView2.setText(episode.getSeasonEpisodeAsString());
        return row;
    }

    static class ViewHolder
    {
        TextView textView1;
        TextView textView2;
    }
}