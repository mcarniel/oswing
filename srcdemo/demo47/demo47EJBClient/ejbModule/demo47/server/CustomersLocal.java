package demo47.server;
import java.util.ArrayList;

import javax.ejb.Local;

import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;

import demo47.java.Doc02Customers;
import demo47.java.Doc05Articles;

@Local
public interface CustomersLocal {

	public Response getCustomers(GridParams gridParams); 
	
	
	public Response insertCustomers(ArrayList<Doc02Customers> customers); 


	public Response updateCustomers(ArrayList<Doc02Customers> customers); 

	
	public Response deleteCustomers(ArrayList<Doc02Customers> customers); 	
}
