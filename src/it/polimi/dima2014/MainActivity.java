package it.polimi.dima2014;

import it.polimi.dima2014.data.Note;

import java.util.logging.Logger;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements MainFragment.OnNoteSelectedListener, NoteFragmentView.OnNoteEditListener, NoteFragmentEdit.OnNoteEditDoneListener {

    public static final String TAG = "dima2014";

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
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Fragment noteEditFragment = new NoteFragmentEdit(note);
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
        MainFragment mainFragment = (MainFragment) manager.findFragmentByTag(MainFragment.TAG);
        mainFragment.updateNote(note);
        manager.popBackStack();
    }

    @Override
    public void onCancel() {
        FragmentManager manager = getFragmentManager();
        manager.popBackStack();
    }

}
