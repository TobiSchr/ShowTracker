package tobis.showtracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import org.joda.time.LocalDate;

/**
 * Created by TobiX on 04.09.2016.
 * outdated
 */
public class AddNewShowFragment extends Fragment {
    private static LocalDate date;
    private static EditText editStartDate;

    //https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawer
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.add_new_fragment, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addnew_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editStartDate = (EditText) getView().findViewById(R.id.startdate);
        final DialogFragment newFragment = new DatePickerFragment();

        editStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    newFragment.show(getFragmentManager(), "Date Picker");
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            LocalDate today = new LocalDate();
            int year = today.getYear();
            int month = today.getMonthOfYear();
            int day = today.getDayOfMonth();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Log.i("month", String.valueOf(month));
            date = new LocalDate(year, month + 1, day);
            editStartDate.setText(date.toString("dd.MM.yyyy"));
        }
    }
}