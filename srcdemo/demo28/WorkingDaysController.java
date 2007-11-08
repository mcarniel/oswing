package demo28;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.server.QueryUtil;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for working days of the employee.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class WorkingDaysController extends GridController implements GridDataLocator {

  private Connection conn = null;

  public WorkingDaysController(Connection conn) {
    this.conn = conn;
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
      String sql = "select WORKING_DAYS.EMP_CODE,WORKING_DAYS.DAY,WORKING_DAYS.START_MORNING_HOUR,WORKING_DAYS.END_MORNING_HOUR,WORKING_DAYS.START_AFTERNOON_HOUR,WORKING_DAYS.END_AFTERNOON_HOUR from WORKING_DAYS where WORKING_DAYS.EMP_CODE=?";

      // mapping between attributes and database fields...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("empCode","WORKING_DAYS.EMP_CODE");
      attribute2dbField.put("day","WORKING_DAYS.DAY");
      attribute2dbField.put("startMorningHour","WORKING_DAYS.START_MORNING_HOUR");
      attribute2dbField.put("endMorningHour","WORKING_DAYS.END_MORNING_HOUR");
      attribute2dbField.put("startAfternoonHour","WORKING_DAYS.START_AFTERNOON_HOUR");
      attribute2dbField.put("endAfternoonHour","WORKING_DAYS.END_AFTERNOON_HOUR");

      ArrayList vars = new ArrayList();
      vars.add( otherGridParams.get("empCode") );

      return QueryUtil.getQuery(
        conn,
        sql,
        vars, // list of values linked to "?" parameters in sql
        attribute2dbField,
        WorkingDayVO.class, // v.o. to dinamically create for each row...
        "Y",
        "N",
        new GridParams(
          action,
          startIndex,
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns,
          new HashMap() // other params...
        ),
        true // log query...
      );
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
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    // mapping between attributes and database fields...
    Map attribute2dbField = new HashMap();
    attribute2dbField.put("empCode","EMP_CODE");
    attribute2dbField.put("day","DAY");
    attribute2dbField.put("startMorningHour","START_MORNING_HOUR");
    attribute2dbField.put("endMorningHour","END_MORNING_HOUR");
    attribute2dbField.put("startAfternoonHour","START_AFTERNOON_HOUR");
    attribute2dbField.put("endAfternoonHour","END_AFTERNOON_HOUR");

    HashSet pk = new HashSet();
    pk.add("empCode");
    pk.add("day");

    Response res = null;
    WorkingDayVO oldVO = null;
    WorkingDayVO newVO = null;

    Calendar cal = Calendar.getInstance();

    for(int i=0;i<persistentObjects.size();i++) {
      oldVO = (WorkingDayVO)oldPersistentObjects.get(i);
      newVO = (WorkingDayVO)persistentObjects.get(i);

      res = QueryUtil.updateTable(conn,pk,oldVO,newVO,"WORKING_DAYS",attribute2dbField,"Y","N",true);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
    }
    conn.commit();
    return new VOListResponse(persistentObjects,false,persistentObjects.size());
  }


}
