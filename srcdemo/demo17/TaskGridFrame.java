package demo17;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame for tasks.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TaskGridFrame extends InternalFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colDeptCode = new TextColumn();
  TextColumn colDescription = new TextColumn();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  CopyButton copyButton = new CopyButton();
  ExportButton exportButton = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();


  public TaskGridFrame(TaskGridFrameController controller) {
    try {
      jbInit();
      setSize(500,300);
      grid.setController(controller);
      grid.setGridDataLocator(controller);
      MDIFrame.add(this);
      setTitle(ClientSettings.getInstance().getResources().getResource("tasks"));

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
    grid.setCopyButton(copyButton);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    grid.setValueObjectClassName("demo17.TaskVO");
    colDeptCode.setColumnFilterable(true);
    colDeptCode.setColumnName("taskCode");
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
    insertButton.setText("insertButton1");
    editButton.setText("editButton1");
    saveButton.setText("saveButton1");
    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colDeptCode, null);
    grid.getColumnContainer().add(colDescription, null);


  }


}

