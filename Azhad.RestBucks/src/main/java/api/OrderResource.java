package api ;

import org.restlet.representation.* ;
import org.restlet.resource.* ;
import org.restlet.data.Tag ;
import org.restlet.data.Header ;
import org.restlet.util.Series ;
import org.restlet.ext.crypto.DigestUtils ;
import org.restlet.ext.json.JsonRepresentation;
import org.json.JSONObject;
import java.util.UUID ;
import org.bson.Document;

public class OrderResource extends ServerResource {

    @Get
    public Representation get_action() throws Exception {
        try {
            Series<Header> headers = (Series<Header>) getRequest().getAttributes().get("org.restlet.http.headers");
            if ( headers != null ) {
                String etag = headers.getFirstValue("If-None-Match") ;
                System.out.println( "HEADERS: " + headers.getNames() ) ;
                System.out.println( "ETAG: " + etag ) ;
            }
            
            String order_id = getAttribute("order_id") ;        
            
            if ( order_id == null || order_id.equals("") ) {

                setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
                Document doc = new Document("status", "error").append("message", "Order not found.");

                return new JsonRepresentation(doc.toJson()) ;
            }
            else {
                Document myDoc = MyMongoDatabase.getOrder(order_id);

                if ( order_id == null || order_id.equals("")  || myDoc == null ) {
                    setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
                    Document doc = new Document("status", "error").append("message", "Order not found.");

                    return new JsonRepresentation(doc.toJson()) ;
                }                
                else {
                    Representation result = new JsonRepresentation(myDoc.toJson()) ;
                    System.out.println( "Get Text: " + result.getText() ) ;
                    String  hash = DigestUtils.toMd5 ( result.getText() ) ;
                    System.out.println( "Get Hash: " + hash ) ;
                    result.setTag( new Tag( hash ) ) ;
                    return result ;
                }
            }
        }
        catch ( Exception e ) {
            setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
            System.out.println("Error: " + e.getMessage());
            Document doc = new Document("status", "error").append("message", "Server Error, Try Again Later.");

            return new JsonRepresentation(doc.toJson()) ;
        }
    }


    @Post
    public Representation post_action (JsonRepresentation rep) throws Exception {
        JSONObject order = null;

        try {
            order = rep.getJsonObject();
            order.put("_id", UUID.randomUUID().toString()) ;

            StarbucksAPI.setOrderStatus( order, getReference().toString(), StarbucksAPI.OrderStatus.PLACED, false ) ;

            Representation result = new JsonRepresentation(order) ;

            System.out.println( "Text: " + result.getText() ) ;
            String  hash = DigestUtils.toMd5 ( result.getText() ) ;
            result.setTag( new Tag( hash ) ) ;

            MyMongoDatabase.saveOrder(Document.parse(result.getText()));

            return result ;
        }
        catch ( Exception e ) {
            setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ; 
            System.out.println("Error\n\n\n\n: " + e.getMessage());
            Document doc = new Document("status", "error").append("message", "Server Error, Try Again Later.");

            return new JsonRepresentation(doc.toJson()) ;
        }
    }

   @Put
    public Representation put_action (JsonRepresentation rep) throws Exception {

        JSONObject order = null;

        try {
            order = rep.getJsonObject();
            order.put("_id", getAttribute("order_id")) ;

            String order_id = getAttribute("order_id") ; 

            Document myDoc = MyMongoDatabase.getOrder(order_id);

            if ( order_id == null || order_id.equals("")  || myDoc == null ) {

                setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
                Document doc = new Document("status", "error").append("message", "Order not found.");

                return new JsonRepresentation(doc.toJson()) ;
            }                
            else if ( myDoc != null && !myDoc.getString("status").equals(StarbucksAPI.OrderStatus.PLACED.toString()) ) {

                setStatus( org.restlet.data.Status.CLIENT_ERROR_PRECONDITION_FAILED ) ;
                Document doc = new Document("status", "error").append("message", "Order Update Rejected.");

                return new JsonRepresentation(doc.toJson()) ;
            }
            else {

                StarbucksAPI.setOrderStatus( order, getReference().toString(), StarbucksAPI.OrderStatus.PLACED, true ) ;
                Representation result = new JsonRepresentation(order) ;
                MyMongoDatabase.updateOrder(Document.parse(result.getText()));

                String  hash = DigestUtils.toMd5 ( result.getText() ) ;
                result.setTag( new Tag( hash ) ) ;
                return result ;
            }
        }
        catch ( Exception e ) {
            setStatus( org.restlet.data.Status.SERVER_ERROR_INTERNAL ) ;
            System.out.println("Error: " + e.getMessage());
            Document doc = new Document("status", "error").append("message", "Server Error, Try Again Later.");

            return new JsonRepresentation(doc.toJson()) ;
        }
    }

    @Delete
    public Representation delete_action (JsonRepresentation rep) throws Exception {
        try {
            String order_id = getAttribute("order_id") ;
            Document myDoc = MyMongoDatabase.getOrder(order_id);

            if ( order_id == null || order_id.equals("")  || myDoc == null ) {

                setStatus( org.restlet.data.Status.CLIENT_ERROR_NOT_FOUND ) ;
                Document doc = new Document("status", "error").append("message", "Order not found.");

                return new JsonRepresentation(doc.toJson()) ;
            }        
            else if ( !myDoc.getString("status").equals(StarbucksAPI.OrderStatus.PLACED.toString()) ) {

                setStatus( org.restlet.data.Status.CLIENT_ERROR_PRECONDITION_FAILED ) ;
                Document doc = new Document("status", "error").append("message", "Order Cancelling Rejected.");

                return new JsonRepresentation(doc.toJson()) ;
            }
            else {
                MyMongoDatabase.deleteOrder(order_id);
                StarbucksAPI.removeOrder(order_id);
                return null ;    
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



