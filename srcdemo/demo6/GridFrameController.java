package demo6;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
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
    try {

      ArrayList vals = new ArrayList();
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("stringValue","DEMO6.TEXT");
      attribute2dbField.put("numericValue","DEMO6.DECNUM");
      attribute2dbField.put("currencyValue","DEMO6.CURRNUM");
      attribute2dbField.put("dateValue","DEMO6.THEDATE");
      attribute2dbField.put("comboValue","DEMO6.COMBO");
      attribute2dbField.put("checkValue","DEMO6.CHECK_BOX");
      attribute2dbField.put("radioButtonValue","DEMO6.RADIO");
      attribute2dbField.put("lookupValue","DEMO6.CODE");
      attribute2dbField.put("descrLookupValue","DEMO6_LOOKUP.DESCRCODE");
      GridParams gridParams = new GridParams(action,startIndex,filteredColumns,currentSortedColumns,currentSortedVersusColumns,otherGridParams);

      return QueryUtil.getQuery(
        conn,
        new UserSessionParameters(),
        "select DEMO6.TEXT,DEMO6.DECNUM,DEMO6.CURRNUM,DEMO6.THEDATE,DEMO6.COMBO,DEMO6.CHECK_BOX,DEMO6.RADIO,DEMO6.CODE,DEMO6_LOOKUP.DESCRCODE from DEMO6,DEMO6_LOOKUP where DEMO6.CODE=DEMO6_LOOKUP.CODE",
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
      stmt = conn.prepareStatement("delete from DEMO6 where TEXT=?");
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



}
