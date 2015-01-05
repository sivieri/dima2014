package it.polimi.dima2014.views;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.logging.Logger;

import it.polimi.dima2014.MainActivity;
import it.polimi.dima2014.R;
import it.polimi.dima2014.data.Note;
import it.polimi.dima2014.data.NotesContentProvider;
import it.polimi.dima2014.data.NotesOpenHelper;

public class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView labelView;
    private long id;
    private String label;
    private OnNoteListSelectedListener listener;
    private ContentResolver resolver;
    private DateTimeFormatter formatter;

    public interface OnNoteListSelectedListener {
        public void onNoteListSelected(Note note);
    }

    public NoteHolder(ContentResolver resolver, OnNoteListSelectedListener listener, View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.resolver = resolver;
        this.listener = listener;
        labelView = (TextView) itemView.findViewById(R.id.note_item_label);
        this.formatter = new DateTimeFormatterBuilder().appendYear(4, 4).appendLiteral("-").appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).appendLiteral(" ").appendHourOfDay(2).appendLiteral(":").appendMinuteOfHour(2).appendLiteral(":").appendSecondOfMinute(2).toFormatter();
    }

    public void bindNote(long id, String label) {
        this.id = id;
        this.label = label;
        labelView.setText(label);
    }

    @Override
    public void onClick(View v) {
        Logger.getLogger(MainActivity.TAG).info("Clicked on " + id);
        Uri uri = Uri.parse(NotesContentProvider.CONTENT_URI + "/" + id);
        String[] projection = { NotesOpenHelper.ID, NotesOpenHelper.KEY, NotesOpenHelper.VALUE, NotesOpenHelper.TIMESTAMP, NotesOpenHelper.LAT, NotesOpenHelper.LNG };
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Long noteId = cursor.getLong(cursor.getColumnIndexOrThrow(NotesOpenHelper.ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesOpenHelper.KEY));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(NotesOpenHelper.VALUE));
            String ts = cursor.getString(cursor.getColumnIndexOrThrow(NotesOpenHelper.TIMESTAMP));
            Double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(NotesOpenHelper.LAT));
            Double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(NotesOpenHelper.LNG));
            cursor.close();
            listener.onNoteListSelected(new Note(noteId, this.formatter.parseDateTime(ts), title, content, lat, lng));
        }
    }
}
