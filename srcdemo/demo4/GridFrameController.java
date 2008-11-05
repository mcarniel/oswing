package demo4;

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
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    new DetailFrameController(this.grid,null,conn);
    return false;
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    TestVO vo = (TestVO)persistentObject;
    new DetailFrameController(grid,vo.getStringValue(),conn);
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
      attribute2dbField.put("stringValue","DEMO4.TEXT");
      attribute2dbField.put("numericValue","DEMO4.DECNUM");
      attribute2dbField.put("currencyValue","DEMO4.CURRNUM");
      attribute2dbField.put("dateValue","DEMO4.THEDATE");
      attribute2dbField.put("combo.code","COMBOCODE");
      attribute2dbField.put("combo","DEMO4.COMBO");
      attribute2dbField.put("checkValue","DEMO4.CHECK_BOX");
      attribute2dbField.put("radioButtonValue","DEMO4.RADIO");
      attribute2dbField.put("lookupValue","DEMO4.CODE");
      attribute2dbField.put("descrLookupValue","DEMO4_LOOKUP.DESCRCODE");
      GridParams gridParams = new GridParams(action,startIndex,filteredColumns,currentSortedColumns,currentSortedVersusColumns,otherGridParams);

      Response res = QueryUtil.getQuery(
        conn,
        new UserSessionParameters(),
        "select DEMO4.TEXT,DEMO4.DECNUM,DEMO4.CURRNUM,DEMO4.THEDATE,DEMO4.COMBO AS COMBOCODE,DEMO4.CHECK_BOX,DEMO4.RADIO,DEMO4.CODE,DEMO4_LOOKUP.DESCRCODE from DEMO4,DEMO4_LOOKUP where DEMO4.CODE=DEMO4_LOOKUP.CODE",
        vals,
        attribute2dbField,
        TestVO.class,
        "Y",
        "N",
        null,
        gridParams,
        50,
        true,
        true
      );

      if (!res.isError()) {

        Domain d = ClientSettings.getInstance().getDomain("ORDERSTATE");
        ArrayList list = ((VOListResponse)res).getRows();
        // this is a simplification: in a real situation combo v.o. will be retrieved from the database...
        TestVO vo = null;
        for(int i=0;i<list.size();i++) {
          vo = (TestVO)list.get(i);
          if (vo.getCombo().getCode().equals("O"))
            vo.getCombo().setDescription("opened");
          else if (vo.getCombo().getCode().equals("S"))
            vo.getCombo().setDescription("sospended");
          else if (vo.getCombo().getCode().equals("D"))
            vo.getCombo().setDescription("delivered");
          else if (vo.getCombo().getCode().equals("C"))
            vo.getCombo().setDescription("closed");
        }
      }


      return res;
//      String sql = "select DEMO4.TEXT,DEMO4.DECNUM,DEMO4.CURRNUM,DEMO4.THEDATE,DEMO4.COMBO,DEMO4.CHECK_BOX,DEMO4.RADIO,DEMO4.CODE,DEMO4_LOOKUP.DESCRCODE from DEMO4,DEMO4_LOOKUP where DEMO4.CODE=DEMO4_LOOKUP.CODE";
//      Vector vals = new Vector();
//      if (filteredColumns.size()>0) {
//        FilterWhereClause[] filter = (FilterWhereClause[])filteredColumns.get("stringValue");
//        sql += " and DEMO4.TEXT "+ filter[0].getOperator()+"?";
//        vals.add(filter[0].getValue());
//        if (filter[1]!=null) {
//          sql += " and DEMO4.TEXT "+ filter[1].getOperator()+"?";
//          vals.add(filter[1].getValue());
//        }
//      }
//      if (currentSortedColumns.size()>0) {
//        sql += " ORDER BY DEMO4.TEXT "+currentSortedVersusColumns.get(0);
//      }
//
//      stmt = conn.prepareStatement(sql);
//      for(int i=0;i<vals.size();i++)
//        stmt.setObject(i+1,vals.get(i));
//
//      ResultSet rset = stmt.executeQuery();
//
//
//      ArrayList list = new ArrayList();
//      TestVO vo = null;
//      while (rset.next()) {
//        vo = new TestVO();
//        vo.setCheckValue(rset.getObject(6)==null || !rset.getObject(6).equals("Y") ? Boolean.FALSE:Boolean.TRUE);
//        vo.setComboValue(rset.getString(5));
//        vo.setCurrencyValue(rset.getBigDecimal(3));
//        vo.setDateValue(rset.getDate(4));
//        vo.setNumericValue(rset.getBigDecimal(2));
//        vo.setRadioButtonValue(rset.getObject(7)==null || !rset.getObject(7).equals("Y") ? Boolean.FALSE:Boolean.TRUE);
//        vo.setStringValue(rset.getString(1));
//        vo.setLookupValue(rset.getString(8));
//        vo.setDescrLookupValue(rset.getString(9));
//        list.add(vo);
//      }
//      return new VOListResponse(list,false,list.size());
//    }
//    catch (SQLException ex) {
//      ex.printStackTrace();
//      return new ErrorResponse(ex.getMessage());
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
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from DEMO4 where TEXT=?");
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
   * @param attributeName attribute name that identify a grid column
   * @return tooltip text to show in the column header; this text will be automatically translated according to internationalization settings
   */
  public String getHeaderTooltip(String attributeName) {
    return attributeName;
  }


  /**
   * @param row row index in the grid
   * @param attributeName attribute name that identify a grid column
   * @return tooltip text to show in the cell identified by the specified row and attribute name; this text will be automatically translated according to internationalization settings
   */
  public String getCellTooltip(int row,String attributeName) {
    return attributeName+" at row "+row;
  }


  /**
   * Callback method invoked by grid before showing quick filter panel.
   * It allows to reset the initial filter value to show within the quick filter panel:
   * it is possible to change filtering value passed as argument, by returning another value.
   * @param attributeName attribute name that identify the column just filtered
   * @param initialValue initial value to show within the quick filter panel
   * @return new couple of values to show as initial values within the quick filter panel
   */
  public Object getInitialQuickFilterValue(String attributeName,Object initialValue) {
    if ("dateValue".equals(attributeName))
      return new java.sql.Date(System.currentTimeMillis());
//    else if ("combo".equals(attributeName))
//      return initialValue==null?null:((ComboVO)initialValue).getCode();
    return initialValue;
  }


  /**
   * Callback method invoked by grid when applying a quick filter condition or a filter condition from filter panel:
   * before executing search it is possible to change filtering value passed as argument, by returning another value.
   * @param attributeName attribute name that identify the column just filtered
   * @param value current filtering value
   * @return new value to use as filter condition
   */
  public Object beforeFilterGrid(String attributeName,Object value) {
    if ("combo".equals(attributeName))
      return value==null?null:((ComboVO)value).getCode();
    return value;
  }


}
