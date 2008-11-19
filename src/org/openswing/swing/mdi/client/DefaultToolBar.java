package org.openswing.swing.mdi.client;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.FlowLayout;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Default toolbar: contains all buttons used by GridControl or Form panel.
 * Buttons are showed in thid order:
 * insert
 * copy
 * edit
 * reload
 * save
 * import
 * export
 * delete
 * filter
 * navigator bar
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
public final class DefaultToolBar extends ToolBar{


  public DefaultToolBar() {
    jbInit();
  }


  public void jbInit() {
    setLayout(new FlowLayout(FlowLayout.LEFT));
    add(insertButton,null);
    add(copyButton,null);
    add(editButton,null);
    add(reloadButton,null);
    add(saveButton,null);
    add(importButton,null);
    add(exportButton,null);
    add(deleteButton,null);
    add(filterButton,null);
    add(navigatorBar,null);
    insertButton.setEnabled(false);
    editButton.setEnabled(false);
    copyButton.setEnabled(false);
    reloadButton.setEnabled(false);
    saveButton.setEnabled(false);
    deleteButton.setEnabled(false);
    importButton.setEnabled(false);
    exportButton.setEnabled(false);
    filterButton.setEnabled(false);
    navigatorBar.setEnabled(false);
  }


  public boolean isCopyButton() {
    return copyButton.isVisible();
  }
  public boolean isDeleteButton() {
    return deleteButton.isVisible();
  }
  public boolean isEditButton() {
    return editButton.isVisible();
  }
  public boolean isExportButton() {
    return exportButton.isVisible();
  }
  public boolean isFilterButton() {
    return filterButton.isVisible();
  }
  public boolean isImportButton() {
    return importButton.isVisible();
  }
  public boolean isInsertButton() {
    return insertButton.isVisible();
  }
  public boolean isNavigatorBar() {
    return navigatorBar.isVisible();
  }
  public boolean isReloadButton() {
    return reloadButton.isVisible();
  }
  public boolean isSaveButton() {
    return saveButton.isVisible();
  }

  public void setCopyButton(boolean copyButton) {
    this.copyButton.setVisible(copyButton);
  }
  public void setDeleteButton(boolean deleteButton) {
    this.deleteButton.setVisible(deleteButton);
  }
  public void setEditButton(boolean editButton) {
    this.editButton.setVisible(editButton);
  }
  public void setExportButton(boolean exportButton) {
    this.exportButton.setVisible(exportButton);
  }
  public void setFilterButton(boolean filterButton) {
    this.filterButton.setVisible(filterButton);
  }
  public void setImportButton(boolean importButton) {
    this.importButton.setVisible(importButton);
  }
  public void setNavigatorBar(boolean navigatorBar) {
    this.navigatorBar.setVisible(navigatorBar);
  }


}
