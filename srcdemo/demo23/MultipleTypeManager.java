package demo23;

import org.openswing.swing.table.columns.client.TypeController;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.Column;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class MultipleTypeManager extends TypeController {

  LookupController lookupController = new DemoLookupController();
  ComboBoxControl ic = new ComboBoxControl();
  CodLookupControl colLookup = new CodLookupControl();


  public MultipleTypeManager() {
    ic.setDomainId("ORDERSTATE");

    colLookup.setLookupController(lookupController);
    colLookup.setAttributeName("propertyValue");
    colLookup.setMaxCharacters(5);
    colLookup.setColumns(10);
  }


  /**
   * Define the cell type for the specified row.
   * @param rowNum row index in the grid that identify the cell for which define the data type
   * @param attributeName attribute name that identify the column in the grid
   * @param grid GridControl component
   * @return Column.TYPE_TEXT as default data type; allowed values are Column.TYPE_XXX
   */
  public int getCellType(int rowNum,String attributeName,GridControl grid) {
    switch (rowNum) {
      case 0: return Column.TYPE_TEXT;
      case 1: return Column.TYPE_INT;
      case 2: return Column.TYPE_DATE;
      case 3: return Column.TYPE_COMBO;
      case 4: return Column.TYPE_CHECK;
      case 5: return Column.TYPE_LOOKUP;
    }
    return Column.TYPE_TEXT;
  }


  /**
   * Define additional properties for the specified cell.
   * @param rowNum row index in the grid that identify the cell for which define these additional properties
   * @param attributeName attribute name that identify the column in the grid
   * @param grid GridControl component
   * @return an InputControl that contains additional properties; null as default value (i.e. do not define any additional property); if this method is overrided and returns a not null value, then this input control overrides "getCellType" return type
   */
  public InputControl getAdditionalProperties(int rowNum,String attributeName,GridControl grid) {
    if (rowNum==3) {
      return ic;
    }
    else if (rowNum==5) {
      return colLookup;
    }
    return null;
  }


}
