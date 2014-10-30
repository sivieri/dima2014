package it.polimi.dima2014;

import it.polimi.dima2014.data.Note;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NoteFragmentEdit extends Fragment {
	private Note note;

	public NoteFragmentEdit(Note note) {
		this.note = note;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_note_edit, container, false);
		EditText noteTitle = (EditText) rootView.findViewById(R.id.noteTitleEdit);
		EditText noteContent = (EditText) rootView.findViewById(R.id.noteContentEdit);
		noteTitle.setText(this.note.getTitle());
		noteContent.setText(this.note.getContent());
		Button cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager manager = getActivity().getFragmentManager();
				manager.popBackStack();
			}
			
		});
		Button saveButton = (Button) rootView.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				FragmentManager manager = getActivity().getFragmentManager();
				manager.popBackStack();
			}
			
		});

		return rootView;
	}
}
