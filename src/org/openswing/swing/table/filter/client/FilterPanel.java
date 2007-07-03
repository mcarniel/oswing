package org.openswing.swing.table.filter.client;

import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.logger.client.Logger;
import org.openswing.swing.client.*;


import java.util.*;
import java.math.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.sql.Types;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.client.CheckBoxControl;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.client.Grids;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Filter panel, associated to the grid.
 * It can be showed at the right of the grid or as a dialog.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */

public class FilterPanel extends JPanel {

  private JPanel mainPanel = new JPanel();
  private JTabbedPane tabbedPane = new JTabbedPane();
  private BorderLayout borderLayout2 = new BorderLayout();
  private JPanel orderPanel = new JPanel();
  private JPanel filterPanel = new JPanel();
  private JScrollPane orderScrollPane = new JScrollPane();
  private BorderLayout borderLayout3 = new BorderLayout();
  private JScrollPane filterScrollPane = new JScrollPane();
  private JPanel oPanel = new JPanel();
  private JPanel fPanel = new JPanel();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private GenericButton filterButton = new GenericButton(new ImageIcon(ClientUtils.getImage("filter.gif")));
  private FlowLayout flowLayout1 = new FlowLayout();
  private JPanel topPanel = new JPanel();
  private GridBagLayout gridBagLayout4 = new GridBagLayout();
  private GridBagLayout gridBagLayout3 = new GridBagLayout();

  /** column properties */
  private Column[] colProperties = null;

  /** list of column names used to filter the grid */
  private ArrayList filterColNames = new ArrayList();

  /** list of combo boxes that contain filter criteria (equals, like, etc.); used to filter the grid */
  private ArrayList filterColOps = new ArrayList();

  /** list of input controls used to contain the filter value, according to column type */
  private ArrayList filterColValues = new ArrayList();

  /** collection of pairs <attribute name,FilterWhereClause> */
  private Hashtable filterColumns = new Hashtable();

  /** filter clauses initially applied to columns; collection of pairs <attribute name,FilterWhereClause[2]> */
  private Map initialFilterList = new HashMap();

  /** list of combo boxes that contain column names; used to sort the grid */
  private ArrayList sortColNames = new ArrayList();

  /** list of combo boxes that contain sort versus (ASC/DESC) */
  private ArrayList sortVersus = new ArrayList();

  /** grid controller */
  private Grids grid = null;

  /** collection of pairs <attribute name,sort versus> */
  private Hashtable orderColumns = new Hashtable();

  /** order and sort versus for columns initially sorted (String[]{attribute name,ASC/DESC}) */
  private ArrayList initialOrderList = new ArrayList();
  GridBagLayout gridBagLayout5 = new GridBagLayout();

