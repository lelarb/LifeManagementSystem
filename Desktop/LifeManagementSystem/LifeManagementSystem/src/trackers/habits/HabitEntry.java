package trackers.habits;

import org.bson.Document;
import org.bson.types.ObjectId;

public class HabitEntry {
    private ObjectId id;
    private String username;
    private String date;
    private boolean done;
    private String note;

    public HabitEntry(String username, String date, boolean done, String note) {
        this.username = username;
        this.date = date;
        this.done = done;
        this.note = note;
    }

    public HabitEntry(ObjectId id, String username, String date, boolean done, String note) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.done = done;
        this.note = note;
    }

    public Document toDocument() {
        return new Document("username", username)
                .append("date", date)
                .append("done", done)
                .append("note", note);
    }

    public ObjectId getId() { return id; }
    public String getUsername() { return username; }
    public String getDate() { return date; }
    public boolean isDone() { return done; }
    public String getNote() { return note; }
}

