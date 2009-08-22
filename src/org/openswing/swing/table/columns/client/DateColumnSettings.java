package org.openswing.swing.table.columns.client;

import java.text.DateFormat;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column settings that can be applied to a DateColumn or DateTimeColumn or TimeColumn
 * to dinamically reset column settings in each grid row (the default behaviour of a numeric column is to apply
 * the same settings to the all grid rows) ONLY for rendering data, NOT for editing data.
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
public interface DateColumnSettings {

  /**
   * @param row row index that identify the cell to render
   * @param attributeName attribute name that identifies the cell to render
   * @return date format to apply for the specified cell (see SimpleDateFormat documentation for date format definitions)
   */
  public String getDataFormat(int row,String attributeName);


}
