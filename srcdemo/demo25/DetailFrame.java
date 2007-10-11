package demo25;

import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import java.util.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.form.model.client.VOModel;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.util.java.Consts;
import java.sql.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupDataLocator;
import org.openswing.swing.internationalization.java.Resources;
import javax.swing.border.*;
import javax.swing.text.MaskFormatter;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Demo Application: detail frame containing several input controls.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DetailFrame extends JFrame {

  JPanel buttonsPanel = new JPanel();
  TextControl controlCity = new TextControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TextControl controlState = new TextControl();
  LabelControl labelCity = new LabelControl();
  NumericControl controlPopulation = new NumericControl();
  LabelControl labelPopulation = new LabelControl();
  ProgressBarControl controlProgressBar = new ProgressBarControl();
  LabelControl labelState = new LabelControl();

  private Form mainPanel = new Form();
  InsertButton insertButton = new InsertButton();
  FlowLayout flowLayout1 = new FlowLayout();
  EditButton editButton = new EditButton();
  ReloadButton reloadButton = new ReloadButton();
  SaveButton saveButton = new SaveButton();
  DeleteButton deleteButton = new DeleteButton();

  private Connection conn = null;
  CopyButton copyButton = new CopyButton();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  NavigatorBar navigatorBar = new NavigatorBar();


  public DetailFrame(Connection conn,DetailFrameController dataController) {
    try {
      this.conn = conn;
      jbInit();

      mainPanel.setFormController(dataController);

      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("city"); // pk for Form is based on one only attribute...
      mainPanel.linkGrid(dataController.getGridFrame().getGrid(),pk,true,true,true,navigatorBar);


      setSize(550,250);
      setVisible(true);


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }




  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    mainPanel.setVOClassName("demo25.TestVO");
    mainPanel.setLayout(gridBagLayout1);
    labelState.setText("state");
    labelCity.setText("city");
    labelPopulation.setTextAlignment(SwingConstants.RIGHT);
    labelPopulation.setText("population");
    controlPopulation.setCanCopy(true);

    controlPopulation.setLinkLabel(labelPopulation);
    controlPopulation.setRequired(true);
    controlProgressBar.setCanCopy(true);
    controlProgressBar.setMaxValue(10000000);
    controlProgressBar.setAttributeName("population");
    controlProgressBar.setRequired(false);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    mainPanel.setBorder(titledBorder1);
    mainPanel.setInsertButton(insertButton);
    mainPanel.setCopyButton(copyButton);
    mainPanel.setEditButton(editButton);
    mainPanel.setReloadButton(reloadButton);
    mainPanel.setDeleteButton(deleteButton);
    mainPanel.setSaveButton(saveButton);
    saveButton.setEnabled(false);
    controlCity.setCanCopy(true);
    controlCity.setRequired(false);
    copyButton.setText("copyButton1");
    controlState.setCanCopy(true);
    titledBorder1.setTitle("USA Cities");
    titledBorder1.setTitleColor(Color.blue);
    this.getContentPane().add(buttonsPanel,  BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(controlCity,              new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    mainPanel.add(labelState,        new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlState,          new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    mainPanel.add(labelCity,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlPopulation,         new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    mainPanel.add(labelPopulation,          new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlProgressBar,                  new GridBagConstraints(3, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    controlCity.setAttributeName("city");
    controlState.setAttributeName("state");
    controlProgressBar.setAttributeName("population");
    controlPopulation.setAttributeName("population");
    controlPopulation.setMaxValue(10000000);

  }


  public Form getMainPanel() {
    return mainPanel;
  }


  public SaveButton getSaveButton() {
    return saveButton;
  }


  public DeleteButton getDeleteButton() {
    return deleteButton;
  }


  public EditButton getEditButton() {
    return editButton;
  }


}
