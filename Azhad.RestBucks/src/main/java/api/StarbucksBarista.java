
package api ;

import java.util.concurrent.BlockingQueue;
import org.bson.Document;

public class StarbucksBarista implements Runnable {

	protected BlockingQueue<String> blockingQueue ;

	public StarbucksBarista(BlockingQueue<String> queue) {
		this.blockingQueue = queue;
	}

	@Override
	public void run() {
		try {
			try { Thread.sleep(5000); } catch ( Exception e ) {System.out.println("Error: " + e.getMessage()); e.printStackTrace();}  
			String order_id = blockingQueue.take();
			Document order = MyMongoDatabase.getOrder(order_id);
			if ( order != null && order.getString("status").equals(StarbucksAPI.OrderStatus.PAID.toString()) ) {
				System.out.println(Thread.currentThread().getName() + " Processed Order: " + order_id );
				StarbucksAPI.setOrderStatus( order, StarbucksAPI.OrderStatus.PREPARING ) ;
				order.put("status", StarbucksAPI.OrderStatus.PREPARING.toString());
				MyMongoDatabase.updateOrder(order);
				try { Thread.sleep(20000); } catch ( Exception e ) {System.out.println("Error: " + e.getMessage()); e.printStackTrace();}
				StarbucksAPI.setOrderStatus( order, StarbucksAPI.OrderStatus.SERVED ) ;
				order.put("status", StarbucksAPI.OrderStatus.SERVED.toString());
				MyMongoDatabase.updateOrder(order);
				try { Thread.sleep(10000); } catch ( Exception e ) {System.out.println("Error: " + e.getMessage()); e.printStackTrace();}
				StarbucksAPI.setOrderStatus( order, StarbucksAPI.OrderStatus.COLLECTED ) ;
				order.put("status", StarbucksAPI.OrderStatus.COLLECTED.toString());
				MyMongoDatabase.updateOrder(order);
			}
		} catch (InterruptedException e) {
			System.out.println("Error: " + e.getMessage()); e.printStackTrace();
		}
	}

}