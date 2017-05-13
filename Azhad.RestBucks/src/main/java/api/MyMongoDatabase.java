package api;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import static com.mongodb.client.model.Filters.*;
import org.bson.Document;
import java.util.ArrayList;

public class MyMongoDatabase {

    public static Document getOrder(String order_id) {
        // MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://10.0.0.183:27017,10.0.0.62:27017,10.0.2.187:27017/ops?replicaSet=rs0&connectTimeoutMS=300000"));
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("Test");
        MongoCollection<Document> collection = database.getCollection("Order");
        Document myDoc = collection.find(eq("_id", order_id)).first();
        mongoClient.close();
        return myDoc;
    }    
    
    public static ArrayList<Document> getOrders() {
        // MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://10.0.0.183:27017,10.0.0.62:27017,10.0.2.187:27017/ops?replicaSet=rs0&connectTimeoutMS=300000"));
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("Test");
        MongoCollection<Document> collection = database.getCollection("Order");

        FindIterable<Document> find = collection.find();
        ArrayList<Document> docs = new ArrayList<Document>();
        for (Document document : find) {
            docs.add(document);
        }
        mongoClient.close();
        return docs;
    }

    public static void saveOrder(Document doc) {
        // MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://10.0.0.183:27017,10.0.0.62:27017,10.0.2.187:27017/ops?replicaSet=rs0&connectTimeoutMS=300000"));
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("Test");
        MongoCollection<Document> collection = database.getCollection("Order");
        collection.insertOne(doc);
        mongoClient.close();
    }

    public static void updateOrder(Document doc) {
        // MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://10.0.0.183:27017,10.0.0.62:27017,10.0.2.187:27017/ops?replicaSet=rs0&connectTimeoutMS=300000"));
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("Test");
        MongoCollection<Document> collection = database.getCollection("Order");
        collection.updateOne(eq("_id", doc.getString("_id")), new Document("$set", doc));
        mongoClient.close();
    }

    public static void deleteOrder(String order_id) {
        // MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://10.0.0.183:27017,10.0.0.62:27017,10.0.2.187:27017/ops?replicaSet=rs0&connectTimeoutMS=300000"));
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("Test");
        MongoCollection<Document> collection = database.getCollection("Order");
        collection.deleteOne(eq("_id", order_id));
        mongoClient.close();
    }
}