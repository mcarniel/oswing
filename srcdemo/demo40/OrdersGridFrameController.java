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
 * <p>Description: Grid controller.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class OrdersGridFrameController extends GridController implements GridDataLocator {

  private OrdersGridFrame grid = null;
  private Connection conn = null;

  public OrdersGridFrameController(Connection conn) {
    this.conn = conn;
    grid = new OrdersGridFrame(conn,this);
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
      String sql = "select ORDERS.ORDER_NUMBER,ORDERS.ORDER_YEAR,ORDERS.ORDER_DATE,ORDERS.CUSTOMER_ID,ORDERS.TOTAL from ORDERS";
      HashMap map = new HashMap();
      map.put("orderNumber","ORDERS.ORDER_NUMBER");
      map.put("orderYear","ORDERS.ORDER_YEAR");
      map.put("orderDate","ORDERS.ORDER_DATE");
      map.put("customerId","ORDERS.CUSTOMER_ID");
      map.put("total","ORDERS.TOTAL");

      Response res = QueryUtil.getQuery(
        conn,
        sql,
        new ArrayList(),
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
        25,
        true
      );
      if (!res.isError()) {
        List rows = ((VOListResponse)res).getRows();
        OrderVO vo = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        try {
          pstmt = conn.prepareStatement("select * from ORDER_ROWS where ORDER_NUMBER=? and ORDER_YEAR=?");
          for (int i = 0; i < rows.size(); i++) {
            vo = (OrderVO) rows.get(i);
            pstmt.setBigDecimal(1,vo.getOrderNumber());
            pstmt.setBigDecimal(2,vo.getOrderYear());
            rset = pstmt.executeQuery();
            vo.setHasOrderRows( rset.next() );
            rset.close();
          }
        }
        catch (Exception ex1) {
          ex1.printStackTrace();
        }
        finally {
          try {
            rset.close();
          }
          catch (Exception ex2) {
          }
          try {
            pstmt.close();
          }
          catch (Exception ex2) {
          }
        }
      }

      return res;
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
    return new Color(255,222,200);
  }



}
