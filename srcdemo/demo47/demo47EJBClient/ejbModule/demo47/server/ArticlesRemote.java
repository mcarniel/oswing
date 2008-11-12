package demo47.server;
import java.util.ArrayList;

import javax.ejb.Remote;

import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;

import demo47.java.Doc05Articles;

@Remote
public interface ArticlesRemote {

	public Response getArticles(GridParams gridParams); 
	
	
	public Response insertArticles(ArrayList<Doc05Articles> articles); 


	public Response updateArticles(ArrayList<Doc05Articles> articles); 

	
	public Response deleteArticles(ArrayList<Doc05Articles> articles); 	
}
