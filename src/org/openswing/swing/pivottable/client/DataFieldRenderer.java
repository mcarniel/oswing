package org.openswing.swing.pivottable.client;

import java.awt.Color;
import org.openswing.swing.pivottable.java.GenericNodeKey;
import java.awt.Font;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Base class used to define for each data cell:<br/>
 * <ul>
 * <li>background and foreground color</p>
 * <li>font to use</p>
 * </ul>
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
public class DataFieldRenderer {


  /**
   * @param currentColor current color to set
   * @param rowPath GenericNodeKey row fields path that identify current row
   * @param colPath GenericNodeKey column fields path that identify current column
   * @param value value to show in the specified cell
   * @param row current row
   * @param col current column
   * @return Color background color to set
   */
  public Color getBackgroundColor(Color currentColor,GenericNodeKey rowPath,GenericNodeKey colPath,Object value,int row,int col) {
    return currentColor;
  }


  /**
   * @param currentColor current color to set
   * @param rowPath GenericNodeKey row fields path that identify current row
   * @param colPath GenericNodeKey column fields path that identify current column
   * @param value value to show in the specified cell
   * @param row current row
   * @param col current column
   * @return Color foreground color to set
   */
  public Color getForegroundColor(Color currentColor,GenericNodeKey rowPath,GenericNodeKey colPath,Object value,int row,int col) {
    return currentColor;
  }


  /**
   * @param currentFont current font to set
   * @param rowPath GenericNodeKey row fields path that identify current row
   * @param colPath GenericNodeKey column fields path that identify current column
   * @param value value to show in the specified cell
   * @param row current row
   * @param col current column
   * @return font to set
   */
  public Font getFont(Font currentFont,GenericNodeKey rowPath,GenericNodeKey colPath,Object value,int row,int col) {
    return currentFont;
  }


}
