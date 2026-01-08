package trackers.sleep;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import db.MongoDBConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;

public class SleepService {
    private final MongoCollection<Document> col;

    public SleepService() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        col = db.getCollection("sleep_entries");
    }

    public void add(SleepEntry e) {
        col.insertOne(e.toDocument());
    }

    public ArrayList<SleepEntry> getAllForUser(String username) {
        ArrayList<SleepEntry> list = new ArrayList<>();
        MongoCursor<Document> cursor = col.find(eq("username", username))
                .sort(ascending("date"))
                .iterator();

        while (cursor.hasNext()) {
            Document d = cursor.next();
            list.add(new SleepEntry(
                    d.getObjectId("_id"),
                    d.getString("username"),
                    d.getString("date"),
                    d.getString("sleepTime"),
                    d.getString("wakeTime"),
                    d.getDouble("hours"),
                    d.getInteger("quality", 0)
            ));
        }
        return list;
    }

    public void update(ObjectId id, String username, String date, String sleepTime, String wakeTime, double hours, int quality) {
        col.updateOne(
                and(eq("_id", id), eq("username", username)),
                new Document("$set",
                        new Document("date", date)
                                .append("sleepTime", sleepTime)
                                .append("wakeTime", wakeTime)
                                .append("hours", hours)
                                .append("quality", quality)
                )
        );
    }

    public void delete(ObjectId id, String username) {
        col.deleteOne(and(eq("_id", id), eq("username", username)));
    }

    public double getAverageHours(String username) {
        ArrayList<SleepEntry> list = getAllForUser(username);
        if (list.isEmpty()) return 0;
        double sum = 0;
        for (SleepEntry e : list) sum += e.getHours();
        return sum / list.size();
    }

    public double getAverageQuality(String username) {
        ArrayList<SleepEntry> list = getAllForUser(username);
        if (list.isEmpty()) return 0;
        double sum = 0;
        for (SleepEntry e : list) sum += e.getQuality();
        return sum / list.size();
    }

    public int getStreak(String username) {
        ArrayList<SleepEntry> list = getAllForUser(username);
        if (list.isEmpty()) return 0;

        Set<String> dates = new HashSet<>();
        for (SleepEntry e : list) dates.add(e.getDate());

        int streak = 0;
        LocalDate day = LocalDate.now();
        while (dates.contains(day.toString())) {
            streak++;
            day = day.minusDays(1);
        }
        return streak;
    }
}
