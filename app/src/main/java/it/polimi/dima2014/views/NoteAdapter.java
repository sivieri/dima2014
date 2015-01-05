package it.polimi.dima2014.views;

import android.database.Cursor;
import android.view.ViewGroup;

public class NoteAdapter extends CursorRecyclerAdapter<NoteHolder> {

    public NoteAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public void onBindViewHolderCursor(NoteHolder holder, Cursor cursor) {

    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }
}
