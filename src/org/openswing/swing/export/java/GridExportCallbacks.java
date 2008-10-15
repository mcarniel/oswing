package org.openswing.swing.export.java;

import org.openswing.swing.message.receive.java.ValueObject;
import java.io.Serializable;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Callback method invoked by export routine.
 * Methods can be overrided in order to execute additional tasks.</p>
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
public class GridExportCallbacks implements Serializable {


  public GridExportCallbacks() {
  }


  /**
   * @return method invoked before adding grid header+rows to export document; if not null, then specified ComponentExportOptions will be added before grid
   */
  public ComponentExportOptions getHeaderComponent() {
    return null;
  }


  /**
   * @return method invoked after adding grid header+rows to export document; if not null, then specified ComponentExportOptions will be added after grid
   */
  public ComponentExportOptions getFooterComponent() {
    return null;
  }


  /**
   * @param vo value object related to current exported row
   * @param row row index just exported
   * @return method invoked after adding a row to export document; if not null, then specified ComponentExportOptions will be added after current row
   */
  public ComponentExportOptions getComponentPerRow(ValueObject vo,int row) {
    return null;
  }


  /**
   * @param vo value object related to current exported row belonging to "table header" (i.e. rows locked on the top of the grid)
   * @param row row index just exported
   * @return method invoked after adding a row to export document; if not null, then specified ComponentExportOptions will be added after current row
   */
  public ComponentExportOptions getComponentPerRowInHeader(ValueObject vo,int row) {
    return null;
  }


  /**
   * @param vo value object related to current exported row belonging to "table footer" (i.e. rows locked on the bottom of the grid)
   * @param row row index just exported
   * @return method invoked after adding a row to export document; if not null, then specified ComponentExportOptions will be added after current row
   */
  public ComponentExportOptions getComponentPerRowInFooter(ValueObject vo,int row) {
    return null;
  }


}
