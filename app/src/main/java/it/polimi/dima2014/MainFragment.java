package it.polimi.dima2014;

import it.polimi.dima2014.data.Note;
import it.polimi.dima2014.data.NotesContentProvider;
import it.polimi.dima2014.data.NotesOpenHelper;

import java.util.logging.Logger;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainFragment extends Fragment implements OnItemClickListener, LoaderCallbacks<Cursor> {
    public static final String TAG = "MainFragment";

    private OnNoteSelectedListener noteListener;
    private SimpleCursorAdapter mAdapter;
    private DateTimeFormatter formatter;

    public interface OnNoteSelectedListener {
        public void onNoteSelected(Note note);

        public void onNoteNew();
    }

    public MainFragment() {
        this.formatter = new DateTimeFormatterBuilder().appendYear(4, 4).appendLiteral("-").appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).appendLiteral(" ").appendHourOfDay(2).appendLiteral(":").appendMinuteOfHour(2).appendLiteral(":").appendSecondOfMinute(2).toFormatter();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.noteListener = (OnNoteSelectedListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnNoteSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] fromColumns = { NotesOpenHelper.KEY };
        int[] toViews = { android.R.id.text1 };
        this.mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, fromColumns, toViews, 0);
        ListView listView = (ListView) rootView.findViewById(R.id.notesList);
        listView.setAdapter(this.mAdapter);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);
        getActivity().getLoaderManager().initLoader(0, null, this);
        Button newButton = (Button) rootView.findViewById(R.id.newNote);
        newButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MainFragment.this.noteListener.onNoteNew();
            }

        });
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Logger.getLogger(MainActivity.TAG).info("Clicked on " + id);
        Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + id);
        String[] projection = { NotesOpenHelper.ID, NotesOpenHelper.KEY, NotesOpenHelper.VALUE, NotesOpenHelper.TIMESTAMP, NotesOpenHelper.LAT, NotesOpenHelper.LNG };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Long noteId = cursor.getLong(cursor.getColumnIndexOrThrow(NotesOpenHelper.ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesOpenHelper.KEY));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(NotesOpenHelper.VALUE));
            String ts = cursor.getString(cursor.getColumnIndexOrThrow(NotesOpenHelper.TIMESTAMP));
            Double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(NotesOpenHelper.LAT));
            Double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(NotesOpenHelper.LNG));
            cursor.close();
            this.noteListener.onNoteSelected(new Note(noteId, this.formatter.parseDateTime(ts), title, content, lat, lng));
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        String[] projection = { NotesOpenHelper.ID, NotesOpenHelper.KEY };
        return new CursorLoader(getActivity(), NotesContentProvider.CONTENT_URI, projection, null, null, NotesOpenHelper.ID + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        this.mAdapter.swapCursor(arg1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        this.mAdapter.swapCursor(null);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.notesList) {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater menuInflater = getActivity().getMenuInflater();
            menuInflater.inflate(R.menu.context, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_delete:
                Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + info.id);
                getActivity().getContentResolver().delete(uri, null, null);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
