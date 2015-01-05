package it.polimi.dima2014;

import it.polimi.dima2014.data.Note;
import it.polimi.dima2014.data.NotesContentProvider;
import it.polimi.dima2014.data.NotesOpenHelper;
import it.polimi.dima2014.views.DividerItemDecoration;
import it.polimi.dima2014.views.NoteAdapter;
import it.polimi.dima2014.views.NoteHolder;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainFragment extends Fragment implements NoteHolder.OnNoteListSelectedListener, LoaderCallbacks<Cursor> {
    public static final String TAG = "MainFragment";

    private OnNoteSelectedListener noteListener;
    private NoteAdapter mAdapter;
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
        mAdapter = new NoteAdapter(getActivity().getContentResolver(), this, null);
        RecyclerView listView = (RecyclerView) rootView.findViewById(R.id.notesList);
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(mAdapter);
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
    public void onNoteListSelected(Note note) {
        this.noteListener.onNoteSelected(note);
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

}
