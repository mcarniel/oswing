package demo22;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import javax.swing.text.MaskFormatter;
import org.openswing.swing.util.client.ClientSettings;
import java.util.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Property Grid Frame</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridFrame extends JFrame {
  PropertyGridControl grid = new PropertyGridControl();
  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton = new ReloadButton();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();


  public GridFrame(GridFrameController controller) {
    try {
      super.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
      jbInit();
      setSize(400,300);
      grid.setController(controller);

      setVisible(true);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {

    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setEditButton(editButton);
    grid.setInsertButton(insertButton);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);

    insertButton.setText("insertButton1");
    editButton.setText("editButton1");
    saveButton.setText("saveButton1");

    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);

    // define properties...
    TestVO defValues = new TestVO();
    defValues.setDateValue(new java.sql.Date(System.currentTimeMillis()));
    grid.addProperties(defValues);

    grid.setPropertyNameWidth(100);
    grid.setPropertyValueWidth(200);

  }


  public PropertyGridControl getGrid() {
    return grid;
  }


}

