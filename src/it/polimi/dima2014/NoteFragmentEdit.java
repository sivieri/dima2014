package it.polimi.dima2014;

import it.polimi.dima2014.data.Note;
import it.polimi.dima2014.data.NotesContentProvider;
import it.polimi.dima2014.data.NotesOpenHelper;

import org.joda.time.DateTime;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NoteFragmentEdit extends Fragment {
    public static final String TAG = "NoteFragmentEdit";

    private Note note;
    private OnNoteEditDoneListener editDoneListener;

    public interface OnNoteEditDoneListener {
        public void onSave(Note note);

        public void onCancel();
    }

    public NoteFragmentEdit(Note note) {
        this.note = note;
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
        noteTitle.setText(this.note.getTitle());
        noteContent.setText(this.note.getContent());
        Button cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                NoteFragmentEdit.this.editDoneListener.onCancel();
            }

        });
        Button saveButton = (Button) rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText noteTitle = (EditText) v.getRootView().findViewById(R.id.noteTitleEdit);
                EditText noteContent = (EditText) v.getRootView().findViewById(R.id.noteContentEdit);
                ContentValues values = new ContentValues();
                values.put(NotesOpenHelper.KEY, noteTitle.getText().toString());
                values.put(NotesOpenHelper.VALUE, noteContent.getText().toString());
                Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + NoteFragmentEdit.this.note.getId());
                getActivity().getContentResolver().update(uri, values, null, null);
                Note note = new Note(NoteFragmentEdit.this.note.getId(), new DateTime(), noteTitle.getText().toString(), noteContent.getText().toString());
                NoteFragmentEdit.this.editDoneListener.onSave(note);
            }

        });

        return rootView;
    }
}
