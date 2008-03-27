package org.openswing.swing.table.columns.client;


import java.beans.*;
import java.text.*;


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
public class FormatTypeEditor extends PropertyEditorSupport {

  static private int[] formats = new int[]{
    DateFormat.SHORT,
    DateFormat.MEDIUM,
    DateFormat.LONG
  };

  static private String[] formatsDescr = new String[]{
    "SHORT",
    "MEDIUM",
    "LONG"
  };

  public FormatTypeEditor() {
  }

  public String getJavaInitializationString() {
     switch( ((Number) getValue()).intValue()) {
       case DateFormat.SHORT:
         return "java.text.DateFormat.SHORT";
       case DateFormat.MEDIUM:
         return "java.text.DateFormat.MEDIUM";
       case DateFormat.LONG:
         return "java.text.DateFormat.LONG";
       default:
         return "java.text.DateFormat.SHORT";
     }
   }

  public String[] getTags() {
    return formatsDescr;
  }

  public void setAsText(String text) throws IllegalArgumentException {
    for(int i=0;i<formatsDescr.length;i++)
      if (formatsDescr[i].equals(text))
        setValue(new Integer(formats[i]));
  }

  public String getAsText() {
    for(int i=0;i<formats.length;i++)
      if (formats[i]==((Integer)getValue()).intValue())
        return formatsDescr[i];
    return null;
  }

}
