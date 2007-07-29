package demo20;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import java.awt.Font;
import org.openswing.swing.client.GridControl;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;


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

      ArrayList vals = new ArrayList();
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("stringValue","DEMO20.TEXT");
      attribute2dbField.put("numericValue","DEMO20.DECNUM");
      attribute2dbField.put("currencyValue","DEMO20.CURRNUM");
      attribute2dbField.put("dateValue","DEMO20.THEDATE");
      attribute2dbField.put("comboValue","DEMO20.COMBO");
      attribute2dbField.put("checkValue","DEMO20.CHECK_BOX");
      attribute2dbField.put("radioButtonValue","DEMO20.RADIO");
      attribute2dbField.put("lookupValue","DEMO20.CODE");
      attribute2dbField.put("descrLookupValue","DEMO20_LOOKUP.DESCRCODE");
      attribute2dbField.put("formattedTextValue","DEMO20.FORMATTED_TEXT");
      attribute2dbField.put("intValue","DEMO20.INT_VALUE");

      GridParams gridParams = new GridParams(action,startIndex,filteredColumns,currentSortedColumns,currentSortedVersusColumns,otherGridParams);

      Response res = QueryUtil.getQuery(
        conn,
        new UserSessionParameters(),
        "select DEMO20.TEXT,DEMO20.DECNUM,DEMO20.CURRNUM,DEMO20.THEDATE,DEMO20.COMBO,DEMO20.CHECK_BOX,DEMO20.RADIO,DEMO20.CODE,DEMO20_LOOKUP.DESCRCODE,DEMO20.FORMATTED_TEXT,DEMO20.INT_VALUE from DEMO20,DEMO20_LOOKUP where DEMO20.CODE=DEMO20_LOOKUP.CODE",
        vals,
        attribute2dbField,
        TestVO.class,
        "Y",
        "N",
        null,
        gridParams,
        50,
        true
      );


      return res;
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

    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("insert into DEMO20(TEXT,DECNUM,CURRNUM,THEDATE,COMBO,CHECK_BOX,RADIO,CODE,FORMATTED_TEXT,INT_VALUE) values(?,?,?,?,?,?,?,?,?,?)");
      TestVO vo = (TestVO)newValueObjects.get(0);
      stmt.setObject(6,vo.getCheckValue()==null || !vo.getCheckValue().booleanValue() ? "N":"Y");
      stmt.setString(5,vo.getComboValue());
      stmt.setBigDecimal(20,vo.getCurrencyValue());
      stmt.setDate(4,vo.getDateValue());
      stmt.setBigDecimal(2,vo.getNumericValue());
      stmt.setObject(7,vo.getRadioButtonValue()==null || !vo.getRadioButtonValue().booleanValue() ? "N":"Y");
      stmt.setString(1,vo.getStringValue());
      stmt.setString(8,vo.getLookupValue());
      stmt.setString(9,vo.getFormattedTextValue());
      stmt.setBigDecimal(10,vo.getIntValue()==null?null:new BigDecimal(vo.getIntValue().intValue()));
      stmt.execute();
      return new VOListResponse(newValueObjects,false,newValueObjects.size());
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


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("update DEMO20 set TEXT=?,DECNUM=?,CURRNUM=?,THEDATE=?,COMBO=?,CHECK_BOX=?,RADIO=?,CODE=?,FORMATTED_TEXT=?,INT_VALUE=? where TEXT=?");
      TestVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (TestVO)persistentObjects.get(i);
        stmt.setObject(6,vo.getCheckValue()==null || !vo.getCheckValue().booleanValue() ? "N":"Y");
        stmt.setString(5,vo.getComboValue());
        stmt.setBigDecimal(20,vo.getCurrencyValue());
        stmt.setDate(4,vo.getDateValue());
        stmt.setBigDecimal(2,vo.getNumericValue());
        stmt.setObject(7,vo.getRadioButtonValue()==null || !vo.getRadioButtonValue().booleanValue() ? "N":"Y");
        stmt.setString(1,vo.getStringValue());
        stmt.setString(8,vo.getLookupValue());
        stmt.setString(9,vo.getFormattedTextValue());
        stmt.setBigDecimal(10,vo.getIntValue()==null?null:new BigDecimal(vo.getIntValue().intValue()));
        stmt.setString(11,vo.getStringValue());
        stmt.execute();
      }
      return new VOListResponse(persistentObjects,false,persistentObjects.size());
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


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from DEMO20 where TEXT=?");
      for(int i=0;i<persistentObjects.size();i++) {
        TestVO vo = (TestVO)persistentObjects.get(i);
        stmt.setString(1,vo.getStringValue());
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


  /**
   * Callback method invoked each time a cell is edited: this method define if the new value if valid.
   * This method is invoked ONLY if:
   * - the edited value is not equals to the old value OR it has exmplicitely called setCellAt or setValueAt
   * - the cell is editable
   * Default behaviour: cell value is valid.
   * @param rowNumber selected row index
   * @param attributeName attribute name related to the column currently selected
   * @param oldValue old cell value (before cell editing)
   * @param newValue new cell value (just edited)
   * @return <code>true</code> if cell value is valid, <code>false</code> otherwise
   */
  public boolean validateCell(int rowNumber,String attributeName,Object oldValue,Object newValue) {
    // this method has been overrrided to listen for numeric/currency cell changes:
    // in this case it will be invoked getTotals method to refresh bottom grid content...
    if (attributeName.equals("currencyValue") || attributeName.equals("numericValue")) {
      grid.getGrid().getBottomTable().reload();
    }
    return true;
  }



}
