package org.openswing.swing.lookup.client;

import java.beans.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Editor for code selection window property.</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class SelectionWindowEditor extends PropertyEditorSupport {

  static private int[] winTypeValues = new int[]{
    LookupController.GRID_FRAME,
    LookupController.TREE_FRAME,
    LookupController.TREE_GRID_FRAME,
    LookupController.GRID_AND_FILTER_FRAME,
    LookupController.TREE_GRID_AND_FILTER_FRAME,
    LookupController.GRID_AND_PANEL_FRAME,
    LookupController.TREE_GRID_AND_PANEL_FRAME
  };

  static private String[] winTypeDescriptions = new String[]{
    "Grid",
    "Tree",
    "Tree + Grid",
    "Filter Panel + Grid",
    "Tree + Filter Panel + Grid",
    "Panel + Grid",
    "Tree + Panel + Grid"
  };

  /**
   *
   * @return String
   */
  public String getJavaInitializationString() {
    switch( ((Number) getValue()).intValue()) {
      case LookupController.TREE_FRAME:
        return "LookupController.TREE_FRAME";
      case LookupController.TREE_GRID_FRAME:
        return "LookupController.TREE_GRID_FRAME";
      case LookupController.GRID_AND_FILTER_FRAME:
        return "LookupController.GRID_AND_FILTER_FRAME";
      case LookupController.TREE_GRID_AND_FILTER_FRAME:
        return "LookupController.TREE_GRID_AND_FILTER_FRAME";
      case LookupController.GRID_AND_PANEL_FRAME:
        return "LookupController.GRID_AND_PANEL_FRAME";
      case LookupController.TREE_GRID_AND_PANEL_FRAME:
        return "LookupController.TREE_GRID_AND_PANEL_FRAME";
      default:
        return "LookupController.GRID_FRAME";
    }
  }


  /**
   *
   * @return String[]
   */
  public String[] getTags() {
    return winTypeDescriptions;
  }


  /**
   *
   * @param text String
   * @throws IllegalArgumentException
   */
  public void setAsText(String text) throws IllegalArgumentException {
    for(int i=0;i<winTypeDescriptions.length;i++)
      if (winTypeDescriptions[i].equals(text))
        setValue(new Integer(winTypeValues[i]));
  }


  /**
   *
   * @return String
   */
  public String getAsText() {
    for(int i=0;i<winTypeValues.length;i++)
      if (winTypeValues[i]==((Integer)getValue()).intValue())
        return winTypeDescriptions[i];
    return null;
  }

}
