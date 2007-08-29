package demo21;

import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.awt.Font;
import java.math.BigDecimal;
import org.openswing.swing.properties.client.PropertyGridController;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Property Grid controller.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridFrameController extends PropertyGridController {

  private GridFrame grid = null;


  public GridFrameController() {
    grid = new GridFrame(this);

  }


  /**
   * Callback method invoked by the PropertyGridControl for loading data (property values).
   */
  public void loadData(PropertyGridControl grid) {
    // do something...
    // e.g.
    // grid.setPropertyValue("...",...);
  }



  /**
   * Callback method invoked by the PropertyGridControl for saving data the first time.
   * @param grid PropertyGridControl
   * @param changedRowNumbers rows indexes, related to changed rows
   * Note: you can obtain the current value of a property by means of the method: PropertyGridControl.getPropertyValue
   * @return <code>true</code> if the insert operation has been correctly performed, <code>false</code> otherwise
   */
  public boolean insertRecords(PropertyGridControl grid,int[] changedRowNumbers) {
    // do something...
    // e.g.
    // Object new1 = grid.getPropertyValue("...");
    // ...
    return true;
  }


  /**
   * Callback method invoked by the PropertyGridControl for saving data the first time.
   * @param grid PropertyGridControl
   * @param changedRowNumbers rows indexes, related to changed rows
   * Note: you can obtain the current value of a property by means of the method: PropertyGridControl.getPropertyValue
   * Note: you can obtain the previous value of a property by means of the method: PropertyGridControl.getOldPropertyValue
   * @return <code>true</code> if the insert operation has been correctly performed, <code>false</code> otherwise
   */
  public boolean updateRecords(PropertyGridControl grid,int[] changedRowNumbers) {
    // do something...
    // e.g.
    // for(int i=0;i<changedRowNumbers.length;i++) {
    //   Object new1 = grid.getPropertyValue(changedRowNumbers[i]);
    //   Object old1 = grid.getOldPropertyValue(changedRowNumbers[i]);
    // }
    // ...
    return true;
  }


}
