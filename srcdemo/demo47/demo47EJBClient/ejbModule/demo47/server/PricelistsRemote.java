package demo47.server;
import java.util.ArrayList;

import javax.ejb.Remote;

import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;

import demo47.java.Doc02Customers;
import demo47.java.Doc03Pricelists;

@Remote
public interface PricelistsRemote {

	public Response getPricelists(Doc02Customers customer,GridParams gridParams); 
	
	
	public Response insertPricelists(ArrayList<Doc03Pricelists> pricelists); 


	public Response updatePricelists(ArrayList<Doc03Pricelists> pricelists); 

	
	public Response deletePricelists(ArrayList<Doc03Pricelists> pricelists); 	
}
