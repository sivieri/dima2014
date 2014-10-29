package it.polimi.dima2014.data;

import org.joda.time.DateTime;

public class Note {
    private int id;
    private DateTime timestamp;
    private String title;
    private String content;

    public Note(int id, DateTime timestamp, String title, String content) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return this.id;
    }

    public DateTime getTimestamp() {
        return this.timestamp;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Note other = (Note) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Note [id=" + this.id + ", timestamp=" + this.timestamp + ", title=" + this.title + ", content=" + this.content + "]";
    }

}
