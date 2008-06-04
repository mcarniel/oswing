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
public class OrderRowDiscountsController  extends GridController implements GridDataLocator {

  private Connection conn = null;
  private OrderRowVO vo = null;


  public OrderRowDiscountsController(Connection conn,OrderRowVO vo) {
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
          "select ORDER_ROW_DISCOUNTS.ORDER_NUMBER,ORDER_ROW_DISCOUNTS.ORDER_YEAR,ORDER_ROW_DISCOUNTS.ROW_NUMBER,ORDER_ROW_DISCOUNTS.DISCOUNT_CODE,ORDER_ROW_DISCOUNTS.DISCOUNT_DESCRIPTION,ORDER_ROW_DISCOUNTS.DISCOUNT_VALUE "+
          "from ORDER_ROW_DISCOUNTS where ORDER_ROW_DISCOUNTS.ORDER_NUMBER=? and ORDER_ROW_DISCOUNTS.ORDER_YEAR=? and ORDER_ROW_DISCOUNTS.ROW_NUMBER=?";

      ArrayList pars = new ArrayList();
      pars.add(vo.getOrderNumber());
      pars.add(vo.getOrderYear());
      pars.add(vo.getRowNumber());

      HashMap map = new HashMap();
      map.put("orderNumber","ORDER_ROW_DISCOUNTS.ORDER_NUMBER");
      map.put("orderYear","ORDER_ROW_DISCOUNTS.ORDER_YEAR");
      map.put("rowNumber","ORDER_ROW_DISCOUNTS.ROW_NUMBER");
      map.put("discountCode","ORDER_ROW_DISCOUNTS.DISCOUNT_CODE");
      map.put("discountDescription","ORDER_ROW_DISCOUNTS.DISCOUNT_DESCRIPTION");
      map.put("discountValue","ORDER_ROW_DISCOUNTS.DISCOUNT_VALUE");

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
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    OrderRowDiscountVO dVO = (OrderRowDiscountVO)valueObject;
    dVO.setOrderYear(vo.getOrderYear());
    dVO.setOrderNumber(vo.getOrderNumber());
    dVO.setRowNumber(vo.getRowNumber());
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    try {
      HashMap map = new HashMap();
      map.put("orderNumber","ORDER_NUMBER");
      map.put("orderYear","ORDER_YEAR");
      map.put("rowNumber","ROW_NUMBER");
      map.put("discountCode","DISCOUNT_CODE");
      map.put("discountDescription","DISCOUNT_DESCRIPTION");
      map.put("discountValue","DISCOUNT_VALUE");

      return QueryUtil.insertTable(
        conn,
        newValueObjects,
        "ORDER_ROW_DISCOUNTS",
        map,
        "Y",
        "N",
        true
      );
    }
    catch (Exception ex) {
      try {
        conn.rollback();
      }
      catch (Exception exx) {

      }
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        conn.commit();
      }
      catch (Exception exx) {

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
      HashMap map = new HashMap();
      map.put("orderNumber","ORDER_NUMBER");
      map.put("orderYear","ORDER_YEAR");
      map.put("rowNumber","ROW_NUMBER");
      map.put("discountCode","DISCOUNT_CODE");
      map.put("discountDescription","DISCOUNT_DESCRIPTION");
      map.put("discountValue","DISCOUNT_VALUE");

      HashSet pk = new HashSet();
      pk.add("orderNumber");
      pk.add("orderYear");
      pk.add("rowNumber");
      pk.add("discountCode");

      OrderRowDiscountVO oldVO,newVO;
      Response res = null;
      for(int i=0;i<oldPersistentObjects.size();i++) {
        oldVO = (OrderRowDiscountVO)oldPersistentObjects.get(i);
        newVO = (OrderRowDiscountVO)persistentObjects.get(i);
        res = QueryUtil.updateTable(
              conn,
              pk,
              oldVO,
              newVO,
              "ORDER_ROW_DISCOUNTS",
              map,
              "Y",
              "N",
              true
        );
        if (res.isError()) {
          try {
            conn.rollback();
          }
          catch (Exception exx) {

          }
          return res;
        }
      }
      return new VOListResponse(persistentObjects,false,persistentObjects.size());
    }
    catch (Exception ex) {
      try {
        conn.rollback();
      }
      catch (Exception exx) {

      }
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        conn.commit();
      }
      catch (Exception exx) {

      }
    }
  }


  private void updateTotals() {

  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement("delete from ORDER_ROW_DISCOUNTS where ORDER_ROW_DISCOUNTS.ORDER_NUMBER=? and ORDER_ROW_DISCOUNTS.ORDER_YEAR=? and ROW_NUMBER=? and DISCOUNT_CODE=?");

      OrderRowDiscountVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (OrderRowDiscountVO)persistentObjects.get(i);
        pstmt.setObject(1,vo.getOrderNumber());
        pstmt.setObject(2,vo.getOrderYear());
        pstmt.setObject(3,vo.getRowNumber());
        pstmt.setObject(4,vo.getDiscountCode());
        int row = pstmt.executeUpdate();
      }
      return new VOResponse(Boolean.TRUE);
    }
    catch (Exception ex) {
      try {
        conn.rollback();
      }
      catch (Exception exx) {

      }
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception exx) {

      }
      try {
        conn.commit();
      }
      catch (Exception exx) {

      }
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
    return new Color(255,249,178);
  }


}
