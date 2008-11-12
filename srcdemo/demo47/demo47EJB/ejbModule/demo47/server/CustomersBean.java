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
import demo47.java.Doc05Articles;

/**
 * Session Bean implementation class CustomersBean
 */
@Stateless
public class CustomersBean implements CustomersRemote, CustomersLocal {

	@PersistenceContext(unitName="demo47JPA")
	EntityManager em;
	
	@Override
	public Response deleteCustomers(ArrayList<Doc02Customers> customers) {
		try {
			for(Doc02Customers o : customers) {
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
	public Response getCustomers(GridParams gridParams) {
		try {
			return JPAUtils.getBlockFromQuery(
					gridParams.getAction(),
					gridParams.getStartPos(),
					50,
					gridParams.getFilteredColumns(),
					gridParams.getCurrentSortedColumns(),
					gridParams.getCurrentSortedVersusColumns(),
					Doc02Customers.class,
				    "select c from Doc02Customers c where c.status='E' ",
				    new Object[0],
				    em
			);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ErrorResponse(e.getMessage());
		}
	}

	@Override
	public Response insertCustomers(ArrayList<Doc02Customers> customers) {
		try {
			for(Doc02Customers o : customers) {
				em.persist(o);
			}
			em.flush();
			return new VOListResponse(customers,false,customers.size());
		} catch (Exception ex) {
		  ex.printStackTrace();
	      Throwable t = ex;
	      while(t.getCause()!=null)
	    	  t = t.getCause();
	      return new ErrorResponse(t.getMessage());
		} 
	}

	@Override
	public Response updateCustomers(ArrayList<Doc02Customers> customers) {
		try {
			ArrayList list = new ArrayList();
			for(Doc02Customers o:  customers) {
			  o = em.merge(o);
  			  list.add(o);
			}
			em.flush();
			customers.clear();
			customers.addAll(list);
			return new VOListResponse(customers,false,customers.size());
		} catch (OptimisticLockException ex) {
		  return new ErrorResponse("Customer has been already updated by another user.");			
		} catch (Throwable ex) {
  	      ex.printStackTrace();
	      Throwable t = ex;
	      while(t.getCause()!=null)
	    	  t = t.getCause();
	      return new ErrorResponse(t.getMessage());
		} 
	}



}
