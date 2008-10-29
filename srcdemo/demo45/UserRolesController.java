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


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for user roles.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class UserRolesController extends GridController implements GridDataLocator {

  private Connection conn = null;
  private UserRolesFrame frame = null;


  public UserRolesController(Connection conn) {
    this.conn = conn;
    this.frame = new UserRolesFrame(conn,this);
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    UserRoleVO vo = (UserRoleVO)valueObject;
    vo.setUsername((String)frame.getUserRolesGrid().getOtherGridParams().get("username"));
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
      String sql = "select USER_ROLES.USERNAME,USER_ROLES.ROLE_ID from USER_ROLES where USER_ROLES.USERNAME=?";

      ArrayList values = new ArrayList(); // list of values linked to "?" parameters in sql
      values.add(otherGridParams.get("username"));

      // mapping between attributes and database fields...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("username","USER_ROLES.USERNAME");
      attribute2dbField.put("roleId","USER_ROLES.ROLE_ID");

      return QueryUtil.getQuery(
        conn,
        sql,
        values,
        attribute2dbField,
        UserRoleVO.class, // v.o. to dinamically create for each row...
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
    attribute2dbField.put("roleId","ROLE_ID");

    Response res = QueryUtil.insertTable(conn,newValueObjects,"USER_ROLES",attribute2dbField,"Y","N",true);
    if (res.isError())
      conn.rollback();
    else
      conn.commit();
    return res;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from USER_ROLES where USERNAME=? and ROLE_ID=?");
      UserRoleVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (UserRoleVO)persistentObjects.get(i);
        stmt.setString(1,vo.getUsername());
        stmt.setString(2,vo.getRoleId());
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
