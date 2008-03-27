package org.openswing.swing.table.profiles.java;

import java.io.*;
import java.util.*;

import org.openswing.swing.util.java.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid profile descriptor: it contains the set of filter clauses, the list of sorted columns,
 * columns visibility state,columns position, columns width.</p>
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
public class GridProfile implements Serializable,Cloneable {

  /** profile identifier */
  private Object id = null;

  /** profile description */
  private String description = null;

  /** identifier (functionId) associated to the grid */
  private String functionId = null;

  /** profile owner, i.e. the username of the current logged user */
  private String username = null;

  /** current sorted columns */
  private ArrayList currentSortedColumns = new ArrayList();

  /** current sorted columns versus (Ascending/Descending) */
  private ArrayList currentSortedVersusColumns = new ArrayList();

  /** hashtable which contains the associations: attribute name, new FilterWhereClause[2] {FilterWhereClause,FilterWhereClause}) */
  private HashMap quickFilterValues = new HashMap();

  /** list of grid columns; columns position is defined according to this array */
  private String[] columnsAttribute = null;

  /** columns visibility state */
  private boolean[] columnsVisibility = null;

  /** columns width */
  private int[] columnsWidth = null;

  /** flag used to identify the default profile */
  private boolean defaultProfile;


  /**
   * Constructor.
   * @param description profile description
   * @param functionId identifier (functionId) associated to the grid
   * @param username profile owner, i.e. the username of the current logged user
   * @param currentSortedColumns current sorted columns
   * @param currentSortedVersusColumns current sorted columns versus (Ascending/Descending)
   * @param quickFilterValues hashtable which contains the associations: attribute name, new FilterWhereClause[2] {FilterWhereClause,FilterWhereClause})
   * @param columnsAttribute list of grid columns; columns position is defined according to this array
   * @param columnsVisibility columns visibility state
   * @param columnsWidth columns width
   * @param defaultProfile flag used to identify the default profile
   */
  public GridProfile(
      String description,
      String functionId,
      String username,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      HashMap quickFilterValues,
      String[] columnsAttribute,
      boolean[] columnsVisibility,
      int[] columnsWidth,
      boolean defaultProfile
  ) {
    this(
      null,
      description,
      functionId,
      username,
      currentSortedColumns,
      currentSortedVersusColumns,
      quickFilterValues,
      columnsAttribute,
      columnsVisibility,
      columnsWidth,
      defaultProfile
    );
  }


  /**
   * Constructor.
   * @param id profile identifier
   * @param description profile description
   * @param functionId identifier (functionId) associated to the grid
   * @param username profile owner, i.e. the username of the current logged user
   * @param currentSortedColumns current sorted columns
   * @param currentSortedVersusColumns current sorted columns versus (Ascending/Descending)
   * @param quickFilterValues hashtable which contains the associations: attribute name, new FilterWhereClause[2] {FilterWhereClause,FilterWhereClause})
   * @param columnsAttribute list of grid columns; columns position is defined according to this array
   * @param columnsVisibility columns visibility state
   * @param columnsWidth columns width
   * @param defaultProfile flag used to identify the default profile
   */
  public GridProfile(
      Object id,
      String description,
      String functionId,
      String username,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      HashMap quickFilterValues,
      String[] columnsAttribute,
      boolean[] columnsVisibility,
      int[] columnsWidth,
      boolean defaultProfile
  ) {
    this.id = id;
    this.description = description;
    this.functionId = functionId;
    this.username = username;
    this.currentSortedColumns = currentSortedColumns;
    this.currentSortedVersusColumns = currentSortedVersusColumns;
    this.quickFilterValues = quickFilterValues;
    this.columnsAttribute = columnsAttribute;
    this.columnsVisibility = columnsVisibility;
    this.columnsWidth = columnsWidth;
    this.defaultProfile = defaultProfile;
  }


  public ArrayList getCurrentSortedColumns() {
    return currentSortedColumns;
  }
  public ArrayList getCurrentSortedVersusColumns() {
    return currentSortedVersusColumns;
  }
  public String getFunctionId() {
    return functionId;
  }
  public Map getQuickFilterValues() {
    return quickFilterValues;
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
  public int[] getColumnsWidth() {
    return columnsWidth;
  }
  public String getDescription() {
    return description;
  }
  public Object getId() {
    return id;
  }


  public boolean equals(Object o) {
    if (!(o instanceof GridProfile))
      return false;
    GridProfile p = (GridProfile)o;
    if (!id.equals(p.getId()))
      return false;
    if (!description.equals(p.getDescription()))
      return false;
    if (!Utils.equals(columnsWidth,p.getColumnsWidth()))
      return false;
    if (!Utils.equals(columnsVisibility,p.getColumnsVisibility()))
      return false;
    if (!Utils.equals(columnsAttribute,p.getColumnsAttribute()))
      return false;
    if (!username.equals(p.getUsername()))
      return false;
    if (!functionId.equals(p.getFunctionId()))
      return false;
    if (!Utils.equals(currentSortedColumns,p.getCurrentSortedColumns()))
      return false;
    if (!Utils.equals(currentSortedVersusColumns,p.getCurrentSortedVersusColumns()))
      return false;
    if (!Utils.equals(quickFilterValues,p.getQuickFilterValues()))
      return false;
    if (defaultProfile!=p.isDefaultProfile())
      return false;

    return true;
  }


  public final Object clone() {
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException ex) {
      return new GridProfile(
        id,
        description,
        functionId,
        username,
        (ArrayList)currentSortedColumns.clone(),
        (ArrayList)currentSortedVersusColumns.clone(),
        (HashMap)quickFilterValues.clone(),
        (String[])columnsAttribute.clone(),
        (boolean[])columnsVisibility.clone(),
        (int[])columnsWidth.clone(),
        defaultProfile
      );
    }
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public void setId(Object id) {
    this.id = id;
  }
  public void setColumnsAttribute(String[] columnsAttribute) {
    this.columnsAttribute = columnsAttribute;
  }
  public void setColumnsVisibility(boolean[] columnsVisibility) {
    this.columnsVisibility = columnsVisibility;
  }
  public void setColumnsWidth(int[] columnsWidth) {
    this.columnsWidth = columnsWidth;
  }
  public void setCurrentSortedColumns(ArrayList currentSortedColumns) {
    this.currentSortedColumns = currentSortedColumns;
  }
  public void setCurrentSortedVersusColumns(ArrayList currentSortedVersusColumns) {
    this.currentSortedVersusColumns = currentSortedVersusColumns;
  }
  public void setFunctionId(String functionId) {
    this.functionId = functionId;
  }
  public void setQuickFilterValues(HashMap quickFilterValues) {
    this.quickFilterValues = quickFilterValues;
  }
  public boolean isDefaultProfile() {
    return defaultProfile;
  }
  public void setDefaultProfile(boolean defaultProfile) {
    this.defaultProfile = defaultProfile;
  }

}
