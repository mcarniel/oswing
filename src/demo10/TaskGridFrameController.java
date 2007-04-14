package demo10;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;


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
  private Connection conn = null;

  public TaskGridFrameController(Connection conn) {
    this.conn = conn;
    grid = new TaskGridFrame(conn,this);
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
    PreparedStatement stmt = null;
    try {
      String sql = "select TASKS.TASK_CODE,TASKS.DESCRIPTION from TASKS where TASKS.STATUS='E'";
      Vector vals = new Vector();
      if (filteredColumns.size()>0) {
        FilterWhereClause[] filter = (FilterWhereClause[])filteredColumns.get("taskCode");
        sql += " and TASKS.TASK_CODE "+ filter[0].getOperator()+"?";
        vals.add(filter[0].getValue());
        if (filter[1]!=null) {
          sql += " and TASKS.TASK_CODE "+ filter[1].getOperator()+"?";
          vals.add(filter[1].getValue());
        }
      }
      if (currentSortedColumns.size()>0) {
        sql += " ORDER BY TASKS.TASK_CODE "+currentSortedVersusColumns.get(0);
      }

      stmt = conn.prepareStatement(sql);
      for(int i=0;i<vals.size();i++)
        stmt.setObject(i+1,vals.get(i));

      ResultSet rset = stmt.executeQuery();


      ArrayList list = new ArrayList();
      TaskVO vo = null;
      while (rset.next()) {
        vo = new TaskVO();
        vo.setTaskCode(rset.getString(1));
        vo.setDescription(rset.getString(2));
        list.add(vo);
      }
      return new VOListResponse(list,false,list.size());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
      }
      catch (SQLException ex1) {
      }
    }

  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {

    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("insert into TASKS(TASK_CODE,DESCRIPTION,STATUS) values(?,?,?)");
      TaskVO vo = (TaskVO)newValueObjects.get(0);
      stmt.setString(1,vo.getTaskCode());
      stmt.setString(2,vo.getDescription());
      stmt.setString(3,"E");
      stmt.execute();
      return new VOListResponse(newValueObjects,false,newValueObjects.size());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
        conn.commit();
      }
      catch (SQLException ex1) {
      }
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
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("update TASKS set TASK_CODE=?,DESCRIPTION=? where TASK_CODE=?");
      TaskVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (TaskVO)persistentObjects.get(i);
        stmt.setString(1,vo.getTaskCode());
        stmt.setString(2,vo.getDescription());
        stmt.setString(3,vo.getTaskCode());
        stmt.execute();
      }
      return new VOListResponse(persistentObjects,false,persistentObjects.size());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
        conn.commit();
      }
      catch (SQLException ex1) {
      }
    }

  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("update TASKS set STATUS='D' where TASK_CODE=?");
      TaskVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (TaskVO)persistentObjects.get(i);
        stmt.setString(1,vo.getTaskCode());
        stmt.execute();
      }
      return new VOResponse(new Boolean(true));
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
        conn.commit();
      }
      catch (SQLException ex1) {
      }
    }

  }



}
