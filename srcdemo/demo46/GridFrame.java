package demo46;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.customvo.client.*;
import org.openswing.swing.customvo.java.*;
import org.openswing.swing.customvo.server.*;
import java.util.ArrayList;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Frame that contains a grid defined at run time.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridFrame extends JFrame {

  private CustomGridControl grid = null;
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  private Connection conn = null;
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  CopyButton copyButton = new CopyButton();
  ExportButton exportButton = new ExportButton();
  FilterButton filterButton = new FilterButton();
  private String sql = null;


  public GridFrame(Connection conn,String sql) {
    this.conn = conn;
    this.sql = sql;
    try {
      this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);

      // fetch "customFields" list of CustomFieldVO objects...
      ArrayList customFields = CustomValueObjectUtils.getCustomFields(conn,sql,new ArrayList());

      // create grid controller...
      GridFrameController controller = new GridFrameController(conn,customFields,this);

      // create custom grid...
      grid = new CustomGridControl(controller);
      grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      grid.setCopyButton(copyButton);
      grid.setDeleteButton(deleteButton);
      grid.setEditButton(editButton);
      grid.setInsertRowsOnTop(false);
      grid.setExportButton(exportButton);
      grid.setMaxNumberOfRowsOnInsert(50);
      grid.setAllowInsertInEdit(true);
      grid.setInsertButton(insertButton);
      grid.setFilterButton(filterButton);
      grid.setReloadButton(reloadButton);
      grid.setSaveButton(saveButton);
      grid.setValueObjectClassName("org.openswing.swing.customvo.java.CustomValueObject");

      // add grid to frame...
      this.getContentPane().add(grid, BorderLayout.CENTER);

      jbInit();
      setSize(750,400);
      setTitle(ClientSettings.getInstance().getResources().getResource("dynamic grid"));
      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {

    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(filterButton, null);
  }


  public String getSQL() {
    return sql;
  }
  public CustomGridControl getGrid() {
    return grid;
  }



}
