package api ;

import org.restlet.representation.* ;
import org.restlet.ext.json.* ;
import org.restlet.resource.* ;

import java.util.ArrayList;

import com.mongodb.util.JSON;
import org.bson.Document;

public class OrdersResource extends ServerResource {

    @Get
    public Representation get_action (Representation rep)  throws Exception{
        try {
            ArrayList<Document> docs = MyMongoDatabase.getOrders();
            return new JsonRepresentation(JSON.serialize(docs));       
        }
        catch ( Exception e ) {
            setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
            System.out.println("Error: " + e.getMessage());
            Document doc = new Document("status", "error").append("message", "Server Error, Try Again Later.");
            return new JsonRepresentation(doc.toJson()) ;
        }
    }
}


