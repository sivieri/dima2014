package it.polimi.dima2014.views;

import android.content.ContentResolver;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.polimi.dima2014.R;
import it.polimi.dima2014.data.NotesOpenHelper;

public class NoteAdapter extends CursorRecyclerAdapter<NoteHolder> {

    private NoteHolder.OnNoteListSelectedListener listener;
    private ContentResolver resolver;

    public NoteAdapter(ContentResolver resolver, NoteHolder.OnNoteListSelectedListener listener, Cursor cursor) {
        super(cursor);
        this.resolver = resolver;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolderCursor(NoteHolder holder, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(NotesOpenHelper.ID));
        String label = cursor.getString(cursor.getColumnIndexOrThrow(NotesOpenHelper.KEY));
        holder.bindNote(id, label);
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_item, viewGroup, false);
        return new NoteHolder(resolver, listener, view);
    }
}
