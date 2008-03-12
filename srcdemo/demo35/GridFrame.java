package demo35;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import org.openswing.swing.table.columns.client.CheckBoxColumn;
import org.openswing.swing.table.columns.client.ButtonColumn;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridFrame extends InternalFrame implements ItemListener {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colName = new TextColumn();
  TextColumn colSurname = new TextColumn();
  TextColumn colStreet = new TextColumn();
  TextColumn colCity = new TextColumn();
  ComboColumn colCountry = new ComboColumn();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  JPanel filterPanel = new JPanel();
  JPanel topPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  ComboBoxControl controlCountry = new ComboBoxControl();
  LabelControl labelCountry = new LabelControl();
  FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);


  public GridFrame(GridFrameController controller) {
    try {
      jbInit();
      setSize(750,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    controlCountry.setNullAsDefaultValue(false);
    controlCountry.setDomainId("COUNTRIES");
    labelCountry.setText("Filter by country");
    filterPanel.setLayout(flowLayout2);
    filterPanel.add(labelCountry,null);
    filterPanel.add(controlCountry,null);
    controlCountry.addItemListener(this);
    colCountry.setDomainId("COUNTRIES");
    grid.setInsertButton(insertButton);
    grid.setEditButton(editButton);
    grid.setSaveButton(saveButton);
    grid.setDeleteButton(deleteButton);

    setTitle("People list");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    this.setSize(new Dimension(1400, 305));
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setFunctionId("F1");
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);
    grid.setValueObjectClassName("demo35.Person");
    colName.setColumnFilterable(true);
    colName.setEditableOnEdit(false);
    colName.setEditableOnInsert(true);
    colName.setColumnRequired(true);
    colName.setSortVersus("ASC");
    colName.setSortingOrder(1);
    colName.setColumnName("name");
    colName.setHeaderColumnName("Name");
    colName.setColumnSortable(true);
    colName.setColumnSortable(true);
    colSurname.setColumnRequired(true);
    colSurname.setEditableOnEdit(false);
    colSurname.setEditableOnInsert(true);
    colSurname.setColumnName("surname");
    colSurname.setColumnFilterable(true);
    colSurname.setColumnSortable(true);
    colStreet.setColumnName("address.street");
    colCity.setColumnName("address.city");
    colCountry.setColumnName("address.country");
    colStreet.setEditableOnEdit(true);
    colCity.setEditableOnEdit(true);
    colCountry.setEditableOnEdit(false);
    colStreet.setEditableOnInsert(true);
    colCity.setEditableOnInsert(true);
    colCountry.setEditableOnInsert(false);
    colSurname.setHeaderColumnName("Surname");
    colStreet.setHeaderColumnName("Street");
    colStreet.setPreferredWidth(200);
    colCity.setHeaderColumnName("City");
    colCountry.setHeaderColumnName("Country");
    topPanel.setLayout(borderLayout1);
    topPanel.add(filterPanel,BorderLayout.NORTH);
    topPanel.add(buttonsPanel,BorderLayout.SOUTH);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(topPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar1, null);

    grid.getColumnContainer().add(colName, null);
    grid.getColumnContainer().add(colSurname, null);
    grid.getColumnContainer().add(colStreet, null);
    grid.getColumnContainer().add(colCity, null);
    grid.getColumnContainer().add(colCountry, null);
  }


    public void itemStateChanged(ItemEvent e) {
      if (e.getStateChange()==e.SELECTED) {
          grid.reloadData();
      }
    }


    public String getSelectedCountry() {
      return (String)controlCountry.getValue();
    }



}


