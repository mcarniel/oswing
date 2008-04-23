package org.openswing.swing.lookup.client;

import java.beans.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Editor for invalid code property.</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class InvalidCodeEditor extends PropertyEditorSupport {

  static private int[] actionValues = new int[]{
      LookupController.ON_INVALID_CODE_CLEAR_CODE,
      LookupController.ON_INVALID_CODE_RESTORE_LAST_VALID_CODE,
      LookupController.ON_INVALID_CODE_RESTORE_FOCUS
  };


  static private String[] actionDescriptions = new String[]{
    "Clear code",
    "Restore last valid code",
    "Restore focus"
  };

  /**
   *
   * @return String
   */
  public String getJavaInitializationString() {
    switch( ((Number) getValue()).intValue()) {
      case LookupController.ON_INVALID_CODE_RESTORE_LAST_VALID_CODE:
        return "LookupController.ON_INVALID_CODE_RESTORE_LAST_VALID_CODE";
      case LookupController.ON_INVALID_CODE_RESTORE_FOCUS:
        return "LookupController.ON_INVALID_CODE_RESTORE_FOCUS";
      default:
        return "LookupController.ON_INVALID_CODE_CLEAR_CODE";
    }
  }


  /**
   *
   * @return String[]
   */
  public String[] getTags() {
    return actionDescriptions;
  }


  /**
   *
   * @param text String
   * @throws IllegalArgumentException
   */
  public void setAsText(String text) throws IllegalArgumentException {
    for(int i=0;i<actionDescriptions.length;i++)
      if (actionDescriptions[i].equals(text))
        setValue(new Integer(actionValues[i]));
  }


  /**
   *
   * @return String
   */
  public String getAsText() {
    for(int i=0;i<actionValues.length;i++)
      if (actionValues[i]==((Integer)getValue()).intValue())
        return actionDescriptions[i];
    return null;
  }

}
