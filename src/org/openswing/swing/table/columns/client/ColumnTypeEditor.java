package org.openswing.swing.table.columns.client;

import java.beans.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Bean Editor</p>
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
public class ColumnTypeEditor extends PropertyEditorSupport {

  static private int[] colTypes = new int[]{
    Column.TYPE_TEXT,
    Column.TYPE_DATE,
    Column.TYPE_DATE_TIME,
    Column.TYPE_TIME,
    Column.TYPE_INT,
    Column.TYPE_DEC,
    Column.TYPE_CHECK,
    Column.TYPE_COMBO,
    Column.TYPE_LOOKUP,
    Column.TYPE_FORMATTED_TEXT,
  };

  static private String[] descColTypes = new String[]{
      "Text",
      "Date",
      "Date + Hour",
      "Hour",
      "Integer",
      "Decimal",
      "Check-box",
      "Combo-box",
      "Lookup",
      "Formatted Text"
  };


  public ColumnTypeEditor() {
  }


  public String[] getTags() {
    return descColTypes;
  }


  public void setAsText(String text) throws IllegalArgumentException {
    for(int i=0;i<descColTypes.length;i++)
      if (descColTypes[i].equals(text))
        setValue(new Integer(colTypes[i]));
  }


  public String getAsText() {
    for(int i=0;i<colTypes.length;i++)
      if (colTypes[i]==((Integer)getValue()).intValue())
        return descColTypes[i];
    return null;
  }

}
