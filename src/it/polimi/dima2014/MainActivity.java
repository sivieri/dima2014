package it.polimi.dima2014;

import it.polimi.dima2014.data.Note;
import it.polimi.dima2014.data.NotesContentProvider;
import it.polimi.dima2014.data.NotesOpenHelper;

import java.util.logging.Logger;

import org.joda.time.DateTime;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity implements MainFragment.OnNoteSelectedListener, NoteFragmentView.OnNoteEditListener, NoteFragmentEdit.OnNoteEditDoneListener {

    public static final String TAG = "dima2014";

    private static final int SHARED_TITLE_LIMIT = 15;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;

        public ErrorDialogFragment() {
            super();
            this.mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            this.mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return this.mDialog;
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        Logger.getLogger(MainActivity.TAG).info(fragment.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String screenType = getString(R.string.screen_type);
        Logger.getLogger(MainActivity.TAG).info("Device type: " + screenType);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentTransaction trans = getFragmentManager().beginTransaction();
            if (screenType.equals("tablet")) {
                trans.add(R.id.listfragment, new MainFragment(), MainFragment.TAG);
                trans.add(R.id.notefragmentview, new NoteFragmentView(), NoteFragmentView.TAG);
            }
            else {
                trans.add(R.id.notefragmentview, new MainFragment(), MainFragment.TAG);
            }
            trans.commit();
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();
            Bundle extras = intent.getExtras();
            if (extras != null && action != null && action.equals(Intent.ACTION_SEND) && type != null && type.startsWith("text")) {
                String sharedText = extras.getString(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    String sharedTitle = sharedText.substring(0, sharedText.length() >= SHARED_TITLE_LIMIT ? SHARED_TITLE_LIMIT : sharedText.length()).concat("...");
                    ContentValues values = new ContentValues();
                    values.put(NotesOpenHelper.KEY, sharedTitle);
                    values.put(NotesOpenHelper.VALUE, sharedText);
                    Uri uri = getContentResolver().insert(NotesContentProvider.CONTENT_URI, values);
                    String uriString = uri.toString();
                    long id = Long.parseLong(uriString.substring(uriString.lastIndexOf('/') + 1));
                    Note note = new Note(id, new DateTime(), sharedTitle, sharedText);
                    onNoteSelected(note);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.wiki_search) {
            Intent i = new Intent(this, WikiActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        servicesConnected();
                        break;
                }
        }
    }

    @Override
    public void onNoteSelected(Note note) {
        FragmentManager manager = getFragmentManager();
        String screenType = getString(R.string.screen_type);
        if (screenType.equals("phone")) {
            Fragment noteFragment = new NoteFragmentView(note);
            FragmentTransaction trans = manager.beginTransaction();
            trans.replace(R.id.notefragmentview, noteFragment, NoteFragmentView.TAG);
            trans.addToBackStack(null);
            trans.commit();
        }
        else {
            NoteFragmentView noteView = (NoteFragmentView) manager.findFragmentByTag(NoteFragmentView.TAG);
            noteView.updateNoteAndView(note);
        }
    }

    @Override
    public void onNoteEdit(Note note) {
        NoteFragmentEdit noteEditFragment = new NoteFragmentEdit(note);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.notefragmentview, noteEditFragment, NoteFragmentEdit.TAG);
        trans.addToBackStack(null);
        trans.commit();
    }

    @Override
    public void onSave(Note note) {
        FragmentManager manager = getFragmentManager();
        NoteFragmentView noteView = (NoteFragmentView) manager.findFragmentByTag(NoteFragmentView.TAG);
        noteView.updateNote(note);
        manager.popBackStack();
    }

    @Override
    public void onCancel(long id) {
        FragmentManager manager = getFragmentManager();
        if (id != -1) {
            Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + id);
            getContentResolver().delete(uri, null, null);
            manager.popBackStack();
        }
        manager.popBackStack();
    }

    @Override
    public void onNoteNew() {
        ContentValues values = new ContentValues();
        values.put(NotesOpenHelper.KEY, "");
        values.put(NotesOpenHelper.VALUE, "");
        Uri uri = getContentResolver().insert(NotesContentProvider.CONTENT_URI, values);
        String uriString = uri.toString();
        long id = Long.parseLong(uriString.substring(uriString.lastIndexOf('/') + 1));
        Note note = new Note(id, new DateTime(), "", "", true);
        onNoteSelected(note);
    }

    public boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        }
        else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getFragmentManager(), "Location Updates");
            }
            return false;
        }
    }
}
