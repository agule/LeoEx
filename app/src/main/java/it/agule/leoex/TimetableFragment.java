package it.agule.leoex;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimetableFragment extends Fragment {

    private final String TAG = TimetableFragment.class.getSimpleName();
    private ArrayAdapter<String> mTimetableAdapter;
    private ListView mListViewTimetable;

    Timetable mTimetable = new Timetable();

    public TimetableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_timetable, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_scroll) {
            if(mListViewTimetable!=null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    mListViewTimetable.smoothScrollToPositionFromTop(mTimetable.getItemBeforeNow(), 10);
                else    // older functions only ensures item is visible, but may be at bottom
                    mListViewTimetable.smoothScrollToPosition(mTimetable.getItemBeforeNow());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    // TODO: to be moved to another message, which would scroll ListView on opening
    public void onResume() {
        super.onResume();
        if(mListViewTimetable!=null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                mListViewTimetable.smoothScrollToPositionFromTop(mTimetable.getItemBeforeNow(), 10);
            else    // older functions only ensures item is visible, but may be at bottom
                mListViewTimetable.smoothScrollToPosition(mTimetable.getItemBeforeNow());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListViewTimetable = (ListView) rootView.findViewById(R.id.listview_timetable);
        List<String> strListData = new ArrayList<String>(Arrays.asList(mTimetable.getTimetableStrings()));
        mTimetableAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.timetable_item, R.id.list_item_timetable, strListData);
        mListViewTimetable.setAdapter(mTimetableAdapter);

        mListViewTimetable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String itemText = mTimetableAdapter.getItem(position);
//                Toast.makeText(getActivity(), itemText, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                                            .putExtra(Intent.EXTRA_TEXT, itemText);
                startActivity(intent);
            }
        });


        return rootView;
    }
}
