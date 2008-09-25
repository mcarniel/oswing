package org.openswing.swing.export.java;

import java.io.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Generic component descriptor, used to export data.</p>
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
public class ComponentExportOptions  implements Serializable {


 /** values to add to export document; may be null */
 private Object[][] cellsContent = null;


 /**
  * Define component layout.
  * @param gridx cell containing the leading edge of the component's display area
  * @param gridy the cell at the top of the component's display area
  */
 public ComponentExportOptions() {
 }


  /**
   * @return values to add to export document; may be null
   */
  public final Object[][] getCellsContent() {
    return cellsContent;
  }


  /**
   * Set values to add to export document.
   * @param cellsContent values to add to export document
   */
  public final void setCellsContent(Object[][] cellsContent) {
    this.cellsContent = cellsContent;
  }




}
