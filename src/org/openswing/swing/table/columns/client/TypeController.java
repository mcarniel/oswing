package org.openswing.swing.table.columns.client;

import org.openswing.swing.client.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Base class that defines the data type for each cell of the column having type MultipleTypeColumn.
 * As default value this class manages all cells as text type.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TypeController {

  /**
   * Define the cell type for the specified row.
   * @param rowNum row index in the grid that identify the cell for which define the data type
   * @param attributeName attribute name that identify the column in the grid
   * @param grid GridControl component
   * @return Column.TYPE_TEXT as default data type; allowed values are Column.TYPE_XXX
   */
  public int getCellType(int rowNum,String attributeName,GridControl grid) {
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
    return null;
  }


}
