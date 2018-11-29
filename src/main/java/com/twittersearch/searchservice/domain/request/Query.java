package com.twittersearch.searchservice.domain.request;

public final class Query implements java.io.Serializable {
    public String query = null;
    public int count = 100;
    public String geocode = null;
    public long until = -1;
    public long max_id = -1;
    public long since_id = -1;

    public Query() {
    }

    public Query(String query, int count, String geocode, long until, long max_id, long since_id) {
        this.query = query;
        this.count = count;
        this.geocode = geocode;
        this.until = until;
        this.max_id = max_id;
        this.since_id = since_id;
    }

    @Override
    public String toString() {
        return "count=" + count + ((query != null)? "&q=" + query : "")  + ((geocode != null)? "&geocode=" + geocode : "")  +
                ((until != -1)? "&until=" + until : "") + ((max_id != -1)? "&max_id=" + max_id : "") + ((since_id != -1)? "&since_id=" + since_id: "");
    }
}

