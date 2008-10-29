package demo45;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value Object used to store grid permissions.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridPermissionsPerRoleVO extends ValueObjectImpl {

  private String columnName;
  private boolean visible;
  private boolean editableInIns;
  private boolean editableInEdit;
  private boolean required;
  private String description;

  private boolean defaultVisible;
  private boolean defaultEditableInIns;
  private boolean defaultEditableInEdit;
  private boolean defaultRequired;


  public GridPermissionsPerRoleVO() {
  }


  public boolean isVisible() {
    return visible;
  }
  public void setVisible(boolean visible) {
    this.visible = visible;
  }
  public String getColumnName() {
    return columnName;
  }
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  public boolean isEditableInIns() {
    return editableInIns;
  }
  public void setEditableInIns(boolean editableInIns) {
    this.editableInIns = editableInIns;
  }
  public boolean isEditableInEdit() {
    return editableInEdit;
  }
  public void setEditableInEdit(boolean editableInEdit) {
    this.editableInEdit = editableInEdit;
  }
  public boolean isRequired() {
    return required;
  }
  public void setRequired(boolean required) {
    this.required = required;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public boolean isDefaultEditableInEdit() {
    return defaultEditableInEdit;
  }
  public boolean isDefaultEditableInIns() {
    return defaultEditableInIns;
  }
  public boolean isDefaultRequired() {
    return defaultRequired;
  }
  public boolean isDefaultVisible() {
    return defaultVisible;
  }
  public void setDefaultVisible(boolean defaultVisible) {
    this.defaultVisible = defaultVisible;
  }
  public void setDefaultRequired(boolean defaultRequired) {
    this.defaultRequired = defaultRequired;
  }
  public void setDefaultEditableInIns(boolean defaultEditableInIns) {
    this.defaultEditableInIns = defaultEditableInIns;
  }
  public void setDefaultEditableInEdit(boolean defaultEditableInEdit) {
    this.defaultEditableInEdit = defaultEditableInEdit;
  }




}
