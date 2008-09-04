package org.openswing.swing.table.columns.client;

import java.beans.*;

import javax.swing.*;


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
public class HeaderTextVerticalAlignmentEditor extends PropertyEditorSupport {

  static private int[] alignmentValues = new int[]{
    SwingConstants.TOP,
    SwingConstants.CENTER,
    SwingConstants.BOTTOM
  };

  static private String[] alignmentDescriptions = new String[]{
    "TOP",
    "CENTER",
    "BOTTOM"
  };

  /**
   *
   * @return String
   */
  public String getJavaInitializationString() {
    switch( ((Number) getValue()).intValue()) {
      case SwingConstants.LEFT:
        return "SwingConstants.TOP";
      case SwingConstants.RIGHT:
        return "SwingConstants.CENTER";
      case SwingConstants.CENTER:
        return "SwingConstants.BOTTOM";
      default:
        return "SwingConstants.TOP";
    }
  }


  /**
   *
   * @return String[]
   */
  public String[] getTags() {
    return alignmentDescriptions;
  }


  /**
   *
   * @param text String
   * @throws IllegalArgumentException
   */
  public void setAsText(String text) throws IllegalArgumentException {
    for(int i=0;i<alignmentDescriptions.length;i++)
      if (alignmentDescriptions[i].equals(text))
        setValue(new Integer(alignmentValues[i]));
  }


  /**
   *
   * @return String
   */
  public String getAsText() {
    for(int i=0;i<alignmentValues.length;i++)
      if (alignmentValues[i]==((Integer)getValue()).intValue())
        return alignmentDescriptions[i];
    return null;
  }

}
