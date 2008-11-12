package demo47.server;
import java.util.ArrayList;

import javax.ejb.Remote;

import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;

import demo47.java.Doc02Customers;

@Remote
public interface CustomersRemote {

	public Response getCustomers(GridParams gridParams); 
	
	
	public Response insertCustomers(ArrayList<Doc02Customers> customers); 


	public Response updateCustomers(ArrayList<Doc02Customers> customers); 

	
	public Response deleteCustomers(ArrayList<Doc02Customers> customers); 	
}
