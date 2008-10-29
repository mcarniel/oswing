package demo45;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for departments.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GroupSalesFrameController extends GridController implements GridDataLocator {

  private GroupSalesGridFrame grid = null;
  private Connection conn = null;

  public GroupSalesFrameController(Connection conn) {
    this.conn = conn;
    grid = new GroupSalesGridFrame(conn,this);
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
      String sql = "select GROUP_SALES.SALE_DATE,GROUP_SALES.AREA,GROUP_SALES.TOTAL_AMOUNT,GROUP_SALES.SALES_NUMBER,GROUP_SALES.NOTE from GROUP_SALES";

      // mapping between attributes and database fields...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("saleDate","GROUP_SALES.SALE_DATE");
      attribute2dbField.put("area","GROUP_SALES.AREA");
      attribute2dbField.put("totalAmount","GROUP_SALES.TOTAL_AMOUNT");
      attribute2dbField.put("salesNumber","GROUP_SALES.SALES_NUMBER");
      attribute2dbField.put("note","GROUP_SALES.NOTE");

      return QueryUtil.getQuery(
        conn,
        sql,
        new ArrayList(), // list of values linked to "?" parameters in sql
        attribute2dbField,
        GroupSalesVO.class, // v.o. to dinamically create for each row...
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
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    // mapping between attributes and database fields...
    Map attribute2dbField = new HashMap();
    attribute2dbField.put("saleDate","SALE_DATE");
    attribute2dbField.put("area","AREA");
    attribute2dbField.put("totalAmount","TOTAL_AMOUNT");
    attribute2dbField.put("salesNumber","SALES_NUMBER");
    attribute2dbField.put("note","NOTE");

    HashSet pk = new HashSet();
    pk.add("saleDate");

    Response res = null;
    GroupSalesVO oldVO = null;
    GroupSalesVO newVO = null;
    for(int i=0;i<persistentObjects.size();i++) {
      oldVO = (GroupSalesVO)oldPersistentObjects.get(i);
      newVO = (GroupSalesVO)persistentObjects.get(i);
      res = QueryUtil.updateTable(conn,pk,oldVO,newVO,"GROUP_SALES",attribute2dbField,"Y","N",true);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
    }
    conn.commit();
    return new VOListResponse(persistentObjects,false,persistentObjects.size());
  }




}
