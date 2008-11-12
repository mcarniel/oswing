package demo47.server;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.server.JPAUtils;

import demo47.java.Doc01Orders;
import demo47.java.Doc05Articles;

/**
 * Session Bean implementation class ArticlesBean
 */
@Stateless
public class ArticlesBean implements ArticlesRemote, ArticlesLocal {

	@PersistenceContext(unitName="demo47JPA")
	EntityManager em;

	@Override
	public Response deleteArticles(ArrayList<Doc05Articles> articles) {
		try {
			for(Doc05Articles o : articles) {
				o.setStatus("D");
				em.merge(o);
			}
			em.flush();
			return new VOResponse(Boolean.TRUE);
		} catch (Exception ex) {
			  ex.printStackTrace();
		      Throwable t = ex;
		      while(t.getCause()!=null)
		    	  t = t.getCause();
		      return new ErrorResponse(t.getMessage());
			} 

	}

	@Override
	public Response getArticles(GridParams gridParams) {
		try {
			Response res = JPAUtils.getBlockFromQuery(
					gridParams.getAction(),
					gridParams.getStartPos(),
					50,
					gridParams.getFilteredColumns(),
					gridParams.getCurrentSortedColumns(),
					gridParams.getCurrentSortedVersusColumns(),
				    Doc05Articles.class,
				    "SELECT a FROM Doc05Articles a WHERE a.status='E' ",
				    new Object[0],
				    em
			);
			return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ErrorResponse(e.getMessage());
		}
	}

	@Override
	public Response insertArticles(ArrayList<Doc05Articles> articles) {
		try {
			for(Doc05Articles o : articles) {
				em.persist(o);
			}
			em.flush();
			return new VOListResponse(articles,false,articles.size());
		} catch (Exception ex) {
			  ex.printStackTrace();
		      Throwable t = ex;
		      while(t.getCause()!=null)
		    	  t = t.getCause();
		      return new ErrorResponse(t.getMessage());
			} 

	}

	@Override
	public Response updateArticles(ArrayList<Doc05Articles> articles) {
		try {
			ArrayList list = new ArrayList();
			for(Doc05Articles o:  articles) {
			  o = em.merge(o);
  			  list.add(o);
			}
			em.flush();
			articles.clear();
			articles.addAll(list);
			return new VOListResponse(articles,false,articles.size());
		} catch (OptimisticLockException ex) {
			  return new ErrorResponse("Article has been already updated by another user.");			
		} catch (Exception ex) {
			  ex.printStackTrace();
		      Throwable t = ex;
		      while(t.getCause()!=null)
		    	  t = t.getCause();
		      return new ErrorResponse(t.getMessage());
			} 

	}

}