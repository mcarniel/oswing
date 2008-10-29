package demo45;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for users.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class UsersController extends GridController implements GridDataLocator {

  private Connection conn = null;
  private UserRolesFrame frame = null;


  public UsersController(Connection conn,UserRolesFrame frame) {
    this.conn = conn;
    this.frame = frame;
  }


  /**
   * Callback method invoked when the user has selected another row.
   * @param rowNumber selected row index
   */
  public void rowChanged(int rowNumber) {
    if (frame.getUsersGrid().getMode()!=Consts.READONLY)
      return;
    if (rowNumber==-1)
      frame.getUserRolesGrid().clearData();
    else {
      UserVO vo = (UserVO)frame.getUsersGrid().getVOListTableModel().getObjectForRow(frame.getUsersGrid().getSelectedRow());
      frame.getUserRolesGrid().getOtherGridParams().put("username",vo.getUsername());
      frame.getUserRolesGrid().reloadData();
    }
  }


  /**
   * Callback method invoked each time the grid mode is changed.
   * @param currentMode current grid mode
   */
  public void modeChanged(int currentMode) {
    if (currentMode==Consts.READONLY) {
      frame.setButtonsEnabled(true);
    }
    else {
      frame.getUserRolesGrid().setMode(Consts.READONLY);
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
      String sql = "select USERS.USERNAME,USERS.PASSWORD,USERS.DESCRIPTION from USERS";

      // mapping between attributes and database fields...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("username","USERS.USERNAME");
      attribute2dbField.put("description","USERS.DESCRIPTION");
      attribute2dbField.put("password","USERS.PASSWORD");

      return QueryUtil.getQuery(
        conn,
        sql,
        new ArrayList(), // list of values linked to "?" parameters in sql
        attribute2dbField,
        UserVO.class, // v.o. to dinamically create for each row...
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
        50, // pagination size...
        true // log query...
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
    // mapping between attributes and database fields...
    Map attribute2dbField = new HashMap();
    attribute2dbField.put("username","USERNAME");
    attribute2dbField.put("description","DESCRIPTION");
    attribute2dbField.put("password","PASSWORD");

    Response res = QueryUtil.insertTable(conn,newValueObjects,"USERS",attribute2dbField,"Y","N",true);
    if (res.isError())
      conn.rollback();
    else
      conn.commit();
    return res;
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
    attribute2dbField.put("username","USERNAME");
    attribute2dbField.put("description","DESCRIPTION");
    attribute2dbField.put("password","PASSWORD");

    HashSet pk = new HashSet();
    pk.add("username");

    Response res = null;
    UserVO oldVO = null;
    UserVO newVO = null;
    for(int i=0;i<persistentObjects.size();i++) {
      oldVO = (UserVO)oldPersistentObjects.get(i);
      newVO = (UserVO)persistentObjects.get(i);
      res = QueryUtil.updateTable(conn,pk,oldVO,newVO,"USERS",attribute2dbField,"Y","N",true);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
    }
    conn.commit();
    return new VOListResponse(persistentObjects,false,persistentObjects.size());
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from USERS where USERNAME=?");
      UserVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (UserVO)persistentObjects.get(i);
        stmt.setString(1,vo.getUsername());
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
