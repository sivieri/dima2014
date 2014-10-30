package it.polimi.dima2014.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Notes {

	private Map<Long, Note> notes;

	public void add(Note note) {
		this.notes.put(note.getId(), note);
	}

	public List<String> getTitles() {
		List<String> res = new ArrayList<String>();
		for (long id : this.notes.keySet()) {
			res.add(this.notes.get(id).getTitle());
		}
		return res;
	}

	public Note get(long id) {
		return this.notes.get(id);
	}
}
