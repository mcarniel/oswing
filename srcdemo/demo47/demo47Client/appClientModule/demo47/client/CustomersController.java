package demo47.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.client.GridControl;

import demo47.java.Doc02Customers;
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
public class CustomersController extends GridController implements GridDataLocator {

  private CustomersFrame frame = null;


  public CustomersController() {
    this.frame = new CustomersFrame(this);
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    Doc02Customers vo = (Doc02Customers)valueObject;
    //vo.setDataIns(new java.sql.Timestamp(System.currentTimeMillis()));
    vo.setCodUtenteIns("DEMO47");
    //vo.setStatus("E");
  }

  
  /**
   * Callback method invoked when the user has selected another row.
   * @param rowNumber selected row index
   */
  public void rowChanged(int rowNumber) {
    if (frame.getCustomersGrid().getMode()!=Consts.READONLY)
      return;
    if (rowNumber==-1)
      frame.getPricelistsGrid().clearData();
    else {
      Doc02Customers vo = (Doc02Customers)frame.getCustomersGrid().getVOListTableModel().getObjectForRow(frame.getCustomersGrid().getSelectedRow());
      frame.getPricelistsGrid().getOtherGridParams().put("customer",vo);
      frame.getPricelistsGrid().reloadData();
    }
  }


  /**
   * Callback method invoked each time the grid mode is changed.
   * @param currentMode current grid mode
   */
  public void modeChanged(int currentMode) {
	 if (frame==null)
		 return;
    if (currentMode==Consts.READONLY) {
      frame.setButtonsEnabled(true);
    }
    else {
      frame.getPricelistsGrid().setMode(Consts.READONLY);
      frame.setButtonsEnabled(false);
    }
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
    	CustomersRemote customersService = (CustomersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("CustomersBean");
    	return customersService.getCustomers(
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


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
	    try {
	    	CustomersRemote customersService = (CustomersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("CustomersBean");
	    	return customersService.insertCustomers(newValueObjects);
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
	    	for(Object vo: persistentObjects) {
		        //((Doc02Customers)vo).setDataAgg(new java.sql.Timestamp(System.currentTimeMillis()));
		        ((Doc02Customers)vo).setCodUtenteAgg("DEMO47");
	    	}
	    	
	    	CustomersRemote customersService = (CustomersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("CustomersBean");
	    	return customersService.updateCustomers(persistentObjects);
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
	    	CustomersRemote customersService = (CustomersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("CustomersBean");
	    	return customersService.deleteCustomers(persistentObjects);
	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	      return new ErrorResponse(ex.getMessage());
	    }
  }



}
