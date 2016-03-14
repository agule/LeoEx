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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
    private Direction currentDirection;
    private TextView mTextViewFrom;
    private ImageButton mButtonXchange;
    private TextView mTextViewTo;
    private ImageButton mButtonSearch;

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

    private void ScrollToNextTrain(){
        if(mListViewTimetable!=null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                mListViewTimetable.smoothScrollToPositionFromTop(mTimetable.getItemBeforeNow(currentDirection), 10);
            else    // older functions only ensures item is visible, but may be at bottom
                mListViewTimetable.smoothScrollToPosition(mTimetable.getItemBeforeNow(currentDirection));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_scroll)
            ScrollToNextTrain();
        return super.onOptionsItemSelected(item);
    }

    @Override
    // TODO: should really restore scroll position and not scroll to current time
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        ScrollToNextTrain();
    }

    private void SwapDirections(){
        mTextViewFrom.setText(currentDirection.Name());
        if(currentDirection==Direction.RomeToFCO)
            currentDirection=Direction.FCOToRome;
        else if(currentDirection==Direction.FCOToRome)
            currentDirection=Direction.RomeToFCO;
        mTextViewTo.setText(currentDirection.Name());
    }

    private void InitTimetableList(){
        if(mListViewTimetable==null)
            return;
        List<String> strListData = new ArrayList<String>(Arrays.asList(mTimetable.getTimetableStrings(currentDirection)));
        mTimetableAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.timetable_item, R.id.list_item_timetable, strListData);
        mListViewTimetable.setAdapter(mTimetableAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mTextViewFrom = (TextView)rootView.findViewById(R.id.textFrom);
        mTextViewFrom.setText(Direction.FCOToRome.Name());
        mButtonXchange = (ImageButton)rootView.findViewById(R.id.buttonXchange);
        mButtonXchange.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwapDirections();
                if (mListViewTimetable != null)
                    InitTimetableList();
            }
        });
        mTextViewTo   = (TextView)rootView.findViewById(R.id.textTo);
        mTextViewTo.setText(Direction.RomeToFCO.Name());
        currentDirection = Direction.RomeToFCO;
        mButtonSearch = (ImageButton)rootView.findViewById(R.id.buttonSearch);
        mButtonSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollToNextTrain();
            }
        });

        mListViewTimetable = (ListView) rootView.findViewById(R.id.listview_timetable);
        InitTimetableList();

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
