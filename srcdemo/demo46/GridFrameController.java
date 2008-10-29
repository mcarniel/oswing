package demo46;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.util.client.ClientUtils;
import javax.swing.ImageIcon;
import java.awt.Color;
import org.openswing.swing.customvo.client.CustomGridControlController;
import org.openswing.swing.customvo.server.CustomValueObjectUtils;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridFrameController extends CustomGridControlController implements GridDataLocator {

  private Connection conn = null;
  private GridFrame frame = null;


  public GridFrameController(Connection conn,ArrayList customFields,GridFrame frame) {
    super(customFields);
    this.conn = conn;
    this.frame = frame;
  }


  /**
   * Method invoked by the grid to load a block or rows.
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
      return CustomValueObjectUtils.getQuery(
          conn,
          frame.getSQL(),
          new ArrayList(),
          action,
          startIndex,
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns,
          customFields,
          50, // blockSize
          true // log query
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
    HashMap attribute2dbField = getAttributesMappingPerTable("ORDERS");
    try {
      return QueryUtil.insertTable(
          conn,
          newValueObjects,
          "ORDERS",
          attribute2dbField,
          "Y",
          "N",
          true
      );
    }
    catch (Exception ex) {
      try { conn.rollback(); } catch (Exception ee) {}
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        conn.commit();
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed/new rows
   * @param oldPersistentObjects old value objects, previous the changes; it can contains null objects, in case of new inserted rows
   * @param persistentObjects value objects related to the changed/new rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    try {
      HashMap attribute2dbField = getAttributesMappingPerTable("ORDERS");
      HashSet pkAttrs = getPrimaryKeyPerTable("ORDERS");
      for(int i=0;i<persistentObjects.size();i++)
         QueryUtil.updateTable(
            conn,
            pkAttrs,
            (ValueObject)oldPersistentObjects.get(i),
            (ValueObject)persistentObjects.get(i),
            "ORDERS",
            attribute2dbField,
            "Y",
            "N",
            true
        );
      return new VOListResponse(persistentObjects,false,persistentObjects.size());
    }
    catch (Exception ex) {
      try { conn.rollback(); } catch (Exception ee) {}
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        conn.commit();
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    PreparedStatement pstmt = null;
    try {
      HashMap attribute2dbField = getAttributesMappingPerTable("ORDERS");
      HashSet pkAttrs = getPrimaryKeyPerTable("ORDERS");
      String sql = "delete from ORDERS where ";
      ArrayList values = new ArrayList();
      ValueObject obj = null;
      for(int j=0;j<persistentObjects.size();j++) {
        obj = (ValueObject)persistentObjects.get(j);
        values.clear();
        Iterator it = pkAttrs.iterator();
        String attr = null;
        while(it.hasNext()) {
          attr = it.next().toString();
          sql += attribute2dbField.get(attr)+"=? and ";
          values.add(frame.getGrid().getVOListTableModel().getField(frame.getGrid().getVOListTableModel().getRowForObject(obj),attr));
        }
        sql = sql.substring(0,sql.length()-4);
        pstmt = conn.prepareStatement(sql);
        for(int i=0;i<values.size();i++)
          pstmt.setObject(i+1,values.get(i));
        pstmt.execute();
      }
      return new VOResponse(Boolean.TRUE);
    }
    catch (Exception ex) {
      try { conn.rollback(); } catch (Exception ee) {}
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex1) {
      }
      try {
        conn.commit();
      }
      catch (Exception ex1) {
      }
    }
  }



}
