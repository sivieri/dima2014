package it.polimi.dima2014;

import it.polimi.dima2014.data.Note;
import it.polimi.dima2014.data.NotesContentProvider;
import it.polimi.dima2014.data.NotesOpenHelper;
import it.polimi.dima2014.tasks.CurrentEventLocationResult;
import it.polimi.dima2014.tasks.CurrentEventLocationTask;

import java.util.Calendar;
import java.util.TimeZone;

import org.joda.time.DateTime;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class NoteFragmentEdit extends Fragment implements CurrentEventLocationResult, GoogleApiClient.ConnectionCallbacks {
    public static final String TAG = "NoteFragmentEdit";

    private Note note = null;
    private OnNoteEditDoneListener editDoneListener;

    public interface OnNoteEditDoneListener {
        public void onSave(Note note);

        public void onCancel(long id);
    }

    public NoteFragmentEdit() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.editDoneListener = (OnNoteEditDoneListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnNoteEditDoneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note_edit, container, false);
        EditText noteTitle = (EditText) rootView.findViewById(R.id.noteTitleEdit);
        EditText noteContent = (EditText) rootView.findViewById(R.id.noteContentEdit);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("note")) {
            note = (Note) bundle.getSerializable("note");
            noteTitle.setText(this.note.getTitle());
            noteContent.setText(this.note.getContent());
        }
        Button cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (NoteFragmentEdit.this.note.isFirst()) {
                    NoteFragmentEdit.this.editDoneListener.onCancel(NoteFragmentEdit.this.note.getId());
                }
                else {
                    NoteFragmentEdit.this.editDoneListener.onCancel(-1);
                }
            }

        });
        Button saveButton = (Button) rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                NoteFragmentEdit.this.note.setFirst(false);
                EditText noteTitle = (EditText) v.getRootView().findViewById(R.id.noteTitleEdit);
                EditText noteContent = (EditText) v.getRootView().findViewById(R.id.noteContentEdit);
                ContentValues values = new ContentValues();
                values.put(NotesOpenHelper.KEY, noteTitle.getText().toString());
                values.put(NotesOpenHelper.VALUE, noteContent.getText().toString());
                values.put(NotesOpenHelper.LAT, NoteFragmentEdit.this.note.getLat());
                values.put(NotesOpenHelper.LNG, NoteFragmentEdit.this.note.getLng());
                Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + NoteFragmentEdit.this.note.getId());
                getActivity().getContentResolver().update(uri, values, null, null);
                Note note = new Note(NoteFragmentEdit.this.note.getId(), new DateTime(), noteTitle.getText().toString(), noteContent.getText().toString(), NoteFragmentEdit.this.note.getLat(), NoteFragmentEdit.this.note.getLng());
                NoteFragmentEdit.this.editDoneListener.onSave(note);
            }

        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        EditText title = (EditText) getActivity().findViewById(R.id.noteTitleEdit);
        if (title.getText().toString().equals("")) {
            Log.i(MainActivity.TAG, "Launching the calendar task...");
            Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            long current = utc.getTimeInMillis();
            CurrentEventLocationTask task = new CurrentEventLocationTask();
            task.setDelegate(this);
            task.setContentResolver(getActivity().getContentResolver());
            task.execute(current);
        }
        if (this.note.getLat() == 0 && this.note.getLng() == 0) {
            Log.d(MainActivity.TAG, "Location needed, trying to connect...");
            MainActivity a = (MainActivity) getActivity();
            a.servicesConnected();
        }
    }

    @Override
    public void onPause() {
        if (NoteFragmentEdit.this.note.isFirst()) {
            Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + this.note.getId());
            getActivity().getContentResolver().delete(uri, null, null);
        }
        MainActivity activity = (MainActivity) getActivity();
        activity.getApiClient().disconnect();
        super.onPause();
    }

    @Override
    public void processResult(String result) {
        EditText title = (EditText) getActivity().findViewById(R.id.noteTitleEdit);
        title.setHint("Note @ " + result);
    }

    @Override
    public void onConnected(Bundle arg0) {
        Log.i(MainActivity.TAG, "Location connected");
        MainActivity activity = (MainActivity) getActivity();
        Location l = LocationServices.FusedLocationApi.getLastLocation(activity.getApiClient());
        Log.d(MainActivity.TAG, l.toString());
        this.note.setLat(l.getLatitude());
        this.note.setLng(l.getLongitude());
        activity.getApiClient().disconnect();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(MainActivity.TAG, "Location connection suspended: " + cause);
    }
}
