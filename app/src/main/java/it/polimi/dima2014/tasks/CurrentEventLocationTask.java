package it.polimi.dima2014.tasks;

import it.polimi.dima2014.MainActivity;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.CalendarContract.Events;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class CurrentEventLocationTask extends AsyncTask<Long, Void, String> {

	private CurrentEventLocationResult delegate = null;
	private ContentResolver contentResolver = null;

	@Override
	protected String doInBackground(Long... arg0) {
		String result = "";
		String current = "" + arg0[0];
		String[] projection = { Events.EVENT_LOCATION };
		String selection = Events.DTSTART + " < ? AND " + Events.DTEND + " > ?";
		String[] selectionArgs = { current, current };
		Cursor cursor = this.contentResolver.query(Events.CONTENT_URI, projection, selection, selectionArgs, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			result = cursor.getString(cursor.getColumnIndexOrThrow(Events.EVENT_LOCATION));
			Log.i(MainActivity.TAG, "Found a location: " + result);
			cursor.close();
		}
		else {
			Log.i(MainActivity.TAG, "No events found");
		}

		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		if (this.delegate != null && result != null && !result.equals("")) {
			this.delegate.processResult(result);
		}
	}

	public void setDelegate(CurrentEventLocationResult delegate) {
		this.delegate = delegate;
	}

	public void setContentResolver(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

}
