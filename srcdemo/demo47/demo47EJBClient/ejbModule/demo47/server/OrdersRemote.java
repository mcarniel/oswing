package demo47.server;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Remote;

import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;

import demo47.java.Doc01Orders;
import demo47.java.Doc01OrdersPK;
import demo47.java.Doc04OrderRows;

@Remote
public interface OrdersRemote {

	
	public Response getOrders(GridParams gridParams); 

	
	public Doc01Orders getOrder(Doc01OrdersPK pk);
	
	
	public Response insertOrders(ArrayList<Doc01Orders> orders); 


	public Response updateOrders(ArrayList<Doc01Orders> orders); 

	
	public Response deleteOrders(ArrayList<Doc01Orders> orders); 
	

	public Response getOrderRows(GridParams gridParams,Doc01OrdersPK pk); 
	
	
	public Response insertOrderRows(Doc01Orders vo,ArrayList<Doc04OrderRows> orders); 


	public Response updateOrderRows(Doc01Orders vo,ArrayList<Doc04OrderRows> orders); 

	
	public Response deleteOrderRows(Doc01Orders vo,ArrayList<Doc04OrderRows> orders); 
}
