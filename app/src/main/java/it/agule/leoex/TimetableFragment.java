package it.agule.leoex;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimetableFragment extends Fragment {

    private final String TAG = TimetableFragment.class.getSimpleName();
    private ArrayAdapter<String> mTimetableAdapter;

    public TimetableFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_timetable);

        String[] timetableArray = { // some pre-produced list items...
                "05:57 ✈Fiumicino → Roma Tiburtina (06:45) R22001 [8€]",
                "06:23 ✈Fiumicino → Roma Termini (06:55) LE3231 [14€]",
                "06:27 ✈Fiumicino → Roma Tiburtina (07:15) R22003 [8€]",
                "06:42 ✈Fiumicino → Roma Tiburtina (07:30) R22005 [8€]",
                "06:53 ✈Fiumicino → Roma Termini (07:25) LE3235 [14€]",
                "06:57 ✈Fiumicino → Roma Tiburtina (07:45) R22007 [8€]",
                "07:12 ✈Fiumicino → Roma Tiburtina (08:00) R22009 [8€]",
                "07:23 ✈Fiumicino → Roma Termini (07:55) LE3239 [14€]",
                "07:27 ✈Fiumicino → Roma Tiburtina (08:15) R22011 [8€]",
                "07:42 ✈Fiumicino → Roma Tiburtina (08:30) R22013 [8€]",
                "07:53 ✈Fiumicino → Roma Termini (07:85) LE3243 [14€]",
                "07:57 ✈Fiumicino → Roma Tiburtina (08:45) R22015 [8€]",
        };

        List<String> strListData = new ArrayList<String>(Arrays.asList(timetableArray));
        mTimetableAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.timetable_item, R.id.list_item_timetable, strListData);
        listView.setAdapter(mTimetableAdapter);

        return rootView;
    }
}
