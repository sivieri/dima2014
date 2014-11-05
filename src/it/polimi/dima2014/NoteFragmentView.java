package it.polimi.dima2014;

import it.polimi.dima2014.data.Note;

import java.util.logging.Logger;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class NoteFragmentView extends Fragment {
    public static final String TAG = "NoteFragmentView";

    private Note note = null;
    private DateTimeFormatter formatter;
    private OnNoteEditListener editListener;

    public interface OnNoteEditListener {
        public void onNoteEdit(Note note);
    }

    public NoteFragmentView() {
        this.formatter = new DateTimeFormatterBuilder().appendDayOfMonth(2).appendLiteral("/").appendMonthOfYear(2).appendLiteral("/").appendYear(4, 4).appendLiteral(" ").appendHourOfDay(2).appendLiteral(":").appendMinuteOfHour(2).toFormatter();
    }

    public NoteFragmentView(Note note) {
        this.note = note;
        this.formatter = new DateTimeFormatterBuilder().appendDayOfMonth(2).appendLiteral("/").appendMonthOfYear(2).appendLiteral("/").appendYear(4, 4).appendLiteral(" ").appendHourOfDay(2).appendLiteral(":").appendMinuteOfHour(2).toFormatter();
    }

    public void updateNote(Note note) {
        this.note = note;
    }

    public void updateNoteAndView(Note note) {
        this.note = note;
        updateNoteView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.editListener = (OnNoteEditListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnNoteEditListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note_view, container, false);
        Button editButton = (Button) rootView.findViewById(R.id.editButton);
        editButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                NoteFragmentView.this.editListener.onNoteEdit(NoteFragmentView.this.note);
            }

        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNoteView();
    }

    private void updateNoteView() {
        if (this.note != null) {
            Logger.getLogger(MainActivity.TAG).info(this.note.toString());
            TextView noteTitle = (TextView) getView().findViewById(R.id.noteTitle);
            TextView noteTs = (TextView) getView().findViewById(R.id.noteTs);
            TextView noteContent = (TextView) getView().findViewById(R.id.noteContent);
            noteTitle.setText(this.note.getTitle());
            noteTs.setText(this.formatter.print(this.note.getTimestamp()));
            noteContent.setText(this.note.getContent());
        }
    }

}
