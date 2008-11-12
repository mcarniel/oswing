package demo47.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;

import org.openswing.swing.form.client.FormController;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.send.java.GridParams;

import demo47.java.Doc01Orders;
import demo47.java.Doc01OrdersPK;
import demo47.server.OrdersRemote;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Form controller for order.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class OrderController  extends FormController {

	  private OrdersFrame parentFrame = null;
	  private OrderFrame frame = null;
	  private Doc01OrdersPK pk = null;
	  
	  
	  public OrderController(OrdersFrame parentFrame,Doc01OrdersPK pk) {
		this.parentFrame = parentFrame;
		this.pk = pk;
		frame = new OrderFrame(parentFrame,this);
	    MDIFrame.add(frame);

	    if (pk!=null) {
	      frame.getForm().setMode(Consts.READONLY);
	      frame.getForm().reload();
	    }
	    else {
	      frame.getForm().setMode(Consts.INSERT);
	    }
	  }	


	  /**
	   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
	   * @param valueObjectClass value object class
	   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
	   */
	  public Response loadData(Class valueObjectClass) {
	    try {
	      // since this method could be invoked also when selecting another row on the linked grid,
	      // the pk attribute must be recalculated from the grid...
	      int row = parentFrame.getGrid().getSelectedRow();
	      if (row!=-1) {
	    	  Doc01Orders parentVO = (Doc01Orders)parentFrame.getGrid().getVOListTableModel().getObjectForRow(row);
	        pk = parentVO.getPk();
	      }

	      OrdersRemote ordersService = (OrdersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("OrdersBean");
	      Doc01Orders vo = ordersService.getOrder(pk);
	      return new VOResponse(vo);
	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	      return new ErrorResponse(ex.getMessage());
	    }
	  }


	  /**
	   * Callback method called when the data loading is completed.
	   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
	   */
	  public void loadDataCompleted(boolean error) {
	    //frame.getGrid().getOtherGridParams().put("pk",pk);
	    frame.getGrid().reloadData();
	  }


	  /**
	   * Method called by the Form panel to insert new data.
	   * @param newValueObject value object to save
	   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	   */
	  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
		    try {
		    	OrdersRemote ordersRemoteService = (OrdersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("OrdersBean");
		    	ArrayList orders = new ArrayList();
		    	orders.add(newPersistentObject);
		    	Response res = ordersRemoteService.insertOrders(orders);
		    	if (!res.isError())
		    		return new VOResponse(((VOListResponse)res).getRows().get(0));
		    	else
		    		return res;
		    }
		    catch (Exception ex) {
		      ex.printStackTrace();
		      return new ErrorResponse(ex.getMessage());
		    }
	  }

	  
	  /**
	   * Method called by the Form panel to update existing data.
	   * @param oldPersistentObject original value object, previous to the changes
	   * @param persistentObject value object to save
	   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	   */
	  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
		    try {
		    	OrdersRemote ordersRemoteService = (OrdersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("OrdersBean");
		    	ArrayList orders = new ArrayList();
		    	orders.add(persistentObject);
		    	Response res = ordersRemoteService.updateOrders(orders);
		    	if (!res.isError())
		    		return new VOResponse(((VOListResponse)res).getRows().get(0));
		    	else
		    		return res;
		    }
		    catch (Exception ex) {
		      ex.printStackTrace();
		      return new ErrorResponse(ex.getMessage());
		    }
	  }

	  
	  /**
	   * Method called by the Form panel to delete existing data.
	   * @param persistentObject value object to delete
	   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	   */
	  public Response deleteRecord(ValueObject persistentObject) throws Exception {
		    try {
		    	OrdersRemote ordersRemoteService = (OrdersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("OrdersBean");
		    	ArrayList orders = new ArrayList();
		    	orders.add(persistentObject);
		    	return ordersRemoteService.deleteOrders(orders);
		    }
		    catch (Exception ex) {
		      ex.printStackTrace();
		      return new ErrorResponse(ex.getMessage());
		    }
	  }


	  /**
	   * Callback method called when the Form mode is changed.
	   * @param currentMode current Form mode
	   */
	  public void modeChanged(int currentMode) {
	    if (currentMode==Consts.INSERT) {
	      frame.getGrid().clearData();
	      frame.setButtonsEnabled(false);
	    }
	    else {
	      frame.setButtonsEnabled(true);
	    }

	  }



	}
