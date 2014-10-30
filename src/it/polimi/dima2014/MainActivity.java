package it.polimi.dima2014;

import it.polimi.dima2014.data.Note;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements MainFragment.OnNoteSelectedListener, NoteFragmentView.OnNoteEditListener {

    public static final String TAG = "dima2014";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new MainFragment()).commit();
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
		Fragment noteFragment = new NoteFragmentView(note);
		FragmentManager manager = getFragmentManager();
		FragmentTransaction trans = manager.beginTransaction();
		trans.replace(R.id.container, noteFragment);
		trans.addToBackStack(null);
		trans.commit();
	}

	@Override
	public void onNoteEdit(Note note) {
		Fragment noteEditFragment = new NoteFragmentEdit(note);
		FragmentManager manager = getFragmentManager();
		FragmentTransaction trans = manager.beginTransaction();
		trans.replace(R.id.container, noteEditFragment);
		trans.addToBackStack(null);
		trans.commit();
	}

}
