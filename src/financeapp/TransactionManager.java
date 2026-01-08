package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class TransactionManager {
    private final MongoCollection<Document>collection;
    private final String username;

    public TransactionManager(String username){
        MongoDatabase db = MongoDBConnection.getDatabase();
        collection = db.getCollection("transactions");
        this.username = username;
    }

    public void addTransaction(Transaction t){
        Document doc = t.toDocument();
        doc.append("username", username);
        collection.insertOne(doc);
    }

    public ArrayList<Transaction> getAllTransactions(){
        ArrayList<Transaction> list = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find(new Document("username", username)).iterator();
        while (cursor.hasNext()){
            Document d = cursor.next();
            list.add(new Transaction(
                    d.getObjectId("_id"),
                    d.getString("username"),
                    d.getString("Vrsta"),
                    d.getDouble("Iznos"),
                    d.getString("Opis"),
                    d.getString("Kategorija") )); }
        return list; }

public double getTotalIncome(){
        double total = 0;
        for(Transaction t : getAllTransactions()){
            if("Prihod".equals(t.getType())){
                total += t.getAmount(); } }
        return total; }

public double getTotalExpense(){
        double total = 0;
        for (Transaction t : getAllTransactions()){
            if ("Rashod".equals(t.getType())){
                total += t.getAmount(); } }
        return total; }

    public void updateTransaction(Transaction t) {
        collection.updateOne(
                new Document("_id", t.getId()).append("username", username),
                new Document("$set",
                        new Document("Vrsta", t.getType())
                                .append("Iznos", t.getAmount())
                                .append("Opis", t.getDescription())
                                .append("Kategorija", t.getCategory())
                )
        );
    }


public void deleteTransaction(ObjectId id){
        collection.deleteOne(new Document("_id", id).append("username", username));
}
}
