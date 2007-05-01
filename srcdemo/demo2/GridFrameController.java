package demo2;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;


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
    MDIFrame.add(grid);
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
    PreparedStatement stmt = null;
    try {
      String sql = "select DEMO2.TEXT,DEMO2.DECNUM,DEMO2.CURRNUM,DEMO2.THEDATE,DEMO2.COMBO,DEMO2.CHECK_BOX,DEMO2.RADIO,DEMO2.CODE,DEMO2_LOOKUP.DESCRCODE from DEMO2,DEMO2_LOOKUP where DEMO2.CODE=DEMO2_LOOKUP.CODE";
      Vector vals = new Vector();
      if (filteredColumns.size()>0) {
        FilterWhereClause[] filter = (FilterWhereClause[])filteredColumns.get("stringValue");
        sql += " and DEMO2.TEXT "+ filter[0].getOperator()+"?";
        vals.add(filter[0].getValue());
        if (filter[1]!=null) {
          sql += " and DEMO2.TEXT "+ filter[1].getOperator()+"?";
          vals.add(filter[1].getValue());
        }
      }
      if (currentSortedColumns.size()>0) {
        sql += " ORDER BY DEMO2.TEXT "+currentSortedVersusColumns.get(0);
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
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from DEMO2 where TEXT=?");
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













  /************************************************************
   * DRAG 'N DROP MANAGEMENT
   ************************************************************/

  /**
   * Method called on beginning a drag event.
   * @return <code>true</code>, dragging can continue, <code>false</code> drag is not allowed; default value: <code>true</code>
   */
  public boolean dragEnabled() {
    return true;
  }


  /**
   * Method called on firing a drop event onto the grid.
   * @param gridId identifier of the source grid (grid that generate a draf event)
   * @return <code>true</code>, drop is allowed, <code>false</code> drop is not allowed; default value: <code>true</code>
   */
  public boolean dropEnabled(String gridId) {
    return true;
  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging has entered the DropSite.
   */
  public void dragEnter() {
    System.out.println("dragEnter");
  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging has exited the DropSite.
   */
  public void dragExit() {
    System.out.println("dragExit");
  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging is currently ocurring over the DropSite.
   */
  public void dragOver() {
    System.out.println("dragOver");
  }


  /**
   * This method is invoked when the user changes the dropAction.
   */
  public void dropActionChanged() {
    System.out.println("dropActionChanged");
  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging has ended.
   */
  public void dragDropEnd() {
    System.out.println("dragDropEnd");
  }



  /************************************************************
   * DROP MANAGEMENT
   ************************************************************/

  /**
   * This method is invoked when you are dragging over the DropSite.
   */
  public void dropEnter() {
    System.out.println("dropEnter");
  }


  /**
   * This method is invoked when you are exit the DropSite without dropping.
   */
  public void dropExit () {
    System.out.println("dropExit");
  }


  /**
   * This method is invoked when a drag operation is going on.
   */
  public void dropOver (int row) {
    System.out.println("dropOver row"+row);
  }










}
