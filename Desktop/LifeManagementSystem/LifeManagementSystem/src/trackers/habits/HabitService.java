package trackers.habits;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import db.MongoDBConnection;
import org.bson.Document;

import java.util.ArrayList;

public class HabitService {
    private final MongoCollection<Document> col;

    public HabitService() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        col = db.getCollection("habits");
    }

    public void save(String username, String isoDate, boolean done, String note) {
        Document set = new Document("username", username)
                .append("date", isoDate)
                .append("done", done)
                .append("note", note);

        col.updateOne(
                Filters.and(
                        Filters.eq("username", username),
                        Filters.eq("date", isoDate)
                ),
                new Document("$set", set),
                new UpdateOptions().upsert(true)
        );
    }

    public ArrayList<HabitEntry> getAllForUser(String username) {
        ArrayList<HabitEntry> list = new ArrayList<>();
        for (Document d : col.find(Filters.eq("username", username)).sort(Sorts.descending("date"))) {
            list.add(new HabitEntry(
                    d.getObjectId("_id"),
                    d.getString("username"),
                    d.getString("date"),
                    d.getBoolean("done", false),
                    d.getString("note")
            ));
        }
        return list;
    }
}
