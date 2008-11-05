package demo17;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.event.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame for Departments.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DeptGridFrame extends InternalFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colDeptCode = new TextColumn();
  TextColumn colDescription = new TextColumn();
//  IntegerColumn colPhone = new IntegerColumn();
  FormattedTextColumn colPhone = new FormattedTextColumn();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  ExportButton exportButton = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  private DeptGridFrameController controller = null;


  public DeptGridFrame(DeptGridFrameController controller) {
    this.controller = controller;
    try {
      jbInit();
      setSize(620,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);
      MDIFrame.add(this);
      setTitle(ClientSettings.getInstance().getResources().getResource("departments"));

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    grid.setValueObjectClassName("demo17.DeptVO");
    colDeptCode.setColumnFilterable(true);
    colDeptCode.setColumnName("deptCode");
    colDeptCode.setColumnSortable(true);
    colDeptCode.setEditableOnInsert(true);
    colDeptCode.setMaxCharacters(5);
    colDeptCode.setTrimText(true);
    colDeptCode.setUpperCase(true);
    colDescription.setColumnDuplicable(true);
    colDescription.setColumnFilterable(true);
    colDescription.setColumnName("description");
    colDescription.setColumnSortable(true);
    colDescription.setEditableOnEdit(true);
    colDescription.setEditableOnInsert(true);
    colDescription.setPreferredWidth(350);

    colPhone.setColumnName("address.phone");
    colPhone.setColumnFilterable(true);
    colPhone.setColumnSortable(true);
    JFormattedTextField.AbstractFormatter formatter = new JFormattedTextField.AbstractFormatter() {

      /**
       * Parses <code>text</code> returning an arbitrary Object. Some
       * formatters may return null.
       *
       * @throws ParseException if there is an error in the conversion
       * @param text String to convert
       * @return Object representation of text
       */
      public Object stringToValue(String text) throws ParseException {
        if (text==null || text.equals(""))
          return null;
        String t = "";
        for(int i=0;i<text.length();i++)
          if (Character.isDigit(text.charAt(i)))
            t += text.charAt(i);
          else
            if (!( text.charAt(i)=='-' && (i==3 || i==6) ))
                throw new ParseException("Invalid pattern!",i);
        return new Integer(t);
      }

      /**
       * Returns the string value to display for <code>value</code>.
       *
       * @throws ParseException if there is an error in the conversion
       * @param value Value to convert
       * @return String representation of value
       */
      public String valueToString(Object value) throws ParseException {
        if (value==null)
          return null;
        String t = value.toString();
        if (t.length()!=9)
          throw new ParseException("Invalid pattern!",t.length()-1);
        t = t.substring(0,3)+"-"+t.substring(3,5)+"-"+t.substring(5);
        return t;
      }

    };
    colPhone.setFormatter(formatter);

    insertButton.setText("insertButton1");
    insertButton.addActionListener(new DeptGridFrame_insertButton_actionAdapter(this));
    editButton.setText("editButton1");
    saveButton.setText("saveButton1");
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colDeptCode, null);
    grid.getColumnContainer().add(colDescription, null);
    grid.getColumnContainer().add(colPhone, null);
  }

  void insertButton_actionPerformed(ActionEvent e) {
    new DeptDetailFrameController(this,null,controller.getSessions());
  }
  public GridControl getGrid() {
    return grid;
  }


}

class DeptGridFrame_insertButton_actionAdapter implements java.awt.event.ActionListener {
  DeptGridFrame adaptee;

  DeptGridFrame_insertButton_actionAdapter(DeptGridFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.insertButton_actionPerformed(e);
  }
}

