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
        Timetable timetable = new Timetable();
        List<String> strListData = new ArrayList<String>(Arrays.asList(timetable.getTimetableStrings()));
        mTimetableAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.timetable_item, R.id.list_item_timetable, strListData);
        listView.setAdapter(mTimetableAdapter);

        return rootView;
    }
}
