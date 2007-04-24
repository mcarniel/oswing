package demo16;


import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import java.util.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.form.model.client.VOModel;
import org.openswing.swing.util.java.Consts;
import java.sql.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.border.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.columns.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Image grid frame.</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ImageGridFrame extends JFrame {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  EditButton editButton1 = new EditButton();
  ReloadButton reloadButton1 = new ReloadButton();
  SaveButton saveButton1 = new SaveButton();
  InsertButton insertButton1 = new InsertButton();
  DeleteButton deleteButton1 = new DeleteButton();
  GridControl grid = new GridControl();
  TextColumn colImaneName = new TextColumn();
  ImageColumn colImage = new ImageColumn();


  public ImageGridFrame(ImageGridFrameController gridController) {
      try {
        setSize(800,440);
        jbInit();
        grid.setController(gridController);
        grid.setGridDataLocator(gridController);

        colImage.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            File f = new File(e.getActionCommand());
            ImageVO vo = (ImageVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
            vo.setImageName(f.getName());
          }
        });

      }
      catch(Exception e) {
        e.printStackTrace();
      }

  }


  public ImageGridFrame() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton1);
    grid.setEditButton(editButton1);
    grid.setInsertButton(insertButton1);
    grid.setReloadButton(reloadButton1);
    grid.setRowHeight(50);
    grid.setRowHeightFixed(false);
    grid.setSaveButton(saveButton1);
    grid.setValueObjectClassName("demo16.ImageVO");
    colImaneName.setColumnName("imageName");
    colImaneName.setEditableOnInsert(false);
    colImaneName.setPreferredWidth(200);
    colImage.setColumnName("image");
    colImage.setEditableOnInsert(true);
    colImage.setPreferredWidth(580);
    this.getContentPane().add(buttonsPanel,  BorderLayout.NORTH);
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(deleteButton1, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colImaneName, null);
    grid.getColumnContainer().add(colImage, null);
  }


}
