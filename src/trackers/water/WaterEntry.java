package trackers.water;

import org.bson.Document;
import org.bson.types.ObjectId;

public class WaterEntry {
    private ObjectId id;
    private String username;
    private String date;
    private int amount;

    public WaterEntry(String username, String date, int amount) {
        this.username = username;
        this.date = date;
        this.amount = amount;
    }

    public WaterEntry(ObjectId id, String username, String date, int amount) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.amount = amount;
    }

    public Document toDocument() {
        return new Document("username", username)
                .append("date", date)
                .append("amount", amount);
    }

    public ObjectId getId() { return id; }
    public String getUsername() { return username; }
    public String getDate() { return date; }
    public int getAmount() { return amount; }
}
