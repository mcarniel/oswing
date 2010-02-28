package org.openswing.swing.mdi.client;

import java.util.*;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.form.client.Form;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Generic toolbar: contains all buttons used by GridControl or Form panel.</p>
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
public class ToolBar extends JPanel {

  protected InsertButton insertButton = new InsertButton();
  protected EditButton editButton = new EditButton();
  protected CopyButton copyButton = new CopyButton();
  protected ReloadButton reloadButton = new ReloadButton();
  protected SaveButton saveButton = new SaveButton();
  protected DeleteButton deleteButton = new DeleteButton();
  protected ImportButton importButton = new ImportButton();
  protected ExportButton exportButton = new ExportButton();
  protected FilterButton filterButton = new FilterButton();
  protected NavigatorBar navigatorBar = new NavigatorBar();


  public ToolBar() {
    setLayout(new FlowLayout(FlowLayout.LEFT));
  }


  /**
   * Rimove all listeners and disable all toolbar buttons.
   */
  public final void disableAllButtons() {
    insertButton.removeAllDataControllers();
    editButton.removeAllDataControllers();
    copyButton.removeAllDataControllers();
    reloadButton.removeAllDataControllers();
    saveButton.removeAllDataControllers();
    deleteButton.removeAllDataControllers();
    importButton.removeAllDataControllers();
    exportButton.removeAllDataControllers();
    filterButton.removeAllDataControllers();
    navigatorBar.initNavigator(null);

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


  /**
   * @return <code>true</code> if the specified listener is already added to this, <code>false</code> otherwise
   */
  public final boolean containsDataController(DataController dataController) {
    if (!insertButton.containsDataController(dataController) &&
        !editButton.containsDataController(dataController) &&
        !copyButton.containsDataController(dataController) &&
        !reloadButton.containsDataController(dataController) &&
        !saveButton.containsDataController(dataController) &&
        !deleteButton.containsDataController(dataController) &&
        !importButton.containsDataController(dataController) &&
        !exportButton.containsDataController(dataController) &&
        !filterButton.containsDataController(dataController))
      return false;
    return true;
  }


  /**
   * Rimove all listeners and disable all toolbar buttons.
   */
  public final void setDataController(DataController dataController) {
    if (!containsDataController(dataController))
      disableAllButtons();

    if (dataController.getBindedButtons().contains(insertButton)) {
      insertButton.addDataController(dataController);
      insertButton.setEnabled(dataController.getCurrentValue(insertButton));
    }
    if (dataController.getBindedButtons().contains(editButton)) {
      editButton.addDataController(dataController);
      editButton.setEnabled(dataController.getCurrentValue(editButton));
    }
    if (dataController.getBindedButtons().contains(copyButton)) {
      copyButton.addDataController(dataController);
      copyButton.setEnabled(dataController.getCurrentValue(copyButton));
    }
    if (dataController.getBindedButtons().contains(reloadButton)) {
      reloadButton.addDataController(dataController);
      reloadButton.setEnabled(dataController.getCurrentValue(reloadButton));
    }
    if (dataController.getBindedButtons().contains(saveButton)) {
      saveButton.addDataController(dataController);
      saveButton.setEnabled(dataController.getCurrentValue(saveButton));
    }
    if (dataController.getBindedButtons().contains(deleteButton)) {
      deleteButton.addDataController(dataController);
      deleteButton.setEnabled(dataController.getCurrentValue(deleteButton));
    }
    if (dataController.getBindedButtons().contains(importButton)) {
      importButton.addDataController(dataController);
      importButton.setEnabled(dataController.getCurrentValue(importButton));
    }
    if (dataController.getBindedButtons().contains(exportButton)) {
      exportButton.addDataController(dataController);
      exportButton.setEnabled(dataController.getCurrentValue(exportButton));
    }
    if (dataController.getBindedButtons().contains(filterButton)) {
      filterButton.addDataController(dataController);
      filterButton.setEnabled(dataController.getCurrentValue(filterButton));
    }
    if (dataController.getBindedButtons().contains(navigatorBar) && dataController instanceof Grids) {
      navigatorBar.initNavigator( (Grids) dataController);
//      navigatorBar.setEnabled(dataController.getOldValue(navigatorBar));
    }

  }


  public CopyButton getCopyButton() {
    return copyButton;
  }
  public DeleteButton getDeleteButton() {
    return deleteButton;
  }
  public EditButton getEditButton() {
    return editButton;
  }
  public ExportButton getExportButton() {
    return exportButton;
  }
  public FilterButton getFilterButton() {
    return filterButton;
  }
  public ImportButton getImportButton() {
    return importButton;
  }
  public InsertButton getInsertButton() {
    return insertButton;
  }
  public NavigatorBar getNavigatorBar() {
    return navigatorBar;
  }
  public ReloadButton getReloadButton() {
    return reloadButton;
  }
  public SaveButton getSaveButton() {
    return saveButton;
  }


  /**
   * Bind grid control to this toolbar, instead of bind single toolbar buttons to grid control.
   * @param grid GridControl to bind to toolbar buttons
   */
  public final void bindGrid(GridControl grid) {
    grid.setCopyButton(getCopyButton());
    grid.setDeleteButton(getDeleteButton());
    grid.setEditButton(getEditButton());
    grid.setExportButton(getExportButton());
    grid.setFilterButton(getFilterButton());
    grid.setImportButton(getImportButton());
    grid.setInsertButton(getInsertButton());
    grid.setNavBar(getNavigatorBar());
    grid.setReloadButton(getReloadButton());
    grid.setSaveButton(getSaveButton());
  }


  /**
   * Bind Form panel to this toolbar, instead of bind single toolbar buttons to Form.
   * @param form For panel to bind to toolbar buttons
   */
  public final void bindForm(Form form) {
    form.setCopyButton(getCopyButton());
    form.setDeleteButton(getDeleteButton());
    form.setEditButton(getEditButton());
    form.setInsertButton(getInsertButton());
    form.setReloadButton(getReloadButton());
    form.setSaveButton(getSaveButton());
  }


}
