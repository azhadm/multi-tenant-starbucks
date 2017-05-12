package api ;

import org.restlet.representation.* ;
import org.restlet.resource.* ;
import org.restlet.ext.json.JsonRepresentation;
import org.bson.Document;
import org.json.JSONObject;

public class PaymentResource extends ServerResource {

    @Post
    public Representation post_action (Representation rep) throws Exception {
        try {
            String order_id = getAttribute("order_id") ;
            Document myDoc = MyMongoDatabase.getOrder(order_id);
            
            if ( myDoc == null ) {
                setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
                Document doc = new Document("status", "error").append("message", "Order not found.");

                return new JsonRepresentation(doc.toJson()) ;
            }
            if ( myDoc != null && !myDoc.getString("status").equals(StarbucksAPI.OrderStatus.PLACED.toString()) ) {
                setStatus( org.restlet.data.Status.CLIENT_ERROR_PRECONDITION_FAILED ) ;
                Document doc = new Document("status", "error").append("message", "Order Payment Rejected.");
                return new JsonRepresentation(doc.toJson()) ;
            }
            else {
                JSONObject order = new JSONObject(myDoc);
                StarbucksAPI.setOrderStatus( order, getReference().toString(), StarbucksAPI.OrderStatus.PAID, false ) ;
                MyMongoDatabase.updateOrder(Document.parse(order.toString()));
                StarbucksAPI.placeOrder( order.getString("_id") ) ;
                StarbucksAPI.startOrderProcessor() ;
                return new JsonRepresentation(order) ;           
            }
        }
        catch ( Exception e ) {
            setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
            System.out.println("Error: " + e.getMessage());
            Document doc = new Document("status", "error").append("message", "Server Error, Try Again Later.");
            return new JsonRepresentation(doc.toJson()) ;
        }
    }
}


