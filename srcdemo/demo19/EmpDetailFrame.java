package demo19;

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
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupDataLocator;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.border.*;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.openswing.swing.util.server.IBatisParamsWrapper;
import org.openswing.swing.util.server.IBatisUtils;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Demo Application: detail employee frame.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpDetailFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  DateControl controlDate = new DateControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelDate = new LabelControl();
  ComboBoxControl controlSex = new ComboBoxControl();
  LabelControl labelEmpCode = new LabelControl();
  CurrencyControl controlCurrency = new CurrencyControl();
  TextControl controlempCode = new TextControl();

  private Form mainPanel = new Form();
  InsertButton insertButton = new InsertButton();
  FlowLayout flowLayout1 = new FlowLayout();
  EditButton editButton = new EditButton();
  ReloadButton reloadButton = new ReloadButton();
  SaveButton saveButton = new SaveButton();
  DeleteButton deleteButton = new DeleteButton();

  CopyButton copyButton = new CopyButton();
  TextAreaControl controlNote = new TextAreaControl();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  LabelControl labelFName = new LabelControl();
  LabelControl labelLName = new LabelControl();
  TextControl controlFName = new TextControl();
  TextControl controlLName = new TextControl();
  LabelControl labelSex = new LabelControl();
  LabelControl labelSalary = new LabelControl();
  LabelControl labelTask = new LabelControl();
  CodLookupControl controlCodTask = new CodLookupControl();
  TextControl controlTaskDescr = new TextControl();

  private SqlMapClient sqlMap = null;

  LookupController taskController = new LookupController();
  LookupDataLocator taskDataLocator = new LookupDataLocator() {

    /**
     * Method called by lookup controller when validating code.
     * @param code code to validate
     * @return code validation response: VOListResponse if code validation has success, ErrorResponse otherwise
     */
    public Response validateCode(String code) {
      try {
        java.util.List tasks = sqlMap.queryForList("validateTask",code);
        ArrayList list = new ArrayList();
        list.addAll(tasks);
        return new VOListResponse(list,false,list.size());
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return new ErrorResponse(ex.getMessage());
      }
    }


    /**
     * Method called by lookup controller when user clicks on lookup button.
     * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
     * @param startIndex current index row on grid to use to start fetching data
     * @param filteredColumns filtered columns
     * @param currentSortedColumns sorted columns
     * @param currentSortedVersusColumns ordering versus of sorted columns
     * @param valueObjectType type of value object associated to the lookup grid
     * @return list of value objects to fill in the lookup grid: VOListResponse if data fetching has success, ErrorResponse otherwise
     */
    public Response loadData(
        int action,
        int startIndex,
        Map filteredColumns,
        ArrayList currentSortedColumns,
        ArrayList currentSortedVersusColumns,
        Class valueObjectType
    ) {
      try {
        // load a block of data...
        IBatisParamsWrapper gridParams = new IBatisParamsWrapper(
            filteredColumns,
            currentSortedColumns,
            currentSortedVersusColumns
        );
        return IBatisUtils.getBlockFromQuery(
            action,
            startIndex,
            50,
            sqlMap,
            "getTasks",
            gridParams
        );

//        java.util.List tasks = sqlMap.queryForList("getTasks");
//        ArrayList list = new ArrayList();
//        list.addAll(tasks);
//        return new VOListResponse(list,false,list.size());
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return new ErrorResponse(ex.getMessage());
      }
    }


    /**
     * Method called by the TreePanel to fill the tree.
     * @return a VOReponse containing a DefaultTreeModel object
     */
    public Response getTreeModel(JTree tree) {
      return new ErrorResponse("Non supported!");
    }

  };


  public EmpDetailFrame(FormController dataController,SqlMapClient sqlMap) {
    this.sqlMap = sqlMap;
    try {
      jbInit();
      mainPanel.setFormController(dataController);

      // task lookup...
      controlCodTask.setLookupController(taskController);
      taskController.setLookupDataLocator(taskDataLocator);
      taskController.setFrameTitle("tasks");
      taskController.setLookupValueObjectClassName("demo19.TaskVO");
      taskController.addLookup2ParentLink("taskCode", "taskVO.taskCode");
      taskController.addLookup2ParentLink("description","taskVO.description");
      taskController.setAllColumnVisible(false);
      taskController.setVisibleColumn("taskCode", true);
      taskController.setVisibleColumn("description", true);
      taskController.setForm(mainPanel);
      taskController.setSortableColumn("taskCode",true);
      taskController.setSortedColumn("taskCode","ASC");

      setSize(590,440);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    mainPanel.setVOClassName("demo19.EmpVO");
    mainPanel.setLayout(gridBagLayout1);
    labelDate.setText("hire date");
    labelEmpCode.setText("empCode");
    controlSex.setCanCopy(true);
    controlSex.setAttributeName("sex");
    controlSex.setDomainId("SEX");
    controlSex.setLinkLabel(labelSex);
    controlSex.setRequired(true);
    controlCurrency.setCanCopy(true);
    controlCurrency.setDecimals(2);
    controlCurrency.setMaxValue(1000.0);
    controlCurrency.setMinValue(-1000.0);
    controlCurrency.setRequired(false);
    controlCurrency.setAttributeName("salary");
    controlempCode.setMaxCharacters(5);
    controlempCode.setTrimText(true);
    controlempCode.setUpperCase(true);
    controlempCode.setEnabledOnEdit(false);
    controlempCode.setAttributeName("empCode");
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
    controlDate.setCanCopy(true);
    controlDate.setRequired(false);
    controlDate.setAttributeName("hireDate");
    copyButton.setText("copyButton1");
    controlNote.setAttributeName("note");
    titledBorder1.setTitle("Title");
    titledBorder1.setTitleColor(Color.blue);
    labelFName.setRequestFocusEnabled(true);
    labelFName.setText("firstName");
    labelLName.setText("lastName");
    controlFName.setLinkLabel(labelFName);
    controlFName.setRequired(true);
    controlFName.setAttributeName("firstName");
    controlLName.setRequired(true);
    controlLName.setAttributeName("lastName");
    labelSex.setRequestFocusEnabled(true);
    labelSex.setText("sex");
    labelSalary.setText("salary");
    labelTask.setText("task");
    controlCodTask.setMaxCharacters(5);
    controlCodTask.setRequired(true);
    controlCodTask.setAttributeName("taskVO.taskCode");
    controlTaskDescr.setAttributeName("taskVO.description");
    controlTaskDescr.setEnabledOnInsert(false);
    controlTaskDescr.setEnabledOnEdit(false);
    this.getContentPane().add(buttonsPanel,  BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(deleteButton, null);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(controlCurrency,              new GridBagConstraints(1, 2, 5, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlNote,              new GridBagConstraints(0, 7, 6, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelEmpCode,           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlempCode,            new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(labelDate,           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelFName,            new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlDate,          new GridBagConstraints(1, 3, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlFName,             new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelLName,          new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlLName,          new GridBagConstraints(3, 1, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelSalary,       new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelSex,       new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlSex,     new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelTask,       new GridBagConstraints(0, 5, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlCodTask,      new GridBagConstraints(1, 5, 1, 2, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    mainPanel.add(controlTaskDescr,     new GridBagConstraints(2, 6, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    controlempCode.setAttributeName("empCode");
    controlempCode.setRequired(true);
//    controlDate.setDateType(Consts.TYPE_DATE_TIME);
//    controlDate.setTimeFormat(Resources.H_MM_AAA);


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


  public CurrencyControl getControlCurrency() {
    return controlCurrency;
  }


}
