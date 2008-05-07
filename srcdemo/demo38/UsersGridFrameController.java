package demo38;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.permissions.java.CryptUtils;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class UsersGridFrameController extends GridController implements GridDataLocator {

  private UsersGridFrame grid = null;
  private Connection conn = null;

  public UsersGridFrameController(Connection conn) {
    this.conn = conn;
    grid = new UsersGridFrame(conn,this);
    MDIFrame.add(grid);
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
      String sql = "select USERS.USERNAME,USERS.PASSWORD,USERS.DESCRIPTION FROM USERS";
      Map attrs = new HashMap();
      attrs.put("username","USERS.USERNAME");
      attrs.put("password","USERS.PASSWORD");
      attrs.put("description","USERS.DESCRIPTION");
      return QueryUtil.getQuery(
      conn,
        sql,
        new ArrayList(),
        attrs,
        valueObjectType,
        "Y",
        "N",
        new GridParams(
          action,
          startIndex,
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns,
          otherGridParams
        ),
        true
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
      Map attrs = new HashMap();
      attrs.put("username", "USERNAME");
      attrs.put("password", "PASSWORD");
      attrs.put("description", "DESCRIPTION");

      UserVO vo = null;
      for(int i=0;i<newValueObjects.size();i++) {
        vo = (UserVO)newValueObjects.get(i);
        vo.setPassword( new String(CryptUtils.getInstance().encodeText(vo.getPassword())) );
      }

      return QueryUtil.insertTable(
          conn,
          newValueObjects,
          "USERS",
          attrs,
          "Y",
          "N",
          true
          );
    }
    catch (Throwable ex) {
      conn.rollback();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      conn.commit();
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
      Map attrs = new HashMap();
      attrs.put("username", "USERNAME");
      attrs.put("password", "PASSWORD");
      attrs.put("description", "DESCRIPTION");
      HashSet pk = new HashSet();
      pk.add("username");
      Response res = null;
      UserVO oldVO,newVO;
      for(int i=0;i<oldPersistentObjects.size();i++) {
        oldVO = (UserVO)oldPersistentObjects.get(i);
        newVO = (UserVO)persistentObjects.get(i);

        if (!oldVO.getPassword().equals(newVO.getPassword()))
            newVO.setPassword( new String(CryptUtils.getInstance().encodeText(newVO.getPassword())) );

        res = QueryUtil.updateTable(
          conn,
          pk,
          oldVO,
          newVO,
          "USERS",
          attrs,
          "Y",
          "N",
          true
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }
      }
      conn.commit();
      return new VOListResponse(persistentObjects,true,persistentObjects.size());
    }
    catch (Throwable ex) {
      conn.rollback();
      return new ErrorResponse(ex.getMessage());
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
      stmt = conn.prepareStatement("delete from USERS where USERNAME=?");
      for(int i=0;i<persistentObjects.size();i++) {
        UserVO vo = (UserVO)persistentObjects.get(i);
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
