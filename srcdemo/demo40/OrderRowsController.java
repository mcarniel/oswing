package demo40;

import java.sql.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.java.*;
import java.awt.Color;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Controller for order rows grid panel.</p>
 * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
 * @version 1.0
 */
public class OrderRowsController  extends GridController implements GridDataLocator {

  private Connection conn = null;
  private OrderVO vo = null;


  public OrderRowsController(Connection conn,OrderVO vo) {
    this.conn = conn;
    this.vo = vo;
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
      String sql =
          "select ORDER_ROWS.ORDER_NUMBER,ORDER_ROWS.ORDER_YEAR,ORDER_ROWS.ROW_NUMBER,ORDER_ROWS.ITEM_ID,ORDER_ROWS.ITEM_DESCRIPTION,ORDER_ROWS.QTY,ORDER_ROWS.PRICE "+
          "from ORDER_ROWS where ORDER_ROWS.ORDER_NUMBER=? and ORDER_ROWS.ORDER_YEAR=?";

      ArrayList pars = new ArrayList();
      pars.add(vo.getOrderNumber());
      pars.add(vo.getOrderYear());

      HashMap map = new HashMap();
      map.put("orderNumber","ORDER_ROWS.ORDER_NUMBER");
      map.put("orderYear","ORDER_ROWS.ORDER_YEAR");
      map.put("rowNumber","ORDER_ROWS.ROW_NUMBER");
      map.put("itemId","ORDER_ROWS.ITEM_ID");
      map.put("itemDescription","ORDER_ROWS.ITEM_DESCRIPTION");
      map.put("qty","ORDER_ROWS.QTY");
      map.put("price","ORDER_ROWS.PRICE");

      return QueryUtil.getQuery(
        conn,
        sql,
        pars,
        map,
        valueObjectType,
        "Y",
        "N",
        new GridParams(
          action,
          startIndex,
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns,
          new HashMap()
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
   * Method used to define the background color for each cell of the grid.
   * @param rowNumber selected row index
   * @param attributeName attribute name related to the column currently selected
   * @param value object contained in the selected cell
   * @return background color of the selected cell
   */
  public Color getBackgroundColor(int row,String attributeName,Object value) {
    return new Color(199,254,214);
  }


}
