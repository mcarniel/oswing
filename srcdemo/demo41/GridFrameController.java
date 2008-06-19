package demo41;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.domains.java.Domain;
import java.math.BigDecimal;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridFrameController extends GridController implements GridDataLocator {

  private GridFrame grid = null;
  private Connection conn = null;

  public GridFrameController(Connection conn) {
    this.conn = conn;
    grid = new GridFrame(conn,this);
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
//    PreparedStatement stmt = null;
    try {
      ArrayList vals = new ArrayList();
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("id","PRODUCTS.ID");
      attribute2dbField.put("pname","PRODUCTS.PNAME");
      attribute2dbField.put("units.id","PRODUCTS.ID_UNIT");
      attribute2dbField.put("units.unitName","UNITS.UNIT_NAME");
      GridParams gridParams = new GridParams(action,startIndex,filteredColumns,currentSortedColumns,currentSortedVersusColumns,otherGridParams);

      return QueryUtil.getQuery(
        conn,
        new UserSessionParameters(),
        "select PRODUCTS.ID,PRODUCTS.PNAME,PRODUCTS.ID_UNIT,UNITS.UNIT_NAME from PRODUCTS,UNITS where PRODUCTS.ID_UNIT=UNITS.ID ",
        vals,
        attribute2dbField,
        Products.class,
        "Y",
        "N",
        null,
        gridParams,
        50,
        true
      );

    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
//    finally {
//      try {
//        stmt.close();
//      }
//      catch (SQLException ex1) {
//      }
//    }

  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try {
      stmt = conn.prepareStatement("select max(ID) from PRODUCTS");
      rset = stmt.executeQuery();
      BigDecimal id = new BigDecimal(1);
      if (rset.next())
        id = rset.getBigDecimal(1).add(new BigDecimal(1));

      Products vo = (Products)newValueObjects.get(0);
      vo.setId(id);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("id","ID");
      attribute2dbField.put("pname","PNAME");
      attribute2dbField.put("units.id","ID_UNIT");

      Response res = QueryUtil.insertTable(
        conn,
        new UserSessionParameters(),
        vo,
        "PRODUCTS",
        attribute2dbField,
        "Y",
        "N",
        null,
        true
      );

      if (res.isError())
        return res;

      return new VOListResponse(newValueObjects,false,newValueObjects.size());

//      return QueryUtil.insertTable(
//        conn,
//        new UserSessionParameters(),
//        newValueObjects,
//        "PRODUCTS",
//        attribute2dbField,
//        "Y",
//        "N",
//        null,
//        true
//      );

    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        rset.close();
      }
      catch (Exception ex1) {
      }
      try {
        stmt.close();
      }
      catch (Exception ex1) {
      }
      try {
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
    try {
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("id","ID");
      attribute2dbField.put("pname","PNAME");
      attribute2dbField.put("units.id","ID_UNIT");

      HashSet pks = new HashSet();
      pks.add("id");

      ValueObject oldVO = null;
      ValueObject newVO = null;
      Response res = null;
      for(int i=0;i<oldPersistentObjects.size();i++) {
        oldVO = (ValueObject)oldPersistentObjects.get(i);
        newVO = (ValueObject)persistentObjects.get(i);

        res = QueryUtil.updateTable(
          conn,
          new UserSessionParameters(),
          pks,
          oldVO,
          newVO,
          "PRODUCTS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
        );
        if (res.isError())
          return res;
      }
      return new VOListResponse(persistentObjects,false,persistentObjects.size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
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
      stmt = conn.prepareStatement("delete from PRODUCTS where ID=?");
      for(int i=0;i<persistentObjects.size();i++) {
        Products vo = (Products)persistentObjects.get(i);
        stmt.setBigDecimal(1,vo.getId());
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
