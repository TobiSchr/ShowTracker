package tobis.showtracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by TobiX on 04.09.2016.
 */
public class AddNewShowActivity extends Fragment {
    //https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawer
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.watch_list_fragment, container, false);
        return view;
    }
}
