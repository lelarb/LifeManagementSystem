package auth;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import db.MongoDBConnection;
import org.bson.Document;

public class UserService {
    private final MongoCollection<Document> users;

    public UserService(){
        MongoDatabase db = MongoDBConnection.getDatabase();
        users = db.getCollection("users");
    }

    public boolean register(String username, String password){
        Document exists = users.find(new Document("username", username)).first();
        if(exists != null) return false;

        users.insertOne(new Document("username", username).append("password", password));
        return true;
    }

    public boolean login(String username, String password){
        Document found = users.find(new Document("username", username).append("password", password)).first();
        return found != null;
    }
}
