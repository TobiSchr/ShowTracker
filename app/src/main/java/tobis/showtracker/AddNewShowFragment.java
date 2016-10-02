package tobis.showtracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TobiX on 04.09.2016.
 * outdated
 */
public class AddNewShowFragment extends Fragment {
    private Context context;
    //views which need to be filled
    private EditText etTitle;
    private EditText etSeasonNumber;
    private TextView tvStartDate;
    private EditText etInterval;
    private EditText etEpisodeNumbers;

    //private  CheckBox cbBreak;
    //private  Spinner etBeforeBreak;
    //private  Spinner etAfterBreak;

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
        View view = inflater.inflate(R.layout.add_new_fragment, container, false);
        context = view.getContext();
        etTitle = (EditText) view.findViewById(R.id.title);
        etSeasonNumber = (EditText) view.findViewById(R.id.seasonNumber);
        tvStartDate = (TextView) view.findViewById(R.id.startdate);
        etInterval = (EditText) view.findViewById(R.id.interval);
        etEpisodeNumbers = (EditText) view.findViewById(R.id.episodeNumbers);
        //cbBreak = (CheckBox) view.findViewById(R.id.hasBreak);
        //etBeforeBreak = (Spinner) view.findViewById(R.id.before_break);
        //etAfterBreak = (Spinner) view.findViewById(R.id.after_break);

        tvStartDate.setHint(new LocalDate().toString("dd.MM.yy"));

        final FloatingActionButton fab_add = (FloatingActionButton) view.findViewById(R.id.fab_add);
        if (fab_add != null) {
            fab_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO check if all is set
                    String showName = etTitle.getText().toString().trim();
                    String seasonNumberStr = etSeasonNumber.getText().toString();
                    String intervalStr = etInterval.getText().toString();
                    String episodeNumbersStr = etEpisodeNumbers.getText().toString();
                    String startDateStr = tvStartDate.getText().toString();

                    //check if all fields are filled
                    if (showName.equals("") || seasonNumberStr.equals("")
                            || startDateStr.equals("")
                            || intervalStr.equals("")
                            || episodeNumbersStr.equals("")) {

                        String text = "Please fill in all fields";
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        //fields must be filled to continue
                        return;
                    }

                    if (Integer.getInteger(episodeNumbersStr) == 0) {
                        String text = "Number of Episodes must be greater than 0";
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        //episodeNumbers must be greater than 0 to continue
                        return;
                    }

                    String seasonArray[] = {
                            showName,
                            seasonNumberStr,
                            episodeNumbersStr,
                            startDateStr.substring(4),
                            intervalStr
                    };
                    Log.i("date substring3", startDateStr.substring(3));
                    Log.i("date substring4", startDateStr.substring(4));
                    Log.i("date substring5", startDateStr.substring(5));

                    //TODO put episode in intent and switch back to watchlist/showoverview

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("owner", 1);
                    intent.putExtra("season", seasonArray);
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addnew_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final DialogFragment newFragment = new DatePickerFragment();

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFragment.show(getFragmentManager(), "Date Picker");
            }
        });
    }

    /*
     * By calling the constructor a DatePickerDialog opens, when a date is selected the editSta
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            LocalDate today = new LocalDate();
            int year = today.getYear();
            int month = today.getMonthOfYear() - 1;
            int day = today.getDayOfMonth();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Log.i("month", String.valueOf(month));
            LocalDate startDate = new LocalDate(year, month + 1, day);

            String dayOfWeek;
            switch (startDate.getDayOfWeek()) {
                case 1:
                    dayOfWeek = "(Mo)";
                    break;
                case 2:
                    dayOfWeek = "(Di)";
                    break;
                case 3:
                    dayOfWeek = "(Mi)";
                    break;
                case 4:
                    dayOfWeek = "(Do)";
                    break;
                case 5:
                    dayOfWeek = "(Fr)";
                    break;
                case 6:
                    dayOfWeek = "(Sa)";
                    break;
                case 7:
                    dayOfWeek = "(So)";
                    break;
                default:
                    dayOfWeek = "(ER)";
                    break;
            }
            dayOfWeek += "\t" + startDate.toString("dd.MM.yy");
            TextView localTvStartDate = (TextView) view.findViewById(R.id.startdate);
            localTvStartDate.setText(dayOfWeek);
        }
    }
}