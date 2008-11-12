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
import demo47.java.Doc02Customers;
import demo47.java.Doc03Pricelists;
import demo47.java.Doc03PricelistsPK;
import demo47.java.Doc04OrderRows;

/**
 * Session Bean implementation class PricelistsBean
 */
@Stateless
public class PricelistsBean implements PricelistsRemote, PricelistsLocal {

	@PersistenceContext(unitName="demo47JPA")
	EntityManager em;
	
	@Override
	public Response deletePricelists(ArrayList<Doc03Pricelists> pricelists) {
		try {
			for(Doc03Pricelists o : pricelists) {
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
	public Response getPricelists(Doc02Customers customer, GridParams gridParams) {
		try {
			return JPAUtils.getBlockFromQuery(
					gridParams.getAction(),
					gridParams.getStartPos(),
					100,
					gridParams.getFilteredColumns(),
					gridParams.getCurrentSortedColumns(),
					gridParams.getCurrentSortedVersusColumns(),
					Doc03Pricelists.class,
				    "SELECT p FROM Doc03Pricelists p JOIN FETCH p.customerIdDoc02 WHERE p.status='E' and p.customerIdDoc02=?1 ",
				    new Object[]{customer},
				    em
			);
		} catch (Exception e) {
			
			e.printStackTrace();
			// TODO Auto-generated catch block
			return new ErrorResponse(e.getMessage());
		}
	}

	@Override
	public Response insertPricelists(ArrayList<Doc03Pricelists> pricelists) {
		try {
			for(Doc03Pricelists o : pricelists) {
				em.persist(o);
			}
			em.flush();
			return new VOListResponse(pricelists,false,pricelists.size());
		} catch (Exception ex) {
			  ex.printStackTrace();
		      Throwable t = ex;
		      while(t.getCause()!=null)
		    	  t = t.getCause();
		      return new ErrorResponse(t.getMessage());
			} 

	}

	@Override
	public Response updatePricelists(ArrayList<Doc03Pricelists> pricelists) {
		try {
			ArrayList list = new ArrayList();
			for(Doc03Pricelists o:  pricelists) {
			  o = em.merge(o);
  			  list.add(o);
			}
			em.flush();
			pricelists.clear();
			pricelists.addAll(list);
			return new VOListResponse(pricelists,false,pricelists.size());
		} catch (OptimisticLockException ex) {
			  return new ErrorResponse("Pricelist has been already updated by another user.");			
		} catch (Exception ex) {
			  ex.printStackTrace();
		      Throwable t = ex;
		      while(t.getCause()!=null)
		    	  t = t.getCause();
		      return new ErrorResponse(t.getMessage());
			} 
	}



}
