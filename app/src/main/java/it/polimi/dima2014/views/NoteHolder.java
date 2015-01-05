package it.polimi.dima2014.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import it.polimi.dima2014.R;
import it.polimi.dima2014.data.Note;

public class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView labelView;
    private Note note;

    public NoteHolder(View itemView) {
        super(itemView);
        labelView = (TextView) itemView.findViewById(R.id.wiki_item_label);
    }

    public void bindNote(Note note) {
        this.note = note;
        labelView.setText(note.getTitle());
    }

    @Override
    public void onClick(View v) {

    }
}
