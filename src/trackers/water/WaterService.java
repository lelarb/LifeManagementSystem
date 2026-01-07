package trackers.water;

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

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Sorts.ascending;

public class WaterService {

    private final MongoCollection<Document> col;

    public WaterService() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        col = db.getCollection("water_entries");
    }

    public void add(WaterEntry e) {
        col.insertOne(e.toDocument());
    }

    public ArrayList<WaterEntry> getAllForUser(String username) {
        ArrayList<WaterEntry> list = new ArrayList<>();

        MongoCursor<Document> cursor = col.find(eq("username", username))
                .sort(ascending("date"))
                .iterator();

        while (cursor.hasNext()) {
            Document d = cursor.next();
            list.add(new WaterEntry(
                    d.getObjectId("_id"),
                    d.getString("username"),
                    d.getString("date"),
                    d.getInteger("amount", 0)
            ));
        }
        return list;
    }

    public void update(ObjectId id, String username, String date, int amount) {
        col.updateOne(
                and(eq("_id", id), eq("username", username)),
                new Document("$set", new Document("date", date).append("amount", amount))
        );
    }

    public void delete(ObjectId id, String username) {
        col.deleteOne(and(eq("_id", id), eq("username", username)));
    }

    public double getAverageAmount(String username) {
        ArrayList<WaterEntry> list = getAllForUser(username);
        if (list.isEmpty()) return 0;

        long sum = 0;
        for (WaterEntry e : list) sum += e.getAmount();
        return (double) sum / list.size();
    }

    public int getStreak(String username) {
        ArrayList<WaterEntry> list = getAllForUser(username);
        if (list.isEmpty()) return 0;

        Set<String> dates = new HashSet<>();
        for (WaterEntry e : list) {
            dates.add(e.getDate());
        }

        int streak = 0;
        LocalDate day = LocalDate.now();

        while (dates.contains(day.toString())) {
            streak++;
            day = day.minusDays(1);
        }
        return streak;
    }
}

