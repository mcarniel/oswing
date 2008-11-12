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

import demo47.java.Doc02Customers;
import demo47.java.Doc03Pricelists;
import demo47.java.Doc03PricelistsPK;
import demo47.server.CustomersRemote;
import demo47.server.PricelistsRemote;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for users.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class PricelistsController extends GridController implements GridDataLocator {

  private CustomersFrame frame = null;


  public PricelistsController(CustomersFrame frame) {
    this.frame = frame;
  }

  
  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
	Doc03Pricelists vo = (Doc03Pricelists)valueObject;
    //vo.setDataIns(new java.sql.Timestamp(System.currentTimeMillis()));
    vo.setCodUtenteIns("DEMO47");
    //vo.setStatus("E");
    Doc02Customers parentVO = (Doc02Customers)frame.getPricelistsGrid().getOtherGridParams().get("customer");
    vo.setCustomerIdDoc02(parentVO);
    Doc03PricelistsPK pk = new Doc03PricelistsPK(); 
    pk.setCustomerIdDoc022(parentVO.getCustomerId());
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
    	PricelistsRemote pricelistsService = (PricelistsRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("PricelistsBean");
    	return pricelistsService.getPricelists(
    	  (Doc02Customers)otherGridParams.get("customer"),
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
	    	PricelistsRemote pricelistsService = (PricelistsRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("PricelistsBean");
	    	return pricelistsService.insertPricelists(newValueObjects);
	    }
	    catch (Exception ex) {
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
	    	PricelistsRemote pricelistsService = (PricelistsRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("PricelistsBean");
	    	return pricelistsService.updatePricelists(persistentObjects);
	    }
	    catch (Exception ex) {
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
	    	PricelistsRemote pricelistsService = (PricelistsRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("PricelistsBean");
	    	return pricelistsService.deletePricelists(persistentObjects);
	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	      return new ErrorResponse(ex.getMessage());
	    }
  }

  



}
