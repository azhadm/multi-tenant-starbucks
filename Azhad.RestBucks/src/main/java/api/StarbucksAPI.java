package api ;

import java.util.concurrent.BlockingQueue ;
import java.util.concurrent.LinkedBlockingQueue ;
import org.json.JSONObject;
import org.bson.Document;

public class StarbucksAPI {

    public enum OrderStatus { PLACED, PAID, PREPARING, SERVED, COLLECTED  }

    private static BlockingQueue<String> orderQueue = new LinkedBlockingQueue<String>();

    public static void placeOrder(String order_id) {
        try { 
            StarbucksAPI.orderQueue.put( order_id ) ; 
        } catch (Exception e) {}
        System.out.println( "Order Placed: " + order_id ) ;
    }

    public static void startOrderProcessor() {
        StarbucksBarista barista = new StarbucksBarista( orderQueue ) ;
        new Thread(barista).start();
    }

    public static void removeOrder(String order_id) {
        StarbucksAPI.orderQueue.remove( order_id ) ;
    }

    public static void setOrderStatus( JSONObject order, String URI, OrderStatus status, boolean isUpdate ) {
        switch ( status ) {
            case PLACED:
                order.put("status",OrderStatus.PLACED);
                order.put("message", "Order has been placed.");
                if(isUpdate) {
                    order.put("links", new JSONObject().put("order", URI).put("payment", URI +"/pay"));
                }
                else {
                    order.put("links", new JSONObject().put("order", URI+"/"+order.getString("_id")).put("payment", URI+"/"+order.getString("_id")+"/pay" ));
                }
            break;
                    
            case PAID:
                order.put("status",OrderStatus.PAID);
                order.put("message", "Payment Accepted.");
                order.getJSONObject("links").remove("payment");
            break;                        
        }
    }

    public static void setOrderStatus( Document order, OrderStatus status ) {
        switch ( status ) {
            case PREPARING:
                order.put("status",OrderStatus.PREPARING);
                order.put("message", "Order preparations in progress.");
                break;

            case SERVED:
                order.put("status",OrderStatus.SERVED);
                order.put("message", "Order served, wating for Customer pickup."); 
                break;

            case COLLECTED:
                order.put("status",OrderStatus.COLLECTED);
                order.put("message", "Order retrived by Customer.");
                break;   
        }
    }
}