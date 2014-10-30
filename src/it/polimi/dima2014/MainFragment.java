package it.polimi.dima2014;

import it.polimi.dima2014.data.Note;
import it.polimi.dima2014.data.Notes;

import java.util.List;
import java.util.logging.Logger;

import org.joda.time.DateTime;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainFragment extends Fragment implements OnItemClickListener {
	public static final String TAG = "MainFragment";

	private Notes notes;
	private OnNoteSelectedListener noteListener;

	public interface OnNoteSelectedListener {
		public void onNoteSelected(Note note);
	}

	public MainFragment() {
		this.notes = new Notes();
		this.notes.add(new Note(0, new DateTime(2014, 10, 23, 12, 56, 21), "Note 1", "First test"));
		this.notes.add(new Note(1, new DateTime(2014, 10, 24, 8, 32, 23), "Note 2", "Second test"));
		this.notes.add(new Note(2, new DateTime(2014, 10, 24, 15, 26, 34), "Note 3", "Third test"));
		this.notes.add(new Note(3, new DateTime(2014, 10, 25, 11, 11, 45), "Note 4", "Fourth test"));
	}

	public void updateNote(Note note) {
		this.notes.add(note);
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
		List<String> titles = this.notes.getTitles();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titles);
		ListView listView = (ListView) rootView.findViewById(R.id.notesList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Logger.getLogger(MainActivity.TAG).info("Clicked on " + id);
		this.noteListener.onNoteSelected(this.notes.get(id));
	}
}
