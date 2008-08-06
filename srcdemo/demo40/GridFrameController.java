package demo40;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import java.awt.Font;
import org.openswing.swing.client.GridControl;
import java.math.BigDecimal;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;


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
        ArrayList rows = ((VOListResponse)res).getRows();
        OrderVO vo = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        try {
          pstmt = conn.prepareStatement("select count(*) from ORDER_ROWS where ORDER_NUMBER=? and ORDER_YEAR=?");
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




}
