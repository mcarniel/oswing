package org.openswing.swing.client;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Editor used in design time to select an attribute name from the value object,
 * filtering by Short/Integer/Long/Float/BigDecimal/Double attribute types and primitive types.</p>
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
public class NumericAttributeNameEditor extends AttributeNameEditor {

  public NumericAttributeNameEditor() {
  }


  /**
   * @param attrType attribute type
   * @return <code>true</code> if attrType is compatible with a numeric control, <code>false</code> otherwise
   */
  protected boolean isCompatible(Class attrType) {
    try {
      if (attrType.equals(Integer.class) ||
          attrType.equals(Long.class) ||
          attrType.equals(Float.class) ||
          attrType.equals(Double.class) ||
          attrType.equals(java.math.BigDecimal.class) ||
          attrType.equals(Short.class) ||
          attrType.equals(Integer.TYPE) ||
          attrType.equals(Long.TYPE) ||
          attrType.equals(Float.TYPE) ||
          attrType.equals(Double.TYPE) ||
          attrType.equals(Short.TYPE)
      ) {
        return true;
      }
      return false;
    }
    catch (Throwable ex) {
      return false;
    }
  }


}
