package demo47.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.util.java.Consts;

import demo47.java.Doc01Orders;
import demo47.java.Doc02Customers;
import demo47.java.Doc03Pricelists;
import demo47.java.Doc03PricelistsPK;
import demo47.server.CustomersRemote;
import demo47.server.OrdersRemote;
import demo47.server.PricelistsRemote;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for orders.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class OrdersController extends GridController implements GridDataLocator {

  private OrdersFrame frame = null;


  public OrdersController() {
    this.frame = new OrdersFrame(this);
  }


  /**
   * Callback method invoked to load data on the grid.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param filteredColumns filtered columns
   * @param currentSortedColumns sorted columns
   * @param currentSortedVersusColumns ordering versus of sorted columns
   * @param valueObjectType v.o. type
   * @param otherGridParams other grid parameters
   * @return response from the server: an object of type VOListResponse if data loading was successfully completed, or an ErrorResponse onject if some error occours
   */
  public Response loadData(
      int action,
      int startIndex,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Class valueObjectType,
      Map otherGridParams) {


    try {
    	OrdersRemote ordersRemoteService = (OrdersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("OrdersBean");
    	return ordersRemoteService.getOrders(
    	  new GridParams(
		      action,
		      startIndex,
		      filteredColumns,
		      currentSortedColumns,
		      currentSortedVersusColumns,
		      otherGridParams
    	  )
    	);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }

  
  public void doubleClick(int rowNumber, org.openswing.swing.message.receive.java.ValueObject persistentObject) {
	  Doc01Orders vo = (Doc01Orders)persistentObject;  
	  OrderController f = new OrderController(frame,vo.getPk());  
  }
  
  
  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
	    try {
	    	OrdersRemote ordersRemoteService = (OrdersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("OrdersBean");
	    	return ordersRemoteService.deleteOrders(persistentObjects);
	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	      return new ErrorResponse(ex.getMessage());
	    }
  }

  



}
