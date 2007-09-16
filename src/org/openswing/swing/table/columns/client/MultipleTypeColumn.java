package org.openswing.swing.table.columns.client;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column that can support multiple types of data: text, date, numeric, combo, check-box, currency, lookup, etc.
 * </p>
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
public class MultipleTypeColumn extends Column {

  /** defines the data type for each cell of the column; as default value this class manages all cells as text type */
  private TypeController typeController = new TypeController();


  public MultipleTypeColumn() { }


  /**
   * @return column type
   */
  public final int getColumnType() {
    return TYPE_MULTIPLE_TYPE;
  }


  /**
   * Defines the data type for each cell of the column.
   * @param typeController controller that defines the data type for each cell of the column
   */
  public final void setTypeController(TypeController typeController) {
    this.typeController = typeController;
  }


  /**
   * @return controller that defines the data type for each cell of the column
   */
  public final TypeController getTypeController() {
    return this.typeController;
  }


}
