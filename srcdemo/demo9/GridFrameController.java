package demo9;

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
    PreparedStatement stmt = null;
    try {
      String sql = "select DEMO3.TEXT,DEMO3.DECNUM,DEMO3.CURRNUM,DEMO3.THEDATE,DEMO3.COMBO,DEMO3.CHECK_BOX,DEMO3.RADIO,DEMO3.CODE,DEMO3_LOOKUP.DESCRCODE from DEMO3,DEMO3_LOOKUP where DEMO3.CODE=DEMO3_LOOKUP.CODE";
      Vector vals = new Vector();
      if (filteredColumns.size()>0) {
        FilterWhereClause[] filter = (FilterWhereClause[])filteredColumns.get("stringValue");
        sql += " and DEMO3.TEXT "+ filter[0].getOperator()+"?";
        vals.add(filter[0].getValue());
        if (filter[1]!=null) {
          sql += " and DEMO3.TEXT "+ filter[1].getOperator()+"?";
          vals.add(filter[1].getValue());
        }
      }
      if (currentSortedColumns.size()>0) {
        sql += " ORDER BY DEMO3.TEXT "+currentSortedVersusColumns.get(0);
      }

      stmt = conn.prepareStatement(sql);
      for(int i=0;i<vals.size();i++)
        stmt.setObject(i+1,vals.get(i));

      ResultSet rset = stmt.executeQuery();


      ArrayList list = new ArrayList();
      TestVO vo = null;
      while (rset.next()) {
        vo = new TestVO();
        vo.setCheckValue(rset.getObject(6)==null || !rset.getObject(6).equals("Y") ? Boolean.FALSE:Boolean.TRUE);
        vo.setComboValue(rset.getString(5));
        vo.setCurrencyValue(rset.getBigDecimal(3));
        vo.setDateValue(rset.getDate(4));
        vo.setNumericValue(rset.getBigDecimal(2));
        vo.setRadioButtonValue(rset.getObject(7)==null || !rset.getObject(7).equals("Y") ? Boolean.FALSE:Boolean.TRUE);
        vo.setStringValue(rset.getString(1));
        vo.setLookupValue(rset.getString(8));
        vo.setDescrLookupValue(rset.getString(9));
        vo.setButton( getButtonIcon(vo.getCheckValue()) );
        list.add(vo);
      }
      return new VOListResponse(list,false,list.size());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
      }
      catch (SQLException ex1) {
      }
    }

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
      stmt = conn.prepareStatement("insert into DEMO3(TEXT,DECNUM,CURRNUM,THEDATE,COMBO,CHECK_BOX,RADIO,CODE) values(?,?,?,?,?,?,?,?)");
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
      return QueryUtil.insertTable(
          conn,
          new UserSessionParameters(),
          newValueObjects,
          "DEMO3",
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
      stmt = conn.prepareStatement("update DEMO3 set TEXT=?,DECNUM=?,CURRNUM=?,THEDATE=?,COMBO=?,CHECK_BOX=?,RADIO=?,CODE=? where TEXT=?");
      stmt2 = conn.prepareStatement("insert into DEMO3(TEXT,DECNUM,CURRNUM,THEDATE,COMBO,CHECK_BOX,RADIO,CODE) values(?,?,?,?,?,?,?,?)");
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
          stmt.setString(9,vo.getStringValue());
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
      stmt = conn.prepareStatement("delete from DEMO3 where TEXT=?");
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



}
