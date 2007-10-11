package demo25;

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
    new DetailFrameController(grid,vo.getCity(),conn);
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
      attribute2dbField.put("city","DEMO25.CITY");
      attribute2dbField.put("state","DEMO25.STATE");
      attribute2dbField.put("population","DEMO25.POPULATION");
      GridParams gridParams = new GridParams(action,startIndex,filteredColumns,currentSortedColumns,currentSortedVersusColumns,otherGridParams);

      Response res = QueryUtil.getQuery(
        conn,
        new UserSessionParameters(),
        "select DEMO25.CITY,DEMO25.STATE,DEMO25.POPULATION from DEMO25",
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
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from DEMO25 where CITY=?");
      for(int i=0;i<persistentObjects.size();i++) {
        TestVO vo = (TestVO)persistentObjects.get(i);
        stmt.setString(1,vo.getCity());
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
    TestVO vo = (TestVO)grid.getGrid().getVOListTableModel().getObjectForRow(row);
    if ("population".equals(attributeName)) {
      return ClientSettings.getInstance().getResources().getResource("population at ")+vo.getCity()+": "+vo.getPopulation();
    }
    else
      return "";
  }


}
