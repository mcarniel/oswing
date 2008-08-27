package org.openswing.swing.table.columns.client;

import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.event.ActionListener;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column fo file upload: it allows to select a file from local file system,
 * read it and store it as byte[] in the column cell.
 * Moreover, it allows to download file starting from byte[] stored in the column cell.
 * Optionally, another attribute can be binded to this column, in order to store the file name.
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
public class FileColumn extends Column {

  /** flag used to define if a file import button must be showed; default value: <code>true</code> */
  private boolean showUploadButton = true;

  /** flag used to define if a file export button must be showed; default value: <code>true</code> */
  private boolean showDownloadButton = true;

  /** optional attribute name used to bind this attribute to the file name */
  private String fileNameAttributeName = null;

  /** file filter used to filter image file selection from select button; default value: jpg and gif files only */
  private javax.swing.filechooser.FileFilter fileFilter = new javax.swing.filechooser.FileFilter() {

    /**
     * Whether the given file is accepted by this filter.
     */
    public boolean accept(File f) {
      return
        f.isFile() ||
        f.isDirectory();
    }

    /**
     * The description of this filter.
     * @see FileView#getName
     */
    public String getDescription() {
      return "All file formats (*.*)";
    }

  };


  public FileColumn() { }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_FILE;
  }


  /**
   * @return define if a file import button must be showed
   */
  public final boolean isShowUploadButton() {
    return showUploadButton;
  }


  /**
   * Define if a file import button must be showed.
   * @param showUploadButton define if a file import button must be showed, in order to select and upload a file
   */
  public final void setShowUploadButton(boolean showUploadButton) {
    this.showUploadButton = showUploadButton;
  }


  /**
   * @return define if a file export button must be showed
   */
  public final boolean isShowDownloadButton() {
    return showDownloadButton;
  }


  /**
   * Define if a file import button must be showed.
   * @param showDownloadButton define if a file export button must be showed, in order to download the file stored in the cell
   */
  public final void setShowDownloadButton(boolean showDownloadButton) {
    this.showDownloadButton = showDownloadButton;
  }


  /**
   * @return file filter used to filter file selection from import button
   */
  public final javax.swing.filechooser.FileFilter getFileFilter() {
    return fileFilter;
  }


  /**
   * @return attribute name used to bind this attribute to the file name
   */
  public final String getFileNameAttributeName() {
    return fileNameAttributeName;
  }


  /**
   * Set the attribute name used to bind this attribute to the file name.
   * @param fileNameAttributeName attribute name used to bind this attribute to the file name
   */
  public final void setFileNameAttributeName(String fileNameAttributeName) {
    this.fileNameAttributeName = fileNameAttributeName;
  }


  /**
   * Set the file filter used to filter file selection from import button.
   * Default value: *.*
   * @param fileFilter file filter used to filter file selection from import button
   */
  public final void setFileFilter(javax.swing.filechooser.FileFilter fileFilter) {
    this.fileFilter = fileFilter;
  }



  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new FileTableCellRenderer(
      tableContainer,
      getColumnName(),
      isShowUploadButton(),
      isShowDownloadButton(),
      getFileNameAttributeName()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new FileCellEditor(
      tableContainer,
      getFileFilter(),
      isShowUploadButton(),
      isShowDownloadButton(),
      getColumnName(),
      getFileNameAttributeName()
    );
  }


}
