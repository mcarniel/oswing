package demo36;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.message.receive.java.*;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.DataObject;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.ObjectId;
import org.openswing.swing.util.server.CayenneUtils;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for tasks.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TaskGridFrameController extends GridController implements GridDataLocator {

  private TaskGridFrame grid = null;
  private DataContext context = null;

  public TaskGridFrameController(DataContext context) {
    this.context = context;
    grid = new TaskGridFrame(this);
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

      return CayenneUtils.getBlockFromQuery(
          action,
          startIndex,
          25,
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns,
          new SelectQuery(TasksVO.class),
          new HashMap(),
          context
      );

//      READ WHOLE RESULT-SET...
//      Response res = HibernateUtils.getAllFromQuery(
//        filteredColumns,
//        currentSortedColumns,
//        currentSortedVersusColumns,
//        valueObjectType,
//        baseSQL,
//        new Object[0],
//        new Type[0],
//        "TASKS",
//        sessions,
//        sess
//      );
//      sess.close();
//      return res;
//      END READ WHOLE RESULT-SET...


//    READ A BLOCK OF DATA FROM RESULT-SET...
//      Response res = HibernateUtils.getBlockFromQuery(
//        action,
//        startIndex,
//        50, // block size...
//        filteredColumns,
//        currentSortedColumns,
//        currentSortedVersusColumns,
//        valueObjectType,
//        baseSQL,
//        new Object[0],
//        new Type[0],
//        "TASKS",
//        sessions,
//        sess
//      );
//      sess.close();
//      return res;
//    END READ A BLOCK OF DATA FROM RESULT-SET...
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
      DataObject vo = null;
      for(int i=0;i<newValueObjects.size();i++) {
        vo = (DataObject) newValueObjects.get(i);
        context.registerNewObject(vo);
      }
      context.commitChanges();
      return new VOListResponse(newValueObjects,false,newValueObjects.size());
    }
    catch (Exception ex) {
      try {
        context.rollbackChanges();
      }
      catch (Exception ex1) {
      }
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    try {
      context.commitChanges();
      return new VOListResponse(persistentObjects,false,persistentObjects.size());
    }
    catch (Exception ex) {
      try {
        context.rollbackChanges();
      }
      catch (Exception ex1) {
      }
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
      context.deleteObjects(persistentObjects);
      context.commitChanges();
      return new VOResponse(new Boolean(true));
    }
    catch (Exception ex) {
      try {
        context.rollbackChanges();
      }
      catch (Exception ex1) {
      }
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }



}
