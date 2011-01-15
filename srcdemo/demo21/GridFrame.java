package demo21;

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
  TextControl propText = new TextControl();
  NumericControl propDecimal = new NumericControl();
  CurrencyControl propCurrency = new CurrencyControl();
  DateControl propDate = new DateControl();
  ComboBoxControl propCombo = new ComboBoxControl();
  CheckBoxControl propCheck = new CheckBoxControl();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  FormattedTextControl propFormattedText = new FormattedTextControl();
  NumericControl propInt = new NumericControl();


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

    propText.setAttributeName("stringValue");
    propText.setMaxCharacters(5);
    propText.setTrimText(true);
    propText.setUpperCase(true);
    propDecimal.setDecimals(2);
    propDecimal.setAttributeName("numericValue");
    propDecimal.setRequired(false);
    propCurrency.setAttributeName("currencyValue");
    propCurrency.setRequired(false);
    propCurrency.setDecimals(3);
    propCurrency.setGrouping(true);
    propDate.setAttributeName("dateValue");
    propDate.setRequired(false);
    propCombo.setDomainId("SEX");
    propCombo.setAttributeName("comboValue");
    propCombo.setRequired(false);
    propCheck.setAttributeName("checkValue");
    insertButton.setText("insertButton1");
    editButton.setText("editButton1");
    saveButton.setText("saveButton1");
    propFormattedText.setAttributeName("formattedTextValue");

    MaskFormatter mask = new MaskFormatter("###-##-####");
    mask.setValidCharacters("0123456789");

    propFormattedText.setFormatter(mask);

    propInt.setAttributeName("intValue");

    this.getContentPane().add(grid, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);

    // define properties...
    grid.addProperty(ClientSettings.getInstance().getResources().getResource("comboValue"),propCombo,null,null);
    grid.addProperty(ClientSettings.getInstance().getResources().getResource("stringValue"),propText,null,null);
    grid.addProperty(ClientSettings.getInstance().getResources().getResource("dateValue"),propDate,new Date(),null);
    grid.addProperty(ClientSettings.getInstance().getResources().getResource("checkValue"),propCheck,null,null);
    grid.addProperty(ClientSettings.getInstance().getResources().getResource("currencyValue"),propCurrency,null,null);
    grid.addProperty(ClientSettings.getInstance().getResources().getResource("numericValue"),propDecimal,new Integer(1),null);
    grid.addProperty(ClientSettings.getInstance().getResources().getResource("formattedTextValue"),propFormattedText,null,null);
    grid.addProperty(ClientSettings.getInstance().getResources().getResource("intValue"),propInt,null,null);

    grid.setPropertyNameWidth(100);
    grid.setPropertyValueWidth(200);

  }


  public PropertyGridControl getGrid() {
    return grid;
  }


}

