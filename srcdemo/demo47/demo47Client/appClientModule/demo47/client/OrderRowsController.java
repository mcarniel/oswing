package demo47.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.message.receive.java.*;

import java.math.BigDecimal;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.client.GridControl;

import demo47.java.Doc01Orders;
import demo47.java.Doc02Customers;
import demo47.java.Doc04OrderRows;
import demo47.java.Doc04OrderRowsPK;
import demo47.java.Doc05Articles;
import demo47.server.CustomersRemote;
import demo47.server.OrdersRemote;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for customers.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class OrderRowsController extends GridController implements GridDataLocator {

  private OrderFrame frame = null;


  public OrderRowsController(OrderFrame frame) {
    this.frame = frame;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    Doc04OrderRows vo = (Doc04OrderRows)valueObject;
    //vo.setDataIns(new java.sql.Timestamp(System.currentTimeMillis()));
    vo.setCodUtenteIns("DEMO47");
    //vo.setStatus("E");
    Doc04OrderRowsPK pk = new Doc04OrderRowsPK();
    Doc01Orders parentVO = (Doc01Orders)frame.getForm().getVOModel().getValueObject();
    vo.setDoc01Orders(parentVO);
    pk.setDocYear(parentVO.getPk().getDocYear());
    pk.setDocNumber(parentVO.getPk().getDocNumber());
    //pk.setRowNumber(frame.getGrid().getVOListTableModel().getRowCount());    
    vo.setPk(pk);
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
        Doc01Orders parentVO = (Doc01Orders)frame.getForm().getVOModel().getValueObject();

    	OrdersRemote orderRowsService = (OrdersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("OrdersBean");
    	return orderRowsService.getOrderRows(
    	  new GridParams(
		      action,
		      startIndex,
		      filteredColumns,
		      currentSortedColumns,
		      currentSortedVersusColumns,
		      otherGridParams
    	  ),
    	  parentVO.getPk()
    	);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
	    try {
	        Doc01Orders parentVO = (Doc01Orders)frame.getForm().getVOModel().getValueObject();

	    	OrdersRemote orderRowsService = (OrdersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("OrdersBean");
	    	Response res = orderRowsService.insertOrderRows(parentVO,newValueObjects);
	    	if (res.isError())
	    		return res;
	    	parentVO = (Doc01Orders)((VOResponse)res).getVo();
	    	frame.getForm().getVOModel().setValueObject(parentVO);
	    	frame.getForm().pull();
	    	ArrayList list = new ArrayList(parentVO.getDoc04OrderRowsCollection());
	    	return new VOListResponse(list,false,list.size());
	    }
	    catch (Throwable ex) {
	      ex.printStackTrace();
	      return new ErrorResponse(ex.getMessage());
	    }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects related to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
	    try {
	        Doc01Orders parentVO = (Doc01Orders)frame.getForm().getVOModel().getValueObject();

	    	for(Object vo: persistentObjects) {
		        //((Doc04OrderRows)vo).setDataAgg(new java.sql.Timestamp(System.currentTimeMillis()));
		        ((Doc04OrderRows)vo).setCodUtenteAgg("DEMO47");
	    	}
	    	
	    	OrdersRemote orderRowsService = (OrdersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("OrdersBean");
	    	Response res = orderRowsService.updateOrderRows(parentVO,persistentObjects);
	    	if (res.isError())
	    		return res;
	    	parentVO = (Doc01Orders)((VOResponse)res).getVo();
	    	frame.getForm().getVOModel().setValueObject(parentVO);
	    	frame.getForm().pull();
	    	ArrayList list = new ArrayList(parentVO.getDoc04OrderRowsCollection());
	    	return new VOListResponse(list,false,list.size());
	    }
	    catch (Throwable ex) {
	      ex.printStackTrace();
	      return new ErrorResponse(ex.getMessage());
	    }	  
  }

  
  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
	    try {
	        Doc01Orders parentVO = (Doc01Orders)frame.getForm().getVOModel().getValueObject();

	    	OrdersRemote orderRowsService = (OrdersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("OrdersBean");
	    	Response res = orderRowsService.deleteOrderRows(parentVO,persistentObjects);
	    	if (res.isError())
	    		return res;
	    	parentVO = (Doc01Orders)((VOResponse)res).getVo();
	    	frame.getForm().getVOModel().setValueObject(parentVO);
	    	frame.getForm().pull();
	    	return new VOResponse(Boolean.TRUE);

	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	      return new ErrorResponse(ex.getMessage());
	    }
  }

  
  public boolean validateCell(int rowNumber, java.lang.String attributeName, java.lang.Object oldValue, java.lang.Object newValue) {
	  Doc04OrderRows vo = (Doc04OrderRows)frame.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
	  if ("qty".equals(attributeName)) {
		  if (newValue==null)
			  vo.setTotal(new BigDecimal(0));
		  else if (vo.getUnitPrice()!=null)
			  vo.setTotal(((BigDecimal)newValue).multiply(vo.getUnitPrice()));
	  }
	  else if ("unitPrice".equals(attributeName)) {
		  if (newValue==null)
			  vo.setTotal(new BigDecimal(0));
		  else if (vo.getQty()!=null)
			  vo.setTotal(vo.getQty().multiply(((BigDecimal)newValue)));
	  }
	  return true;
  }  


}