  /**
   * Constructor called by a grid control to apply filtering/sorting conditions.
   * @param colProperties column properties
   * @param gridOrder list or order clauses to apply to the grid
   * @param gridFilter list of filter clauses to apply to the grid
   * @param grid grid control
   */
  public FilterPanel(Column[] colProperties,Grids grid) {
    this.colProperties = colProperties;
//    this.gridOrder = gridOrder;
//    this.gridFilter = gridFilter;
    this.grid = grid;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void init() {


    filterColNames.clear();
    filterColOps.clear();
    filterColValues.clear();
    sortColNames.clear();
    sortVersus.clear();
    oPanel.removeAll();
    fPanel.removeAll();
    initialOrderList.clear();
    initialFilterList.clear();

    // prepare sort panel...
    prepareSortPanel();

    // prepare filter panel...
    prepareFilterPanel();

    oPanel.revalidate();
    fPanel.revalidate();
    oPanel.repaint();
    fPanel.repaint();
  }


  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout5);
    try {
      this.setBackground(new Color(this.getBackground().getRed() - 10,
                                   this.getBackground().getGreen() - 10,
                                   this.getBackground().getBlue() - 10));
    }
    catch (Exception ex) {
    }
    mainPanel.setLayout(borderLayout2);
    mainPanel.setOpaque(false);
    orderPanel.setLayout(borderLayout3);
    filterPanel.setLayout(gridBagLayout4);
    oPanel.setLayout(gridBagLayout1);
    fPanel.setLayout(gridBagLayout2);
    topPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.RIGHT);
    flowLayout1.setVgap(0);
    filterButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        filterButton_actionPerformed(e);
      }
    });
    filterScrollPane.setBorder(BorderFactory.createEtchedBorder());
    orderScrollPane.setBorder(null);
    orderPanel.setBorder(BorderFactory.createEtchedBorder());
    topPanel.setOpaque(false);
    this.add(mainPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(tabbedPane,  BorderLayout.CENTER);
    orderPanel.add(orderScrollPane,  BorderLayout.CENTER);
    tabbedPane.add(filterPanel, ClientSettings.getInstance().getResources().getResource("filtering conditions"));
    tabbedPane.add(orderPanel,  ClientSettings.getInstance().getResources().getResource( "sorting conditions"));
    filterPanel.add(filterScrollPane,       new GridBagConstraints(1, 0, 1, 1, 2.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    mainPanel.add(topPanel, BorderLayout.NORTH);
    orderScrollPane.getViewport().add(oPanel, null);
    filterScrollPane.getViewport().add(fPanel, null);
    topPanel.add(filterButton,null);
  }


  /**
   * Prepare sort panel.
   */
  private void prepareSortPanel() {
    for(int i=0;i<grid.getCurrentSortedColumns().size();i++)
      initialOrderList.add(new String[]{
        grid.getCurrentSortedColumns().get(i).toString(),
        grid.getCurrentSortedVersusColumns().get(i).toString()
      });

    ArrayList colOrdered = new ArrayList(colProperties.length);
    for(int i=0;i<colProperties.length;i++)
      colOrdered.add(null);
    int row = 0;
    ComboModel colNamesComboModel = null;
    DefaultComboBoxModel orderVersusComboModel = null;

    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].isColumnSortable()) {
        // prepare combo model having column names, one combo for each sortable column...
        colNamesComboModel = new ComboModel();
        colNamesComboModel.addItem("",""); // first combo item is always equals to "" (no sort condition defined)...
        for(int j=0;j<colProperties.length;j++)
          if (colProperties[j].isColumnSortable())
            colNamesComboModel.addItem(colProperties[j].getColumnName(),ClientSettings.getInstance().getResources().getResource(colProperties[j].getHeaderColumnName()));

        // prepare combo model related to sorting versus, one for each sortable column...
        orderVersusComboModel = new DefaultComboBoxModel();
        orderVersusComboModel.addElement("");
        orderVersusComboModel.addElement(Consts.ASC_SORTED);
        orderVersusComboModel.addElement(Consts.DESC_SORTED);

        JComboBox colNameComboBox = new JComboBox(colNamesComboModel);
        JComboBox orderVersusComboBox = new JComboBox(orderVersusComboModel);

        oPanel.add(colNameComboBox,    new GridBagConstraints(0, row, 1, 1, 1.0, 0.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        oPanel.add(orderVersusComboBox,    new GridBagConstraints(1, row, 1, 1, 0.0, 0.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        sortColNames.add(colNameComboBox);
        sortVersus.add(orderVersusComboBox);

        if (!colProperties[i].getSortVersus().equals(Consts.NO_SORTED))
          colOrdered.set(colProperties[i].getSortingOrder(),new Integer(i));
        row++;
      }
      oPanel.add(new JLabel(""),    new GridBagConstraints(0, row, 1, 1, 1.0, 1.0
              ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

      // set initial item in combos, for each pre-sorted column...
      int colIndex = -1;
      row = 0;
      for(int i=0;i<colOrdered.size();i++)
        if (colOrdered.get(i)!=null) {
          colIndex = ((Integer)colOrdered.get(i)).intValue();
          ((JComboBox)sortColNames.get(row)).setSelectedItem(
            ClientSettings.getInstance().getResources().getResource(colProperties[colIndex].getHeaderColumnName())
          );

          if (colProperties[colIndex].getSortVersus().equals(Consts.ASC_SORTED))
            ((JComboBox)sortVersus.get(row)).setSelectedIndex(1);
          else if (colProperties[colIndex].getSortVersus().equals(Consts.DESC_SORTED))
            ((JComboBox)sortVersus.get(row)).setSelectedIndex(2);

          row++;
        }
     oPanel.revalidate();
  }


  /**
   * Prepare filter panel.
   */
  private void prepareFilterPanel() {
    // backup initial filtering conditions...
    Iterator it = grid.getQuickFilterValues().keySet().iterator();
    String attrName = null;
    while(it.hasNext()) {
      attrName = it.next().toString();
      initialFilterList.put(attrName,grid.getQuickFilterValues().get(attrName));
    }

    int row = 0;
    DefaultComboBoxModel colOpsComboModel = null;
    DefaultComboBoxModel colOpsComboModel2 = null;
    FilterWhereClause[] filter = null;
    String colOpValue = null;
    Object colValue = null;
    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].isColumnFilterable() && colProperties[i].getColumnType()!=Column.TYPE_CHECK) {

        // find out a filter applied to the current analyzed column...
        colOpValue = null;
        colValue = null;
        filter = (FilterWhereClause[])grid.getQuickFilterValues().get(colProperties[i].getColumnName());
        if (filter!=null) {
          colOpValue = filter[0].getOperator();
          colValue = filter[0].getValue();
        }

        // prepare a combo model for the first filter criteria...
        colOpsComboModel = new DefaultComboBoxModel();
        colOpsComboModel.addElement("");
        colOpsComboModel.addElement(Consts.EQ);
        colOpsComboModel.addElement(Consts.NEQ);
        colOpsComboModel.addElement(Consts.LIKE);
        colOpsComboModel.addElement(Consts.IS_NOT_NULL);
        colOpsComboModel.addElement(Consts.IS_NULL);
        colOpsComboModel.addElement(Consts.GE);
        colOpsComboModel.addElement(Consts.GT);
        colOpsComboModel.addElement(Consts.LE);
        colOpsComboModel.addElement(Consts.LT);

        final JComboBox colOpsComboBox = new JComboBox(colOpsComboModel);
        if (colOpValue!=null)
          colOpsComboBox.setSelectedItem(colOpValue);

        final JComponent c = createValueComponent(colProperties[i]);
        setValueComponent(c,colValue,colProperties[i]);

        fPanel.add(new JLabel(ClientSettings.getInstance().getResources().getResource(colProperties[i].getHeaderColumnName())),
                   new GridBagConstraints(0, row, 1, 1, 0.0, 0.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        fPanel.add(colOpsComboBox,
                   new GridBagConstraints(1, row, 1, 1, 0.0, 0.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        fPanel.add(c,
                   new GridBagConstraints(2, row, 1, 1, 1.0, 0.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        colOpsComboBox.addItemListener(new ComboItemListener(i,colOpsComboBox,c));

        filterColNames.add(colProperties[i]);
        filterColOps.add(colOpsComboBox);
        filterColValues.add(c);

        row++;

        // (ONLY FOR date/numeric type columns) add an additional filter...
        if (colProperties[i].getColumnType()==Column.TYPE_INT ||
            colProperties[i].getColumnType()==Column.TYPE_DEC ||
            colProperties[i].getColumnType()==Column.TYPE_PERC ||
            colProperties[i].getColumnType()==Column.TYPE_CURRENCY ||
            colProperties[i].getColumnType()==Column.TYPE_DATE ||
            colProperties[i].getColumnType()==Column.TYPE_DATE_TIME ||
            colProperties[i].getColumnType()==Column.TYPE_TIME) {

          colOpValue = null;
          colValue = null;
          if (filter!=null && filter[1]!=null) {
            colOpValue = filter[1].getOperator();
            colValue = filter[1].getValue();
          }


          // prepare a combo model for the second filter criteria...
          colOpsComboModel2 = new DefaultComboBoxModel();
          colOpsComboModel2.addElement("");
          colOpsComboModel2.addElement(Consts.EQ);
          colOpsComboModel2.addElement(Consts.GE);
          colOpsComboModel2.addElement(Consts.GT);
          colOpsComboModel2.addElement(Consts.IS_NOT_NULL);
          colOpsComboModel2.addElement(Consts.IS_NULL);
          colOpsComboModel2.addElement(Consts.LE);
          colOpsComboModel2.addElement(Consts.LIKE);
          colOpsComboModel2.addElement(Consts.LT);
          colOpsComboModel2.addElement(Consts.NEQ);

          final JComboBox colOpsComboBox2 = new JComboBox(colOpsComboModel2);
          if (colOpValue!=null)
            colOpsComboBox2.setSelectedItem(colOpValue);

          final JComponent c2 = createValueComponent(colProperties[i]);
          setValueComponent(c2,colValue,colProperties[i]);

          fPanel.add(colOpsComboBox2,
                     new GridBagConstraints(1, row, 1, 1, 0.0, 0.0
                  ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
          fPanel.add(c,
                     new GridBagConstraints(2, row, 1, 1, 1.0, 0.0
                  ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

          colOpsComboBox2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
              if (e.getStateChange()==e.SELECTED) {
                if (colOpsComboBox2.getSelectedItem().equals(Consts.IS_NOT_NULL) ||
                    colOpsComboBox2.getSelectedItem().equals(Consts.IS_NULL))
                  c2.setEnabled(false);
                else
                  c2.setEnabled(true);
              }
            }
          });

          filterColNames.add(colProperties[i]);
          filterColOps.add(colOpsComboBox2);
          filterColValues.add(c2);

          row++;

        } // end if on additional filter...
    }
    fPanel.add(new JLabel(""),    new GridBagConstraints(0, row, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    fPanel.revalidate();
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to listen a combo selection event.</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class ComboItemListener implements ItemListener {

    private int i;
    private JComboBox colOpsComboBox = null;
    private JComponent c = null;

    public ComboItemListener(int i,JComboBox colOpsComboBox,JComponent c) {
      this.i = i;
      this.colOpsComboBox = colOpsComboBox;
      this.c = c;
    }

    public void itemStateChanged(ItemEvent e) {
      if (e.getStateChange()==e.SELECTED) {
        if (colOpsComboBox.getSelectedItem().equals(Consts.IS_NOT_NULL) ||
            colOpsComboBox.getSelectedItem().equals(Consts.IS_NULL)) {
          c.setEnabled(false);
          setValueComponent(c,null,colProperties[i]);
        }
        else
          c.setEnabled(true);
      }
    }
  }



  /**
   * Dinamically create an input control according to the specified column (type).
   */
  private JComponent createValueComponent(Column colProperties) {
    JComponent result=null;
    if (colProperties.getColumnType()==Column.TYPE_COMBO) {
      Domain domain = ClientSettings.getInstance().getDomain(((ComboColumn)colProperties).getDomainId());
      Vector couple = null;
      DomainPair[] pairs = domain.getDomainPairList();
      Vector items = new Vector();
      items.add( "" );
      for(int i=0;i<pairs.length;i++) {
        items.add(pairs[i].getDescription());
      }
      JComboBox list=new JComboBox(items);
      JPanel listPanel = new JPanel();
      listPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
      listPanel.add(list,null);
      return listPanel;
    }

    switch (colProperties.getColumnType()) {
      case Column.TYPE_DATE :
      case Column.TYPE_DATE_TIME :
      case Column.TYPE_TIME :
      {
        result=new DateControl();
        ((DateControl)result).setDateType(colProperties.getColumnType());
      }
      ;break;
      case Column.TYPE_INT:
      {
        NumericControl num = new NumericControl();
        if (colProperties!=null) {
          try {
            num.setDecimals(0);
            num.setMaxValue(((IntegerColumn)colProperties).getMaxValue());
            num.setMinValue(((IntegerColumn)colProperties).getMinValue());
            num.setGrouping(((IntegerColumn)colProperties).isGrouping());
          } catch (ClassCastException ex) {
            Logger.error(this.getClass().getName(),"createValueComponent","Error while creating an input control for the filter",ex);
          }
        }
        result=num;
      }
      ;break;
      case Column.TYPE_DEC :
      case Column.TYPE_PERC: {
        NumericControl num = new NumericControl();
        if (colProperties!=null) {
          try {
            num.setDecimals(((DecimalColumn)colProperties).getDecimals());
            num.setMaxValue(((DecimalColumn)colProperties).getMaxValue());
            num.setMinValue(((DecimalColumn)colProperties).getMinValue());
            num.setGrouping(((DecimalColumn)colProperties).isGrouping());
          } catch (ClassCastException ex) {
            Logger.error(this.getClass().getName(),"createValueComponent","Error while creating an input control for the filter",ex);
          }
        }
        result=num;
      }
      ;break;
      case Column.TYPE_CURRENCY: {
        CurrencyControl num = new CurrencyControl();
        if (colProperties!=null) {
          try {
            num.setDecimals(((DecimalColumn)colProperties).getDecimals());
            num.setMaxValue(((DecimalColumn)colProperties).getMaxValue());
            num.setMinValue(((DecimalColumn)colProperties).getMinValue());
            num.setGrouping(((DecimalColumn)colProperties).isGrouping());
          } catch (ClassCastException ex) {
            Logger.error(this.getClass().getName(),"createValueComponent","Error while creating an input control for the filter",ex);
          }
        }
        result=num;
      }
      ;break;
      case Column.TYPE_TEXT:
      {
        TextControl edit = new TextControl();

        if (((TextColumn)colProperties).isRpadding() && (((TextColumn)colProperties).getMaxCharacters()>0))
          edit.setRpadding(true);

        try {
          edit.setMaxCharacters(((TextColumn)colProperties).getMaxCharacters());
        } catch (ClassCastException ex) {
            Logger.error(this.getClass().getName(),"createValueComponent","Error while creating an input control for the filter",ex);
        }
        result=edit;
      }
      ;break;
      case Column.TYPE_FORMATTED_TEXT:
      {
        JFormattedTextField edit = ((FormattedTextColumn)colProperties).getTextBox();
        result=edit;
      }
      ;break;
      case Column.TYPE_LOOKUP:
      {
        BaseInputControl edit = null;
        if (((CodLookupColumn)colProperties).isAllowOnlyNumbers()) {
          edit = new NumericControl();
          try {
            ((NumericControl)edit).setMaxCharacters(((CodLookupColumn)colProperties).getMaxCharacters());
          } catch (ClassCastException ex) {
              Logger.error(this.getClass().getName(),"createValueComponent","Error while creating an input control for the filter",ex);
          }
        }
        else {
          edit = new TextControl();
          if (((CodLookupColumn)colProperties).isCodePadding() && (((CodLookupColumn)colProperties).getMaxCharacters()>0))
            ((TextControl)edit).setRpadding(true);
          try {
            ((TextControl)edit).setMaxCharacters(((CodLookupColumn)colProperties).getMaxCharacters());
          } catch (ClassCastException ex) {
              Logger.error(this.getClass().getName(),"createValueComponent","Error while creating an input control for the filter",ex);
          }
        }
        result=edit;
      }
      ;break;
      default: {
        Logger.error(this.getClass().getName(),"createValueComponent","Error while creating an input control for the filter: column type not supported",null);
      }
    }
    return result;
  }


  /**
   * Set input control content, according to the specified filter value.
   * @param result input control to set
   * @param initValue initial filter value
   * @param colProperties column properties related to the current input control
   */
  private void setValueComponent(JComponent result,Object initValue,Column colProperties) {
    if (colProperties.getColumnType()==Column.TYPE_COMBO) {
      JComboBox combo = (JComboBox)((JPanel)result).getComponent(0);
      if (initValue==null)
        combo.setSelectedIndex(0);
      return;
    }

    switch (colProperties.getColumnType()) {
      case Column.TYPE_DATE :
      case Column.TYPE_DATE_TIME :
      case Column.TYPE_TIME :
      {
        ((DateControl)result).setValue((Date)initValue);
      }
      ;break;
      case Column.TYPE_INT:
      {
        ((NumericControl)result).setValue(initValue==null?null:new Double(initValue.toString()));
      }
      ;break;
      case Column.TYPE_DEC :
      case Column.TYPE_PERC: {
        ((NumericControl)result).setValue(initValue==null?null:new Double(initValue.toString()));
      }
      ;break;
      case Column.TYPE_CURRENCY: {
        ((CurrencyControl)result).setValue(initValue==null?null:new Double(initValue.toString()));
      }

      ;break;
      case Column.TYPE_TEXT:
      case Column.TYPE_LOOKUP:
      {
        ((TextControl)result).setValue(initValue==null?"":initValue.toString());
      }
      ;break;
      case Column.TYPE_FORMATTED_TEXT:
      {
        ((JFormattedTextField)result).setValue(initValue==null?"":initValue.toString());
      }
      ;break;
      default: {
        Logger.error(this.getClass().getName(),"setValueComponent","Error while setting input control value: column type not supported",null);
      }
    }
  }


  /**
   * @return filter value contained in the specified input control, created according to the specified column type
   */
  private Object getValueComponent(JComponent result,Column colProperties) {
    if (colProperties.getColumnType()==Column.TYPE_COMBO) {
      Domain domain = ClientSettings.getInstance().getDomain(((ComboColumn)colProperties).getDomainId());
      Vector couple = null;
      DomainPair[] pairs = domain.getDomainPairList();
      Vector items = new Vector();
      result = (JComboBox)((JPanel)result).getComponent(0);
      for(int i=0;i<pairs.length;i++)
        if (pairs[i].getDescription().equals(((JComboBox)result).getSelectedItem()))
          return pairs[i].getCode();
      return null;
    }

    switch (colProperties.getColumnType()) {
      case Column.TYPE_DATE :
      case Column.TYPE_DATE_TIME :
      case Column.TYPE_TIME :
      {
        return ((DateControl)result).getValue();
      }
      case Column.TYPE_INT:
      {
        return ((NumericControl)result).getValue();
      }
      case Column.TYPE_DEC :
      case Column.TYPE_PERC: {
        return ((NumericControl)result).getValue();
      }
      case Column.TYPE_CURRENCY: {
        return ((NumericControl)result).getValue();
      }
      case Column.TYPE_TEXT:
        return ((TextControl)result).getValue();
      case Column.TYPE_FORMATTED_TEXT:
        return ((JFormattedTextField)result).getValue();
      case Column.TYPE_LOOKUP: {
        if (((CodLookupColumn)colProperties).isAllowOnlyNumbers())
          return ((NumericControl)result).getValue();
        else
        return ((TextControl)result).getValue();
      }
      default: {
        Logger.error(this.getClass().getName(),"getValueComponent","Error while retrieving filter value: column type not supported",null);
        return null;
      }
    }
  }


  /**
   * Apply filtering and sorting conditions and execute grid reloading.
   */
  void filterButton_actionPerformed(ActionEvent e) {
    orderColumns.clear();
    filterColumns.clear();

    String colName = null;
    String versus = null;
    boolean alreadyExist;
    grid.getCurrentSortedColumns().clear();
    grid.getCurrentSortedVersusColumns().clear();
    for(int i=0;i<sortColNames.size();i++)
      if (((JComboBox)sortColNames.get(i)).getSelectedIndex()>0 &&
          ((JComboBox)sortVersus.get(i)).getSelectedIndex()>0) {
        colName = ((ComboModel)((JComboBox)sortColNames.get(i)).getModel()).getSelectedCode(((JComboBox)sortColNames.get(i)).getSelectedIndex());

        alreadyExist = grid.getCurrentSortedColumns().contains(colName);
        if (alreadyExist)
          continue;

        if (((JComboBox)sortVersus.get(i)).getSelectedIndex()==1)
          versus = Consts.ASC_SORTED;
        else if (((JComboBox)sortVersus.get(i)).getSelectedIndex()==2)
          versus = Consts.DESC_SORTED;
        orderColumns.put(colName,new String[]{colName,versus});
        grid.getCurrentSortedColumns().add(colName);
        grid.getCurrentSortedVersusColumns().add(versus);
      } // end if inside for

    Object value = null;
    String op = null;
    grid.getQuickFilterValues().clear();
    for(int i=0;i<filterColOps.size();i++)
      if (((JComboBox)filterColOps.get(i)).getSelectedIndex()>0 &&
          getValueComponent((JComponent)filterColValues.get(i),(Column)filterColNames.get(i))!=null) {
        colName = ((Column)filterColNames.get(i)).getColumnName();
        value = getValueComponent((JComponent)filterColValues.get(i),(Column)filterColNames.get(i));
        op = ((JComboBox)filterColOps.get(i)).getSelectedItem().toString();
        FilterWhereClause whereClause = new FilterWhereClause(colName,op,value);
        filterColumns.put(colName,whereClause);

        if (grid.getQuickFilterValues().get(colName)==null)
          grid.getQuickFilterValues().put(colName,new FilterWhereClause[]{whereClause,null});
        else
          ((FilterWhereClause[])grid.getQuickFilterValues().get(colName))[1] = whereClause;

      }
      else if (((JComboBox)filterColOps.get(i)).getSelectedIndex()>0 &&
               (((JComboBox)filterColOps.get(i)).getSelectedItem().toString().equals(Consts.IS_NOT_NULL) ||
                ((JComboBox)filterColOps.get(i)).getSelectedItem().toString().equals(Consts.IS_NULL))) {
        colName = ((Column)filterColNames.get(i)).getColumnName();
        op = ((JComboBox)filterColOps.get(i)).getSelectedItem().toString();
        FilterWhereClause whereClause = null;
        whereClause = new FilterWhereClause(colName,((JComboBox)filterColOps.get(i)).getSelectedItem().toString(),null);
        filterColumns.put(colName,whereClause);

        if (grid.getQuickFilterValues().get(colName)==null)
          grid.getQuickFilterValues().put(colName,new FilterWhereClause[]{whereClause,null});
        else
          ((FilterWhereClause[])grid.getQuickFilterValues().get(colName))[1] = whereClause;
      }

    // repaint icons in column headers...
    grid.updateColumnHeaderIcons();

    // reload grid...
    grid.reload();
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to define a combo model.</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class ComboModel extends DefaultComboBoxModel {

    ArrayList codes = new ArrayList();

    public void addItem(String code,String colName) {
      super.addElement(colName);
      codes.add(code);
    }

    public String getSelectedCode(int selectedIndex) {
      if (super.getSelectedItem()==null)
        return null;
      else
        return codes.get(selectedIndex).toString();
    }

  }




}
