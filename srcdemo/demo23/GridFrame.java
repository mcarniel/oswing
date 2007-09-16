package demo23;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.message.receive.java.Response;
import java.util.Map;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import java.math.BigDecimal;
import org.openswing.swing.table.client.GridController;
import javax.swing.text.MaskFormatter;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridFrame extends JFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colPropName = new TextColumn();
  MultipleTypeColumn colPropValue = new MultipleTypeColumn();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  ExportButton exportButton = new ExportButton();
  ButtonColumn colButton = new ButtonColumn();
  NavigatorBar navigatorBar1 = new NavigatorBar();

  public GridFrame(GridFrameController controller) {
    try {
      jbInit();
      setSize(400,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);

      setVisible(true);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {

    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);

    grid.setValueObjectClassName("demo23.TestVO");
    colPropName.setColumnName("propertyName");
    colPropName.setColumnSortable(true);
    colPropName.setEditableOnInsert(true);
    colPropName.setPreferredWidth(200);
    colPropName.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colPropName.setMaxCharacters(5);
    colPropName.setTrimText(true);
    colPropName.setUpperCase(true);

    colPropValue.setColumnName("propertyValue");
    colPropValue.setEditableOnEdit(true);
    colPropValue.setPreferredWidth(150);
    colPropValue.setTypeController(new MultipleTypeManager());

    editButton.setText("editButton1");
    saveButton.setText("saveButton1");
    colButton.setColumnName("button");
    colButton.setHeaderColumnName("button");
    colButton.setPreferredWidth(50);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colPropName, null);
    grid.getColumnContainer().add(colPropValue, null);

  }
  public GridControl getGrid() {
    return grid;
  }


}

