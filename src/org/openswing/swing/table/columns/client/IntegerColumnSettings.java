package org.openswing.swing.table.columns.client;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column settings that can be applied to an IntegerColumn to dinamically reset column settings
 * in each grid row (the default behaviour of a numeric column is to apply the same settings to the all grid rows).
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
public interface IntegerColumnSettings {


  /**
   * @return maximum value
   */
  public double getMaxValue(int row);


  /**
   * @return minimum value
   */
  public double getMinValue(int row);


  /**
   * @return boolean thousands symbol visibility
   */
  public boolean isGrouping(int row);


}
