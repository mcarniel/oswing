/*
 * DeptGridFrame.java
 *
 * Created on 7 aprile 2007, 22.21
 */

package demo13;


import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;

/**
 *
 * @author  Administrator
 */
public class DeptGridFrame extends org.openswing.swing.mdi.client.InternalFrame {

  private Connection conn = null;


    /** Creates new form BeanForm */
    public DeptGridFrame(Connection conn,DeptFrameController controller) {
        this.conn = conn;
        try {
          initComponents();
          setSize(620,500);
          gridControl1.setController(controller);
          gridControl1.setGridDataLocator(controller);

          setTitle(ClientSettings.getInstance().getResources().getResource("departments"));
          MDIFrame.add(this);
        }
        catch(Exception e) {
          e.printStackTrace();
        }
    }


  public void reloadData() {
    gridControl1.reloadData();
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonsPanel = new javax.swing.JPanel();
        insertButton1 = new org.openswing.swing.client.InsertButton();
        copyButton1 = new org.openswing.swing.client.CopyButton();
        editButton1 = new org.openswing.swing.client.EditButton();
        reloadButton1 = new org.openswing.swing.client.ReloadButton();
        saveButton1 = new org.openswing.swing.client.SaveButton();
        exportButton1 = new org.openswing.swing.client.ExportButton();
        deleteButton1 = new org.openswing.swing.client.DeleteButton();
        gridControl1 = new org.openswing.swing.client.GridControl();
        textColumn3 = new org.openswing.swing.table.columns.client.TextColumn();
        textColumn1 = new org.openswing.swing.table.columns.client.TextColumn();
        textColumn2 = new org.openswing.swing.table.columns.client.TextColumn();

        buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        buttonsPanel.add(insertButton1);

        buttonsPanel.add(copyButton1);

        buttonsPanel.add(editButton1);

        buttonsPanel.add(reloadButton1);

        buttonsPanel.add(saveButton1);

        buttonsPanel.add(exportButton1);

        buttonsPanel.add(deleteButton1);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.NORTH);

        gridControl1.getColumnContainer().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        gridControl1.setCopyButton(copyButton1);
        gridControl1.setDeleteButton(deleteButton1);
        gridControl1.setEditButton(editButton1);
        gridControl1.setExportButton(exportButton1);
        gridControl1.setInsertButton(insertButton1);
        gridControl1.setMaxSortedColumns(2);
        gridControl1.setSaveButton(saveButton1);
        gridControl1.setValueObjectClassName("demo13.DeptVO");
        textColumn3.setColumnDuplicable(true);
        textColumn3.setColumnFilterable(true);
        textColumn3.setColumnName("deptCode");
        textColumn3.setColumnSortable(true);
        textColumn3.setEditableOnInsert(true);
        textColumn3.setMaxCharacters(20);
        textColumn3.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
        textColumn3.setTrimText(true);
        textColumn3.setUpperCase(true);
        gridControl1.getColumnContainer().add(textColumn3);

        textColumn1.setColumnFilterable(true);
        textColumn1.setColumnName("description");
        textColumn1.setColumnSortable(true);
        textColumn1.setEditableOnEdit(true);
        textColumn1.setEditableOnInsert(true);
        textColumn1.setPreferredWidth(200);
        gridControl1.getColumnContainer().add(textColumn1);

        textColumn2.setColumnName("address");
        textColumn2.setColumnRequired(false);
        textColumn2.setEditableOnEdit(true);
        textColumn2.setEditableOnInsert(true);
        textColumn2.setPreferredWidth(300);
        gridControl1.getColumnContainer().add(textColumn2);

        getContentPane().add(gridControl1, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private org.openswing.swing.client.CopyButton copyButton1;
    private org.openswing.swing.client.DeleteButton deleteButton1;
    private org.openswing.swing.client.EditButton editButton1;
    private org.openswing.swing.client.ExportButton exportButton1;
    private org.openswing.swing.client.GridControl gridControl1;
    private org.openswing.swing.client.InsertButton insertButton1;
    private org.openswing.swing.client.ReloadButton reloadButton1;
    private org.openswing.swing.client.SaveButton saveButton1;
    private org.openswing.swing.table.columns.client.TextColumn textColumn1;
    private org.openswing.swing.table.columns.client.TextColumn textColumn2;
    private org.openswing.swing.table.columns.client.TextColumn textColumn3;
    // End of variables declaration//GEN-END:variables

}
