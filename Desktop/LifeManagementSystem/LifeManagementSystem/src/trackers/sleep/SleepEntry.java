package trackers.sleep;

import org.bson.Document;
import org.bson.types.ObjectId;

public class SleepEntry {
    private ObjectId id;
    private String username;
    private String date;
    private String sleepTime;
    private String wakeTime;
    private double hours;
    private int quality;

    public SleepEntry(String username, String date, String sleepTime, String wakeTime, double hours, int quality) {
        this.username = username;
        this.date = date;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.hours = hours;
        this.quality = quality;
    }

    public SleepEntry(ObjectId id, String username, String date, String sleepTime, String wakeTime, double hours, int quality) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.hours = hours;
        this.quality = quality;
    }

    public Document toDocument() {
        return new Document("username", username)
                .append("date", date)
                .append("sleepTime", sleepTime)
                .append("wakeTime", wakeTime)
                .append("hours", hours)
                .append("quality", quality);
    }

    public ObjectId getId() { return id; }
    public String getUsername() { return username; }
    public String getDate() { return date; }
    public String getSleepTime() { return sleepTime; }
    public String getWakeTime() { return wakeTime; }
    public double getHours() { return hours; }
    public int getQuality() { return quality; }
}
