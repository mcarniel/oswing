package demo47.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.server.*;
import demo47.java.Doc01Orders;
import demo47.java.Doc01OrdersPK;
import demo47.java.Doc02Customers;
import demo47.java.Doc03Pricelists;
import demo47.java.Doc04OrderRows;

/**
 * Session Bean implementation class OrdersBean
 */

@Stateless
public class OrdersBean implements OrdersRemote, OrdersLocal {

	@PersistenceContext(unitName="demo47JPA")
	EntityManager em;


	@Override
	public Response getOrders(GridParams gridParams) {
		try {
			return JPAUtils.getBlockFromQuery(
					gridParams.getAction(),
					gridParams.getStartPos(),
					50,
					gridParams.getFilteredColumns(),
					gridParams.getCurrentSortedColumns(),
					gridParams.getCurrentSortedVersusColumns(),
				    Doc01Orders.class,
				    "select o from Doc01Orders o JOIN FETCH o.customerId JOIN FETCH o.doc03Pricelists where o.status='E' ",
				    new Object[0],
				    em
			);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ErrorResponse(e.getMessage());
		}
	}

	
	@Override
	public Doc01Orders getOrder(Doc01OrdersPK pk) {
		try {
			return (Doc01Orders)em.find(Doc01Orders.class,pk);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	

	@Override
	public Response deleteOrders(ArrayList<Doc01Orders> orders) {
		try {
			for(Doc01Orders o : orders) {
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
	public Response insertOrders(ArrayList<Doc01Orders> orders) {
		try {
			for(Doc01Orders o : orders) {
				em.persist(o);
			}
			em.flush();
			return new VOListResponse(orders,false,orders.size());
		} catch (Exception ex) {
			  ex.printStackTrace();
		      Throwable t = ex;
		      while(t.getCause()!=null)
		    	  t = t.getCause();
		      return new ErrorResponse(t.getMessage());
			} 
	}


	@Override
	public Response updateOrders(ArrayList<Doc01Orders> orders) {
		try {
			ArrayList list = new ArrayList();
			for(Doc01Orders o : orders) {
			  o = em.merge(o);
  			  list.add( o );
			}
			em.flush();
			orders.clear();
			orders.addAll(list);
			return new VOListResponse(orders,false,orders.size());
		} catch (OptimisticLockException ex) {
			  return new ErrorResponse("Order has been already updated by another user.");			
		} catch (Exception ex) {
			  ex.printStackTrace();
		      Throwable t = ex;
		      while(t.getCause()!=null)
		    	  t = t.getCause();
		      return new ErrorResponse(t.getMessage());
			} 

	}
	

	public Response getOrderRows(GridParams gridParams,Doc01OrdersPK pk) {
		try {
			return JPAUtils.getBlockFromQuery(
					gridParams.getAction(),
					gridParams.getStartPos(),
					50,
					gridParams.getFilteredColumns(),
					gridParams.getCurrentSortedColumns(),
					gridParams.getCurrentSortedVersusColumns(),
					Doc04OrderRows.class,
				    "select o from Doc04OrderRows o JOIN FETCH o.itemId where o.status='E' and o.pk.docNumber=?1 and o.pk.docYear=?2 ",
				    new Object[]{pk.getDocNumber(),pk.getDocYear()},
				    em
			);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ErrorResponse(e.getMessage());
		}		
	}
	
	
	public Response insertOrderRows(Doc01Orders vo,ArrayList<Doc04OrderRows> orders){
		try {
			ArrayList list = new ArrayList();
			for(Doc04OrderRows o : orders) {

				Number rowNumber = (Number)em.createNamedQuery("Doc04OrderRows.getMaxRowNumber")
				.setParameter(1,o.getPk().getDocYear())
				.setParameter(2,o.getPk().getDocNumber())
				.getSingleResult();
				if (rowNumber==null)
					rowNumber = new Long(1);
				o.getPk().setRowNumber(rowNumber.longValue());
				em.persist(o);
				list.add(em.find(Doc04OrderRows.class, o.getPk()));
			}
			vo = calcTotal(vo);
			em.flush();
			orders.clear();
			orders.addAll(list);

			for(Doc04OrderRows o : orders) {
				o.setDoc01Orders(vo);
			}
			vo.setDoc04OrderRowsCollection(orders);			
			return new VOResponse(vo);
			
			//return new VOListResponse(orders,false,orders.size());
		} catch (Exception ex) {
			  ex.printStackTrace();
		      Throwable t = ex;
		      while(t.getCause()!=null)
		    	  t = t.getCause();
		      return new ErrorResponse(t.getMessage());
		} 		
	} 

	
	public Doc01Orders calcTotal(Doc01Orders vo) {
		BigDecimal total = new BigDecimal(0);
		Response res = getOrderRows(new GridParams(), vo.getPk());
		if (res.isError())
			throw new RuntimeException(res.getErrorMessage());
		List<Doc04OrderRows> rows = ((VOListResponse)res).getRows();
		for(Doc04OrderRows rowVO: rows) {
			if (rowVO.getStatus().equals("E") && rowVO.getTotal()!=null)
				total = total.add(rowVO.getTotal());
		}
		vo.setTotal(total);
		vo = em.merge(vo);
		return vo;
	}
	

	public Response updateOrderRows(Doc01Orders vo,ArrayList<Doc04OrderRows> orders){
		try {
			ArrayList list = new ArrayList();
			for(Doc04OrderRows o:  orders) {
			  o = em.merge(o);
  			  list.add(o);
			}
			vo = calcTotal(vo);
			em.flush();
			orders.clear();
			orders.addAll(list);
			
			for(Doc04OrderRows o : orders) {
				o.setDoc01Orders(vo);
			}
			vo.setDoc04OrderRowsCollection(orders);
			return new VOResponse(vo);
			
			//return new VOListResponse(orders,false,orders.size());
		} catch (OptimisticLockException ex) {
			  return new ErrorResponse("Order Row has been already updated by another user.");			
		} catch (Exception ex) {
			  ex.printStackTrace();
		      Throwable t = ex;
		      while(t.getCause()!=null)
		    	  t = t.getCause();
		      return new ErrorResponse(t.getMessage());
		} 		
	}

	
	public Response deleteOrderRows(Doc01Orders vo,ArrayList<Doc04OrderRows> orders){
		try {
			for(Doc04OrderRows o : orders) {
				o.setStatus("D");
				em.merge(o);
			}
			calcTotal(vo);
			em.flush();
			
			return new VOResponse(vo);

		} catch (Exception ex) {
			  ex.printStackTrace();
		      Throwable t = ex;
		      while(t.getCause()!=null)
		    	  t = t.getCause();
		      return new ErrorResponse(t.getMessage());
		} 		
	} 	
	
}
