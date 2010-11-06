package org.openswing.swing.table.permissions.java;

import java.io.*;
import java.util.*;

import org.openswing.swing.util.java.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid permissions descriptor: it contains: columns visibility state,columns editability (only for editable columns) in edit and insert modes
 * and column mandatory (only for not required columns).</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridPermissions implements Serializable,Cloneable {

  /** identifier (functionId) associated to the grid */
  private String functionId = null;

  /** permissions owner, i.e. the username of the current logged user */
  private String username = null;

  /** list of grid columns; columns position is defined according to this array */
  private String[] columnsAttribute = null;

  /** columns visibility state */
  private boolean[] columnsVisibility = null;

  /** columns editability in edit */
  private boolean[] columnsEditabilityInEdit;

  /** columns editability in insert */
  private boolean[] columnsEditabilityInInsert;

  /** columns mandatory */
  private boolean[] columnsMandatory;


  public GridPermissions() {}


  /**
   * Constructor.
   * @param functionId identifier (functionId) associated to the grid
   * @param username permissions owner, i.e. the username of the current logged user
   * @param columnsAttribute list of grid columns; columns position is defined according to this array
   * @param columnsVisibility columns visibility state
   * @param columnsEditabilityInInsert columns editability in insert
   * @param columnsEditabilityInEdit columns editability in edit
   * @param columnsMandatory columns mandatory
   */
  public GridPermissions(
      String functionId,
      String username,
      String[] columnsAttribute,
      boolean[] columnsVisibility,
      boolean[] columnsEditabilityInInsert,
      boolean[] columnsEditabilityInEdit,
      boolean[] columnsMandatory
  ) {
    this.functionId = functionId;
    this.username = username;
    this.columnsAttribute = columnsAttribute;
    this.columnsVisibility = columnsVisibility;
    this.columnsEditabilityInInsert = columnsEditabilityInInsert;
    this.columnsEditabilityInEdit = columnsEditabilityInEdit;
    this.columnsMandatory = columnsMandatory;
  }


  public boolean[] getColumnsEditabilityInInsert() {
    return columnsEditabilityInInsert;
  }
  public String getFunctionId() {
    return functionId;
  }
  public String getUsername() {
    return username;
  }
  public String[] getColumnsAttribute() {
    return columnsAttribute;
  }
  public boolean[] getColumnsVisibility() {
    return columnsVisibility;
  }
  public boolean[] getColumnsEditabilityInEdit() {
    return columnsEditabilityInEdit;
  }


  public boolean equals(Object o) {
    if (!(o instanceof GridPermissions))
      return false;
    GridPermissions p = (GridPermissions)o;
    if (!Utils.equals(columnsVisibility,p.getColumnsVisibility()))
      return false;
    if (!Utils.equals(columnsEditabilityInEdit,p.getColumnsEditabilityInEdit()))
      return false;
    if (!Utils.equals(columnsEditabilityInInsert,p.getColumnsEditabilityInInsert()))
      return false;
    if (!Utils.equals(columnsMandatory,p.getColumnsMandatory()))
      return false;
    if (!Utils.equals(columnsAttribute,p.getColumnsAttribute()))
      return false;
    if (!username.equals(p.getUsername()))
      return false;
    if (!functionId.equals(p.getFunctionId()))
      return false;
    return true;
  }


  public final Object clone() {
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException ex) {
      return new GridPermissions(
        functionId,
        username,
        (String[])columnsAttribute.clone(),
        (boolean[])columnsVisibility.clone(),
        (boolean[])columnsEditabilityInInsert.clone(),
        (boolean[])columnsEditabilityInEdit.clone(),
        (boolean[])columnsMandatory.clone()
      );
    }
  }


  public void setColumnsAttribute(String[] columnsAttribute) {
    this.columnsAttribute = columnsAttribute;
  }
  public void setColumnsVisibility(boolean[] columnsVisibility) {
    this.columnsVisibility = columnsVisibility;
  }
  public void setColumnsEditabilityInEdit(boolean[] columnsEditabilityInEdit) {
    this.columnsEditabilityInEdit = columnsEditabilityInEdit;
  }
  public void setColumnsEditabilityInInsert(boolean[] columnsEditabilityInInsert) {
    this.columnsEditabilityInInsert = columnsEditabilityInInsert;
  }
  public void setFunctionId(String functionId) {
    this.functionId = functionId;
  }
  public boolean[] getColumnsMandatory() {
    return columnsMandatory;
  }
  public void setColumnsMandatory(boolean[] columnsMandatory) {
    this.columnsMandatory = columnsMandatory;
  }
  public void setUsername(String username) {
    this.username = username;
  }
}
