package org.openswing.swing.permissions.java;

import java.io.Serializable;

/**
 * <p>Description: Inner class used to store authorizations for a single functionId and insert, edit, delete buttons.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ButtonAuthorization implements Serializable {

  private String functionId;
  private boolean insertEnabled; // copy = insert...
  private boolean editEnabled;
  private boolean deleteEnabled;


  public ButtonAuthorization() {}


  public ButtonAuthorization(String functionId,boolean insertEnabled,boolean editEnabled,boolean deleteEnabled) {
    this.functionId = functionId;
    this.insertEnabled = insertEnabled;
    this.editEnabled = editEnabled;
    this.deleteEnabled = deleteEnabled;
  }


  public boolean isDeleteEnabled() {
    return deleteEnabled;
  }
  public boolean isEditEnabled() {
    return editEnabled;
  }
  public String getFunctionId() {
    return functionId;
  }
  public boolean isInsertEnabled() {
    return insertEnabled;
  }
  public void setInsertEnabled(boolean insertEnabled) {
    this.insertEnabled = insertEnabled;
  }
  public void setFunctionId(String functionId) {
    this.functionId = functionId;
  }
  public void setEditEnabled(boolean editEnabled) {
    this.editEnabled = editEnabled;
  }
  public void setDeleteEnabled(boolean deleteEnabled) {
    this.deleteEnabled = deleteEnabled;
  }



}


