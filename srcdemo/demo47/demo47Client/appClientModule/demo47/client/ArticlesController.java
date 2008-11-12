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
import demo47.java.Doc05Articles;
import demo47.server.ArticlesRemote;
import demo47.server.CustomersRemote;
import demo47.server.OrdersRemote;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for items.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ArticlesController extends GridController implements GridDataLocator {

  private ArticlesFrame frame = null;


  public ArticlesController() {
    this.frame = new ArticlesFrame(this);
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    Doc05Articles vo = (Doc05Articles)valueObject;
    //vo.setDataIns(new java.sql.Timestamp(System.currentTimeMillis()));
    vo.setCodUtenteIns("DEMO47");
    //vo.setStatus("E");
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
    	ArticlesRemote articlesRemote = (ArticlesRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("ArticlesBean");
    	return articlesRemote.getArticles(
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
	    	ArticlesRemote articlesRemote = (ArticlesRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("ArticlesBean");
	    	return articlesRemote.insertArticles(newValueObjects);
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
		        //((Doc05Articles)vo).setDataAgg(new java.sql.Timestamp(System.currentTimeMillis()));
		        ((Doc05Articles)vo).setCodUtenteAgg("DEMO47");
	    	}
	    	
	    	ArticlesRemote articlesRemote = (ArticlesRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("ArticlesBean");
	    	return articlesRemote.updateArticles(persistentObjects);
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
	    	ArticlesRemote articlesRemote = (ArticlesRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("ArticlesBean");
	    	return articlesRemote.deleteArticles(persistentObjects);
	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	      return new ErrorResponse(ex.getMessage());
	    }
  }



}
