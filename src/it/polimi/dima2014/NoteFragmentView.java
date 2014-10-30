package it.polimi.dima2014;

import it.polimi.dima2014.data.Note;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class NoteFragmentView extends Fragment {
	private Note note;
	private DateTimeFormatter formatter;
	private OnNoteEditListener editListener;
	
	public interface OnNoteEditListener {
        public void onNoteEdit(Note note);
    }

	public NoteFragmentView(Note note) {
		this.note = note;
		this.formatter = new DateTimeFormatterBuilder().appendDayOfMonth(2).appendLiteral("/").appendMonthOfYear(2).appendLiteral("/").appendYear(4, 4).appendLiteral(" ").appendHourOfDay(2)
		        .appendLiteral(":").appendMinuteOfHour(2).toFormatter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_note_view, container, false);
		TextView noteTitle = (TextView) rootView.findViewById(R.id.noteTitle);
		TextView noteTs = (TextView) rootView.findViewById(R.id.noteTs);
		TextView noteContent = (TextView) rootView.findViewById(R.id.noteContent);
		noteTitle.setText(this.note.getTitle());
		noteTs.setText(this.formatter.print(this.note.getTimestamp()));
		noteContent.setText(this.note.getContent());
		Button editButton = (Button) rootView.findViewById(R.id.editButton);
		editButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				editListener.onNoteEdit(note);
			}
			
		});

		return rootView;
	}
}
