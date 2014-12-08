package it.polimi.dima2014.data;

import org.joda.time.DateTime;

public class Note {
    private long id;
    private DateTime timestamp;
    private String title;
    private String content;
    private double lat = 0.0;
    private double lng = 0.0;
    private boolean first = false;

    public Note(long id, DateTime timestamp, String title, String content) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.content = content;
    }

    public Note(long id, DateTime timestamp, String title, String content, boolean first) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.content = content;
        this.first = first;
    }

    public Note(long id, DateTime timestamp, String title, String content, double lat, double lng) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.content = content;
        this.lat = lat;
        this.lng = lng;
    }

    public Note(long id, DateTime timestamp, String title, String content, double lat, double lng, boolean first) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.content = content;
        this.lat = lat;
        this.lng = lng;
        this.first = first;
    }

    public long getId() {
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

    public double getLat() {
        return this.lat;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isFirst() {
        return this.first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.id ^ (this.id >>> 32));
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
        if (!(obj instanceof Note)) {
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
        return "Note [id=" + this.id + ", timestamp=" + this.timestamp + ", title=" + this.title + ", content=" + this.content + ", lat=" + this.lat + ", lng=" + this.lng + ", first=" + this.first + "]";
    }

}
