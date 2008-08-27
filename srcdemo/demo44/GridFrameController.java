package demo44;

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
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.export.java.ExportOptions;


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
      String sql = "select DEMO44.TEXT,DEMO44.DECNUM,DEMO44.CURRNUM,DEMO44.THEDATE,DEMO44.COMBO,DEMO44.CHECK_BOX,DEMO44.RADIO,DEMO44.CODE,DEMO44_LOOKUP.DESCRCODE,DEMO44.INTNUM from DEMO44,DEMO44_LOOKUP where DEMO44.CODE=DEMO44_LOOKUP.CODE";

      // mapping between attributes and database fields...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("checkValue","DEMO44.CHECK_BOX");
      attribute2dbField.put("comboValue","DEMO44.COMBO");
      attribute2dbField.put("currencyValue","DEMO44.CURRNUM");
      attribute2dbField.put("dateValue","DEMO44.THEDATE");
      attribute2dbField.put("numericValue","DEMO44.DECNUM");
      attribute2dbField.put("radioButtonValue","DEMO44.RADIO");
      attribute2dbField.put("stringValue","DEMO44.TEXT");
      attribute2dbField.put("lookupValue","DEMO44.CODE");
      attribute2dbField.put("descrLookupValue","DEMO44_LOOKUP.DESCRCODE");
      attribute2dbField.put("intValue","DEMO44.INTNUM");

      Response res = QueryUtil.getQuery(
        conn,
        sql,
        new ArrayList(), // list of values linked to "?" parameters in sql
        attribute2dbField,
        TestVO.class, // v.o. to dinamically create for each row...
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
        25, // pagination size...
        true // log query...
      );
      if (!res.isError()) {
        ArrayList rows = ((VOListResponse)res).getRows();
        TestVO vo = null;
        for(int i=0;i<rows.size();i++) {
          vo = (TestVO)rows.get(i);
          vo.setButton( getButtonIcon(vo.getCheckValue()) );
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
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    TestVO vo = (TestVO)valueObject;
    vo.setCurrencyValue(new BigDecimal(1));
    vo.setIntValue(new BigDecimal(grid.getGrid().getVOListTableModel().getRowCount()+1));
    vo.setStringValue("ABC");
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
/*
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("insert into DEMO44(TEXT,DECNUM,CURRNUM,THEDATE,COMBO,CHECK_BOX,RADIO,CODE) values(?,?,?,?,?,?,?,?)");
      TestVO vo = null;
      for(int i=0;i<newValueObjects.size();i++) {
        vo = (TestVO)newValueObjects.get(i);
        stmt.setObject(6,vo.getCheckValue()==null || !vo.getCheckValue().booleanValue() ? "N":"Y");
        stmt.setString(5,vo.getComboValue());
        stmt.setBigDecimal(3,vo.getCurrencyValue());
        stmt.setDate(4,vo.getDateValue());
        stmt.setBigDecimal(2,vo.getNumericValue());
        stmt.setObject(7,vo.getRadioButtonValue()==null || !vo.getRadioButtonValue().booleanValue() ? "N":"Y");
        stmt.setString(1,vo.getStringValue());
        stmt.setString(8,vo.getLookupValue());
        stmt.execute();
      }
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
*/

    try {
      HashMap map = new HashMap();
      map.put("stringValue","TEXT");
      map.put("numericValue","DECNUM");
      map.put("currencyValue","CURRNUM");
      map.put("dateValue","THEDATE");
      map.put("comboValue","COMBO");
      map.put("checkValue","CHECK_BOX");
      map.put("radioButtonValue","RADIO");
      map.put("lookupValue","CODE");
      map.put("intValue","INTNUM");
      return QueryUtil.insertTable(
          conn,
          new UserSessionParameters(),
          newValueObjects,
          "DEMO44",
          map,
          "Y",
          "N",
          null,
          true
      );
    }
    catch (SQLException ex) {
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
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    PreparedStatement stmt2 = null;
    try {
      stmt = conn.prepareStatement("update DEMO44 set TEXT=?,DECNUM=?,CURRNUM=?,THEDATE=?,COMBO=?,CHECK_BOX=?,RADIO=?,CODE=?,INTNUM=? where TEXT=?");
      stmt2 = conn.prepareStatement("insert into DEMO44(TEXT,DECNUM,CURRNUM,THEDATE,COMBO,CHECK_BOX,RADIO,CODE,INTNUM) values(?,?,?,?,?,?,?,?,?)");
      TestVO vo = null;
      TestVO oldVO = null;
      for(int i=0;i<persistentObjects.size();i++) {
        oldVO = (TestVO)oldPersistentObjects.get(i);
        vo = (TestVO)persistentObjects.get(i);
        if (oldVO!=null) {
          stmt.setObject(6,vo.getCheckValue()==null || !vo.getCheckValue().booleanValue() ? "N":"Y");
          stmt.setString(5,vo.getComboValue());
          stmt.setBigDecimal(3,vo.getCurrencyValue());
          stmt.setDate(4,vo.getDateValue());
          stmt.setBigDecimal(2,vo.getNumericValue());
          stmt.setObject(7,vo.getRadioButtonValue()==null || !vo.getRadioButtonValue().booleanValue() ? "N":"Y");
          stmt.setString(1,vo.getStringValue());
          stmt.setString(8,vo.getLookupValue());
          stmt.setBigDecimal(9,vo.getIntValue());
          stmt.setString(10,vo.getStringValue());
          stmt.execute();
        }
        else {
          stmt2.setObject(6,vo.getCheckValue()==null || !vo.getCheckValue().booleanValue() ? "N":"Y");
          stmt2.setString(5,vo.getComboValue());
          stmt2.setBigDecimal(3,vo.getCurrencyValue());
          stmt2.setDate(4,vo.getDateValue());
          stmt2.setBigDecimal(2,vo.getNumericValue());
          stmt2.setObject(7,vo.getRadioButtonValue()==null || !vo.getRadioButtonValue().booleanValue() ? "N":"Y");
          stmt2.setString(1,vo.getStringValue());
          stmt2.setString(8,vo.getLookupValue());
          stmt2.setBigDecimal(9,vo.getIntValue());
          stmt2.execute();
        }

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
        stmt2.close();
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
      stmt = conn.prepareStatement("delete from DEMO44 where TEXT=?");
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


  private ImageIcon getButtonIcon(Boolean checkValue) {
    if (checkValue!=null && checkValue.booleanValue())
      return new ImageIcon(ClientUtils.getImage("chiuso.gif"));
    else
      return new ImageIcon(ClientUtils.getImage("aperto.gif"));

  }


  /**
   * Callback method invoked each time a cell is edited: this method define if the new value is valid.
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
    if (attributeName.equals("checkValue")) {
      TestVO vo = (TestVO)grid.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
      vo.setButton( getButtonIcon((Boolean)newValue) );
    }
    return true;
  }


  /**
   * @param grid grid
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public boolean isCellEditable(GridControl grid,int row,String attributeName) {
    TestVO vo = (TestVO)grid.getVOListTableModel().getObjectForRow(row);
    if (attributeName.equals("button") && "O".equals(vo.getComboValue())) {
      return false;
    }
    return grid.isFieldEditable(row,attributeName);
  }


  /**
   * Method used to define the background color for each cell of the grid.
   * @param rowNumber selected row index
   * @param attributeName attribute name related to the column currently selected
   * @param value object contained in the selected cell
   * @return background color of the selected cell
   */
  public Color getBackgroundColor(int row,String attributeName,Object value) {
    if (attributeName.equals("button"))
      return new Color(200,200,220);
    return super.getBackgroundColor(row,attributeName,value);
  }


  /**
   * Callback method invoked by grid when exporting data from grid.
   * @param exportOptions options used to export data; these options can be programmatically changed, in order to customize esporting result
   */
  public void exportGrid(ExportOptions exportOptions) {
  }


}
