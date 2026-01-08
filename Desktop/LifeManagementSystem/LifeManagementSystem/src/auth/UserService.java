package auth;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import db.MongoDBConnection;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.result.DeleteResult;


public class UserService {
    private final MongoCollection<Document> users;

    public UserService(){
        MongoDatabase db = MongoDBConnection.getDatabase();
        users = db.getCollection("users");
    }

    public boolean register(String username, String password){
        Document exists = users.find(new Document("username", username)).first();
        if(exists != null) return false;

        users.insertOne(new Document("username", username)
                .append("password", password)
                .append("fullName", "")
                .append("email", "")
                .append("theme", "Default")
        );

        return true;
    }

    public boolean login(String username, String password){
        Document found = users.find(new Document("username", username).append("password", password)).first();
        return found != null;
    }

    public Document getUserByUsername(String username){
        return users.find(Filters.eq("username", username)).first();
    }

    public boolean updateProfile(String username, String fullName, String email, String theme){
        UpdateResult result = users.updateOne(
                Filters.eq("username", username),
                combine(
                        set("fullName", fullName),
                        set("email", email),
                        set("theme", theme)
                )
        );
        return result.getModifiedCount() > 0;
    }

    public boolean deleteByUsername(String username){
        DeleteResult result = users.deleteOne(Filters.eq("username", username));
        return result.getDeletedCount() > 0;
    }
}
