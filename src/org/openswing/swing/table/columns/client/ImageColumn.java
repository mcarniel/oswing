package org.openswing.swing.table.columns.client;


import java.io.*;
import java.util.*;

import java.awt.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.Image;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type image, i.e.
 * it contains an ImagePanel and an (optional) selection button.</p>
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
public class ImageColumn extends Column {

  /** flag used to define if an image selection button must be showed; default value: <code>true</code> */
  private boolean showButton = true;

  /** file filter used to filter image file selection from select button; default value: jpg and gif files only */
  private FileFilter fileFilter = new FileFilter() {

    /**
     * Whether the given file is accepted by this filter.
     */
    public boolean accept(File f) {
      return
        f.getName().toLowerCase().endsWith(".gif") ||
        f.getName().toLowerCase().endsWith(".jpg") ||
        f.isDirectory();
    }

    /**
     * The description of this filter. For example: "JPG and GIF Images"
     * @see FileView#getName
     */
    public String getDescription() {
      return "JPEG/GIF image (*.jpg; *.gif)";
    }

  };

  /** list of ActionListener objects added to selection button */
  private ArrayList listeners = new ArrayList();

  /** flag used to show the preview of the image in ImageControl and Image Column components; default value: <code>ClientSettings.SHOW_PREVIEW_OF_IMAGE</code> */
  public static boolean showPreview = ClientSettings.SHOW_PREVIEW_OF_IMAGE;


  public ImageColumn() { }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_IMAGE;
  }


  /**
   * @return define if an image selection button must be showed
   */
  public final boolean isShowButton() {
    return showButton;
  }


  /**
   * Define if an image selection button must be showed.
   * @param showButton define if an image selection button must be showed
   */
  public final void setShowButton(boolean showButton) {
    this.showButton = showButton;
  }


  /**
   * @return file filter used to filter image file selection from select button
   */
  public final FileFilter getFileFilter() {
    return fileFilter;
  }


  /**
   * Set the file filter used to filter image file selection from select button.
   * Default value: jpg and gif files only.
   * @param fileFilter file filter used to filter image file selection from select button
   */
  public final void setFileFilter(FileFilter fileFilter) {
    this.fileFilter = fileFilter;
  }


  /**
   * @return show the preview of the image in ImageControl and Image Column components; default value: <code>ClientSettings.SHOW_PREVIEW_OF_IMAGE</code>
   */
  public final boolean isShowPreview() {
    return showPreview;
  }


  /**
   * Define if showing the preview of the image in ImageControl and Image Column components; default value: <code>ClientSettings.SHOW_PREVIEW_OF_IMAGE</code>
   * @param showPreview show/hide the preview of the image in ImageControl and Image Column components
   */
  public final void setShowPreview(boolean showPreview) {
    this.showPreview = showPreview;
  }


  /**
   * Add an action listener to the selection button.
   * When the user has selected an image from file system the actionPerfomed method is invoked; the command value contains the image file name.
   * @param listener listener to add
   */
  public final void addActionListener(ActionListener listener) {
    listeners.add(listener);
  }


  /**
   * Remove an action listener from the selection button.
   * @param listener listener to remove
   */
  public final void removeActionListener(ActionListener listener) {
    listeners.remove(listener);
  }


  /**
   * @return ActionListeners added to this
   */
  public final ArrayList getListeners() {
    return listeners;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new ImageTableCellRenderer(
      tableContainer,
      getTextAlignment(),
      getColumnName()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new ImageCellEditor(
      isShowButton(),
      getFileFilter(),
      getListeners(),
      showPreview
    );
  }




}
