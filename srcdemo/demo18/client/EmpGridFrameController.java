package demo18.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import java.awt.Color;
import demo18.java.*;
import org.openswing.swing.util.client.ClientUtils;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for employees.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpGridFrameController extends GridController {

  private EmpGridFrame grid = null;

  public EmpGridFrameController() {
    grid = new EmpGridFrame(this);
    MDIFrame.add(grid);
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    GridEmpVO vo = (GridEmpVO)persistentObject;
    new EmpDetailFrameController(grid,vo.getEmpCode());
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deleteEmps",persistentObjects);
  }



  /**
   * Method used to define the background color for each cell of the grid.
   * @param rowNumber selected row index
   * @param attributedName attribute name related to the column currently selected
   * @param value object contained in the selected cell
   * @return background color of the selected cell
   */
  public Color getBackgroundColor(int row,String attributedName,Object value) {
    if (attributedName.equals("deptCode")) {
      if (value.equals("SF"))
        return new Color(255,100,100);
      else if (value.equals("S"))
        return new Color(210,100,100);
      else if (value.equals("P"))
        return new Color(170,100,100);
    }
    return super.getBackgroundColor(row,attributedName,value);
  }



}
