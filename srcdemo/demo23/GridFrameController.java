package demo23;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import java.awt.Font;
import org.openswing.swing.client.GridControl;
import java.math.BigDecimal;


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

  public GridFrameController() {
    grid = new GridFrame(this);
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

    // simulation...
    ArrayList rows = new ArrayList();

    TestVO vo1 = new TestVO(); vo1.setPropertyName("Text"); vo1.setPropertyValue("This is a text"); rows.add(vo1);
    TestVO vo2 = new TestVO(); vo2.setPropertyName("Number"); vo2.setPropertyValue(new Integer(1234)); rows.add(vo2);
    TestVO vo3 = new TestVO(); vo3.setPropertyName("Date"); vo3.setPropertyValue(new java.util.Date()); rows.add(vo3);
    TestVO vo4 = new TestVO(); vo4.setPropertyName("Combo"); vo4.setPropertyValue("O"); rows.add(vo4);
    TestVO vo5 = new TestVO(); vo5.setPropertyName("Check-Box"); vo5.setPropertyValue(Boolean.TRUE); rows.add(vo5);
    TestVO vo6 = new TestVO(); vo6.setPropertyName("Lookup"); vo6.setPropertyValue("C1"); rows.add(vo6);



    return new VOListResponse(rows,false,rows.size());
  }




  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return new VOListResponse(persistentObjects,false,persistentObjects.size());
  }


}
