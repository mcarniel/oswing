package demo47.server;
import java.util.ArrayList;

import javax.ejb.Local;

import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;

import demo47.java.Doc01Orders;
import demo47.java.Doc05Articles;

@Local
public interface ArticlesLocal {

	public Response getArticles(GridParams gridParams); 
	
	
	public Response insertArticles(ArrayList<Doc05Articles> articles); 


	public Response updateArticles(ArrayList<Doc05Articles> articles); 

	
	public Response deleteArticles(ArrayList<Doc05Articles> articles); 	
}
