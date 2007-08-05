package org.openswing.swing.client;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Editor used in design time to select an attribute name from the value object,
* filtering by java.util.Date/java.sql.Date/java.sql.Timestamp attribute types.</p>
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
public class DateAttributeNameEditor extends AttributeNameEditor {

  public DateAttributeNameEditor() {
  }


  /**
   * @param attrType tipo dell'attributo
   * @return "true" se i due tipi sono compatibili, "false" altrimenti
   */
  protected boolean isCompatible(Class attrType) {
    try {
      if (attrType.equals(java.util.Date.class) || attrType.equals(java.sql.Date.class) || attrType.equals(java.sql.Timestamp.class)) {
        return true;
      }
      return false;
    }
    catch (Throwable ex) {
      return false;
    }
  }


}
