package org.openswing.swing.table.filter.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import javax.swing.text.DefaultFormatterFactory;
import java.text.*;


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

  /** list of input controls used to contain the filter value, according to column type; each element is a JComponent[] array */
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

  /** used only when "showFilterPanelOnGrid" is set to <code>true</code>; define filter panel policy for hiding it; allowed values: Consts.FILTER_PANEL_ON_GRID_xxx */
  private int filterPanelOnGridPolicy;

  GridBagLayout gridBagLayout5 = new GridBagLayout();

  private GenericButton lockPanel;
  private ImageIcon lockImage;
  private ImageIcon unlockImage;
  private boolean locked;

  private GenericButton closePanel;


  /**
   * Constructor called by a grid control to apply filtering/sorting conditions.
   * @param colProperties column properties
   * @param gridOrder list or order clauses to apply to the grid
   * @param gridFilter list of filter clauses to apply to the grid
   * @param grid grid control
   * @param filterPanelOnGridPolicy used only when "showFilterPanelOnGrid" is set to <code>true</code>; define filter panel policy for hiding it; allowed values: FILTER_PANEL_ON_GRID_xxx; default value: ClientSettings.FILTER_PANEL_ON_GRID_POLICY
   */
  public FilterPanel(Column[] colProperties,Grids grid,int filterPanelOnGridPolicy) {
    this.colProperties = colProperties;
//    this.gridOrder = gridOrder;
//    this.gridFilter = gridFilter;
    this.grid = grid;
    this.filterPanelOnGridPolicy = filterPanelOnGridPolicy;
    try {
      jbInit();

      if (filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_PRESSED ||
          filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_UNPRESSED) {
        // create padlock...
        lockImage = new ImageIcon(ClientUtils.getImage(ClientSettings.FILTER_PANEL_LOCK_ON));
        unlockImage = new ImageIcon(ClientUtils.getImage(ClientSettings.FILTER_PANEL_LOCK_OFF));

        lockPanel = new GenericButton(lockImage);
        lockPanel.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            lockPanel_mouseClicked(e);
          }
        });
        setLocked(filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_PRESSED);
        topPanel.add(lockPanel,null);
      }
      else if (filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_USE_CLOSE_BUTTON) {
        // create close button...
        closePanel = new GenericButton(new ImageIcon(ClientUtils.getImage(ClientSettings.CLOSE_BUTTON_ON_FILTER_PANEL)));
        closePanel.setToolTipText(ClientSettings.getInstance().getResources().getResource("close window"));
        topPanel.add(closePanel,null);
      }


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  void lockPanel_mouseClicked(MouseEvent e) {
    setLocked(! locked);
  }


  public boolean isLocked() {
    return(locked);
  }


  public void setLocked(boolean value) {
    this.locked = value;
    if (locked)
      lockPanel.setIcon(lockImage);
    else
      lockPanel.setIcon(unlockImage);
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
    filterScrollPane.setHorizontalScrollBarPolicy(filterScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
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
    ComboBoxControl orderVersusComboBox = null;
    Domain d = null;

    for(int i=0;i<grid.getMaxSortedColumns();i++) {
      // prepare combo model having column names, one combo for each sortable column...
      colNamesComboModel = new ComboModel();
      colNamesComboModel.addItem("",""); // first combo item is always equals to "" (no sort condition defined)...
      for(int j=0;j<colProperties.length;j++)
        if (colProperties[j].isColumnSortable())
          colNamesComboModel.addItem(colProperties[j].getColumnName(),ClientSettings.getInstance().getResources().getResource(colProperties[j].getHeaderColumnName()));

      // prepare combo model related to sorting versus, one for each sortable column...
      d = new Domain("");
      d.addDomainPair("","");
      d.addDomainPair(Consts.ASC_SORTED,Consts.ASC_SORTED);
      d.addDomainPair(Consts.DESC_SORTED,Consts.DESC_SORTED);
      orderVersusComboBox = new ComboBoxControl();
      orderVersusComboBox.setDomain(d);

      JComboBox colNameComboBox = new JComboBox(colNamesComboModel);

      oPanel.add(colNameComboBox,    new GridBagConstraints(0, row, 1, 1, 1.0, 0.0
              ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
      oPanel.add(orderVersusComboBox,    new GridBagConstraints(1, row, 1, 1, 0.0, 0.0
              ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

      sortColNames.add(colNameComboBox);
      sortVersus.add(orderVersusComboBox);

      row++;
    }

    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].isColumnSortable())
        if (!colProperties[i].getSortVersus().equals(Consts.NO_SORTED))
          colOrdered.set(colProperties[i].getSortingOrder(),new Integer(i));

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
          ((ComboBoxControl)sortVersus.get(row)).setSelectedIndex(1);
        else if (colProperties[colIndex].getSortVersus().equals(Consts.DESC_SORTED))
          ((ComboBoxControl)sortVersus.get(row)).setSelectedIndex(2);

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
    FilterWhereClause[] filter = null;
    String colOpValue = null;
    Object colValue = null;
    Domain d = null;
    Domain d2 = null;
    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].isColumnFilterable() &&
//          colProperties[i].getColumnType()!=Column.TYPE_CHECK &&
          colProperties[i].getColumnType()!=Column.TYPE_IMAGE) {

        // find out a filter applied to the current analyzed column...
        colOpValue = null;
        colValue = null;
        filter = (FilterWhereClause[])grid.getQuickFilterValues().get(colProperties[i].getColumnName());
        if (filter!=null) {
          colOpValue = filter[0].getOperator();
          colValue = filter[0].getValue();
        }

        // prepare a combo model for the first filter criteria...
        d = new Domain("");
        d.addDomainPair("","");
        d.addDomainPair(Consts.EQ,Consts.EQ);
        d.addDomainPair(Consts.NEQ,Consts.NEQ);
        if (colProperties[i].getColumnType()==Column.TYPE_LINK ||
            colProperties[i].getColumnType()==Column.TYPE_BUTTON ||
            colProperties[i].getColumnType()==Column.TYPE_FORMATTED_TEXT ||
            colProperties[i].getColumnType()==Column.TYPE_LOOKUP ||
            colProperties[i].getColumnType()==Column.TYPE_MULTI_LINE_TEXT ||
            colProperties[i].getColumnType()==Column.TYPE_TEXT)
          d.addDomainPair(ClientSettings.LIKE,Consts.LIKE);
        if (colProperties[i].getColumnType()!=Column.TYPE_CHECK) {
          d.addDomainPair(Consts.IS_NOT_NULL,Consts.IS_NOT_NULL);
          d.addDomainPair(Consts.IS_NULL,Consts.IS_NULL);
          d.addDomainPair(Consts.GE,Consts.GE);
          d.addDomainPair(Consts.GT,Consts.GT);
          d.addDomainPair(Consts.LE,Consts.LE);
          d.addDomainPair(Consts.LT,Consts.LT);
          if (ClientSettings.INCLUDE_IN_OPERATOR) {
            d.addDomainPair(Consts.IN, Consts.IN);
            d.addDomainPair(Consts.NOT_IN, Consts.NOT_IN);
          }
        }

        final ComboBoxControl colOpsComboBox = new ComboBoxControl();
        colOpsComboBox.setDomain(d);
        if (colOpValue!=null)
          colOpsComboBox.setValue(colOpValue);

        final JComponent c = createValueComponent(colProperties[i]);

        fPanel.add(new JLabel(ClientSettings.getInstance().getResources().getResource(colProperties[i].getHeaderColumnName())),
                   new GridBagConstraints(0, row, 1, 1, 0.0, 0.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        fPanel.add(colOpsComboBox,
                   new GridBagConstraints(1, row, 1, 1, 0.0, 0.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        JPanel aux = new JPanel();
        aux.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        aux.add(c,null);
        fPanel.add(aux,
                   new GridBagConstraints(2, row, 1, 1, 1.0, 0.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        colOpsComboBox.addItemListener(new ComboItemListener(i,colOpsComboBox.getComboBox(),c instanceof JPanel ? (JComponent)c.getComponent(0):c));
        c.addFocusListener(new OpFocusListener(filterColOps.size(),c));
        JComponent[] cc = new JComponent[]{c};

        if (colValue instanceof ArrayList) {
          ArrayList values = (ArrayList)colValue;
          setValueComponent(c,values.get(0),colProperties[i]);
          for(int j=1;j<values.size();j++) {
            JComponent newc = createValueComponent(colProperties[i]);
            newc.addFocusListener(new OpFocusListener(filterColOps.size(),newc));
            JComponent[] newcc = new JComponent[cc.length+1];
            System.arraycopy(cc,0,newcc,0,cc.length);
            newcc[newcc.length-1] = newc;
            cc = newcc;
            aux.add(newc,null);
            setValueComponent(newc,values.get(j),colProperties[i]);
          }
        }
        else
          setValueComponent(c,colValue,colProperties[i]);

        filterColNames.add(colProperties[i]);
        filterColOps.add(colOpsComboBox);
        filterColValues.add(cc);

        row++;

        // (ONLY FOR date/numeric type columns) add an additional filter...
        if (colProperties[i].getColumnType()==Column.TYPE_PROGRESS_BAR ||
            colProperties[i].getColumnType()==Column.TYPE_INT ||
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
          d2 = new Domain("");
          d2.addDomainPair("","");
          d2.addDomainPair(Consts.EQ,Consts.EQ);
          d2.addDomainPair(Consts.GE,Consts.GE);
          d2.addDomainPair(Consts.GT,Consts.GT);
          d2.addDomainPair(Consts.IS_NOT_NULL,Consts.IS_NOT_NULL);
          d2.addDomainPair(Consts.IS_NULL,Consts.IS_NULL);
          d2.addDomainPair(Consts.LE,Consts.LE);
          if (colProperties[i].getColumnType()==Column.TYPE_LINK ||
              colProperties[i].getColumnType()==Column.TYPE_BUTTON ||
              colProperties[i].getColumnType()==Column.TYPE_FORMATTED_TEXT ||
              colProperties[i].getColumnType()==Column.TYPE_LOOKUP ||
              colProperties[i].getColumnType()==Column.TYPE_MULTI_LINE_TEXT ||
              colProperties[i].getColumnType()==Column.TYPE_TEXT)
            d2.addDomainPair(ClientSettings.LIKE,Consts.LIKE);
          d2.addDomainPair(Consts.LT,Consts.LT);
          d2.addDomainPair(Consts.NEQ,Consts.NEQ);
          if (ClientSettings.INCLUDE_IN_OPERATOR) {
            d2.addDomainPair(Consts.IN,Consts.IN);
            d2.addDomainPair(Consts.NOT_IN, Consts.NOT_IN);
          }

          final ComboBoxControl colOpsComboBox2 = new ComboBoxControl();
          colOpsComboBox2.setDomain(d2);
          if (colOpValue!=null)
            colOpsComboBox2.setValue(colOpValue);

          final JComponent c2 = createValueComponent(colProperties[i]);
          c2.addFocusListener(new OpFocusListener(filterColOps.size(),c2));

          fPanel.add(colOpsComboBox2,
                     new GridBagConstraints(1, row, 1, 1, 0.0, 0.0
                  ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
          aux = new JPanel();
          aux.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
          aux.add(c2,null);
          fPanel.add(aux,
                     new GridBagConstraints(2, row, 1, 1, 1.0, 0.0
                  ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

          cc = new JComponent[]{c2};

          if (colValue instanceof ArrayList) {
            ArrayList values = (ArrayList)colValue;
            setValueComponent(c2,values.get(0),colProperties[i]);
            for(int j=1;j<values.size();j++) {
              JComponent newc = createValueComponent(colProperties[i]);
              newc.addFocusListener(new OpFocusListener(filterColOps.size(),newc));
              JComponent[] newcc = new JComponent[cc.length+1];
              System.arraycopy(cc,0,newcc,0,cc.length);
              newcc[newcc.length-1] = newc;
              cc = newcc;
              aux.add(newc,null);
              setValueComponent(newc,values.get(j),colProperties[i]);
            }
          }
          else
            setValueComponent(c2,colValue,colProperties[i]);


          colOpsComboBox2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
              if (e.getStateChange()==e.SELECTED) {
                if (colOpsComboBox2.getValue().equals(Consts.IS_NOT_NULL) ||
                    colOpsComboBox2.getValue().equals(Consts.IS_NULL))
                  c2.setEnabled(false);
                else
                  c2.setEnabled(true);
              }
            }
          });

          filterColNames.add(colProperties[i]);
          filterColOps.add(colOpsComboBox2);
          filterColValues.add(cc);

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
      Domain domain = null;
      if (((ComboColumn)colProperties).getDomainId()!=null)
        domain = ClientSettings.getInstance().getDomain(((ComboColumn)colProperties).getDomainId());
      else
        domain = ((ComboColumn)colProperties).getDomain();
      DomainPair[] pairs = domain.getDomainPairList();
      Vector items = new Vector();
      items.add( "" );
      for(int i=0;i<pairs.length;i++) {
        items.add(ClientSettings.getInstance().getResources().getResource(pairs[i].getDescription()));
      }
      final JComboBox list=new JComboBox(items);
      JPanel listPanel = new JPanel() {
        public void addFocusListener(FocusListener l) {
          if (list!=null)
            list.addFocusListener(l);
        }
      };
      listPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
      listPanel.add(list,null);
      return listPanel;
    }
    else if (colProperties.getColumnType()==Column.TYPE_CHECK) {
      final JCheckBox check = new JCheckBox();
      JPanel listPanel = new JPanel();
      listPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
      listPanel.add(check,null);
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
      case Column.TYPE_PROGRESS_BAR : {
        NumericControl num = new NumericControl();
        if (colProperties!=null) {
          try {
            num.setMaxValue(((ProgressBarColumn)colProperties).getMaxValue());
            num.setMinValue(((ProgressBarColumn)colProperties).getMinValue());
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
      case Column.TYPE_LINK:
      {
        TextControl edit = new TextControl();
        try {
        } catch (ClassCastException ex) {
            Logger.error(this.getClass().getName(),"createValueComponent","Error while creating an input control for the filter",ex);
        }
        result=edit;
      }
      ;break;
      case Column.TYPE_FORMATTED_TEXT:
      {
        FormattedTextBox edit = new FormattedTextBox( ((FormattedTextColumn)colProperties).getController() );
        edit.setFormatterFactory( ((FormattedTextColumn)colProperties).getFormatterFactory() );
        edit.setFormatter( ((FormattedTextColumn)colProperties).getFormatter() );
        edit.setPreferredSize(new Dimension(100,20));
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
      else {
        Domain domain = null;
        if (((ComboColumn)colProperties).getDomainId()!=null)
          domain = ClientSettings.getInstance().getDomain(((ComboColumn)colProperties).getDomainId());
        else
          domain = ((ComboColumn)colProperties).getDomain();
        DomainPair[] pairs = domain.getDomainPairList();
        for(int i=0;i<pairs.length;i++)
          if (pairs[i].getCode().equals(initValue)) {
            combo.setSelectedIndex(i+1);
            break;
          }
      }
      return;
    }

    if (colProperties.getColumnType()==Column.TYPE_CHECK) {
      JCheckBox check = (JCheckBox)((JPanel)result).getComponent(0);
      if (initValue!=null)
        check.setSelected(
          initValue.equals(((CheckBoxColumn)colProperties).getPositiveValue())
        );
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
      case Column.TYPE_PROGRESS_BAR: {
        ((NumericControl)result).setValue(initValue==null?null:new Double(initValue.toString()));
      }
      ;break;
      case Column.TYPE_CURRENCY: {
        ((CurrencyControl)result).setValue(initValue==null?null:new Double(initValue.toString()));
      }

      ;break;
      case Column.TYPE_TEXT:
      case Column.TYPE_LOOKUP:
      case Column.TYPE_LINK:
      {
        ((TextControl)result).setValue(initValue==null?"":initValue.toString());
      }
      ;break;
      case Column.TYPE_FORMATTED_TEXT:
      {
        ((JFormattedTextField)result).setValue(initValue==null?"":initValue);
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
    Object value = getInternalValueComponent(result,colProperties);

    // check for type conversion...
    Class clazz = grid.getVOListTableModel().getColumnClass(grid.getVOListTableModel().findColumn(colProperties.getColumnName()));
    return ClientUtils.convertObject(value,clazz);
  }


  /**
   * @return filter value contained in the specified input control, created according to the specified column type
   */
  private Object getInternalValueComponent(JComponent result,Column colProperties) {
    if (colProperties.getColumnType()==Column.TYPE_COMBO) {
      Domain domain = null;
      if (((ComboColumn)colProperties).getDomainId()!=null)
        domain = ClientSettings.getInstance().getDomain(((ComboColumn)colProperties).getDomainId());
      else
        domain = ((ComboColumn)colProperties).getDomain();
      DomainPair[] pairs = domain.getDomainPairList();
      result = (JComboBox)((JPanel)result).getComponent(0);
      for(int i=0;i<pairs.length;i++)
        if (ClientSettings.getInstance().getResources().getResource(pairs[i].getDescription()).equals(((JComboBox)result).getSelectedItem()))
          return pairs[i].getCode();
      return null;
    }
    else if (colProperties.getColumnType()==Column.TYPE_CHECK) {
      result = (JCheckBox)((JPanel)result).getComponent(0);
      if (result!=null && ((JCheckBox)result).isSelected())
        return ((CheckBoxColumn)colProperties).getPositiveValue();
      else
        return ((CheckBoxColumn)colProperties).getNegativeValue();
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
      case Column.TYPE_PROGRESS_BAR: {
        return ((NumericControl)result).getValue();
      }
      case Column.TYPE_CURRENCY: {
        return ((NumericControl)result).getValue();
      }
      case Column.TYPE_TEXT:
      case Column.TYPE_LINK:
        return ((TextControl)result).getValue();
      case Column.TYPE_FORMATTED_TEXT:
        try {
          ( (JFormattedTextField) result).commitEdit();
          return ( (JFormattedTextField) result).getValue();
        }
        catch (ParseException ex) {
          return null;
        }
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
          ((ComboBoxControl)sortVersus.get(i)).getSelectedIndex()>0) {
        colName = ((ComboModel)((JComboBox)sortColNames.get(i)).getModel()).getSelectedCode(((JComboBox)sortColNames.get(i)).getSelectedIndex());

        alreadyExist = grid.getCurrentSortedColumns().contains(colName);
        if (alreadyExist)
          continue;

        if (((ComboBoxControl)sortVersus.get(i)).getSelectedIndex()==1)
          versus = Consts.ASC_SORTED;
        else if (((ComboBoxControl)sortVersus.get(i)).getSelectedIndex()==2)
          versus = Consts.DESC_SORTED;
        orderColumns.put(colName,new String[]{colName,versus});
        grid.getCurrentSortedColumns().add(colName);
        grid.getCurrentSortedVersusColumns().add(versus);
      } // end if inside for

    Object value = null;
    String op = null;
    grid.getQuickFilterValues().clear();
    JComponent[] cc = null;
    for(int i=0;i<filterColOps.size();i++) { // for each applyable filter...
      cc = (JComponent[])filterColValues.get(i);
      if (((ComboBoxControl)filterColOps.get(i)).getSelectedIndex()>0 &&
          (((ComboBoxControl)filterColOps.get(i)).getValue().toString().equals(Consts.IN) ||
           ((ComboBoxControl)filterColOps.get(i)).getValue().toString().equals(Consts.NOT_IN))) {
        // "IN" operator has been selected:
        // FilterWhereClause will always contain an ArrayList of objects
        ArrayList values = new ArrayList();
        for(int j=0;j<cc.length;j++) {
          value = getValueComponent(cc[j], (Column) filterColNames.get(i));
          if (value!=null && !"".equals(value))
            values.add(value);
        }
        if (values.size()>0) {
          // at least one filter value has been specified...
          colName = ((Column)filterColNames.get(i)).getColumnName();
          for(int j=0;j<values.size();j++)
            values.set(j,grid.getGridController().beforeFilterGrid(colName,values.get(j)));
          op = ((ComboBoxControl)filterColOps.get(i)).getValue().toString();
          FilterWhereClause whereClause = new FilterWhereClause(colName,op,values);
          filterColumns.put(colName,whereClause);

          if (grid.getQuickFilterValues().get(colName)==null)
            grid.getQuickFilterValues().put(colName,new FilterWhereClause[]{whereClause,null});
          else
            ((FilterWhereClause[])grid.getQuickFilterValues().get(colName))[1] = whereClause;
        }
      }
      else if (((ComboBoxControl)filterColOps.get(i)).getSelectedIndex()>0 &&
          !((ComboBoxControl)filterColOps.get(i)).getValue().toString().equals(Consts.NOT_IN) &&
          !((ComboBoxControl)filterColOps.get(i)).getValue().toString().equals(Consts.IN) &&
          !((ComboBoxControl)filterColOps.get(i)).getValue().toString().equals(Consts.IS_NOT_NULL) &&
          !((ComboBoxControl)filterColOps.get(i)).getValue().toString().equals(Consts.IS_NULL)) {
        // operator is not "IN" and is not "IS NULL" and is not IS NOT NULL:
        // FilterWhereClause could contain an ArrayList of objects or directly an onject
        ArrayList values = new ArrayList();
        for(int j=0;j<cc.length;j++) {
          value = getValueComponent(cc[j], (Column) filterColNames.get(i));
          if (value!=null && !"".equals(value))
            values.add(value);
        }
        if (values.size()>1) {
          // an OR logical connector is required...
          colName = ((Column)filterColNames.get(i)).getColumnName();
          for(int j=0;j<values.size();j++)
            values.set(j,grid.getGridController().beforeFilterGrid(colName,values.get(j)));
          op = ((ComboBoxControl)filterColOps.get(i)).getValue().toString();
          FilterWhereClause whereClause = new FilterWhereClause(colName,op,values);
          filterColumns.put(colName,whereClause);

          if (grid.getQuickFilterValues().get(colName)==null)
            grid.getQuickFilterValues().put(colName,new FilterWhereClause[]{whereClause,null});
          else
            ((FilterWhereClause[])grid.getQuickFilterValues().get(colName))[1] = whereClause;
        }
        else if (((ComboBoxControl)filterColOps.get(i)).getSelectedIndex()>0 &&
            getValueComponent(cc[0],(Column)filterColNames.get(i))!=null) { // a filter value has been specified
          // filter value has been specified...
          colName = ((Column)filterColNames.get(i)).getColumnName();
          value = getValueComponent(cc[0],(Column)filterColNames.get(i));
          value = grid.getGridController().beforeFilterGrid(colName,value);
          op = ((ComboBoxControl)filterColOps.get(i)).getValue().toString();
          FilterWhereClause whereClause = new FilterWhereClause(colName,op,value);
          filterColumns.put(colName,whereClause);

          if (grid.getQuickFilterValues().get(colName)==null)
            grid.getQuickFilterValues().put(colName,new FilterWhereClause[]{whereClause,null});
          else
            ((FilterWhereClause[])grid.getQuickFilterValues().get(colName))[1] = whereClause;
        }
      }
      else if (((ComboBoxControl)filterColOps.get(i)).getSelectedIndex()>0 && // an operator has been selected
               (((ComboBoxControl)filterColOps.get(i)).getValue().toString().equals(Consts.IS_NOT_NULL) ||
                ((ComboBoxControl)filterColOps.get(i)).getValue().toString().equals(Consts.IS_NULL))) {
        // operator is "IS NULL "or "IS NOT NULL"...
        colName = ((Column)filterColNames.get(i)).getColumnName();
        op = ((ComboBoxControl)filterColOps.get(i)).getValue().toString();
        FilterWhereClause whereClause = null;
        whereClause = new FilterWhereClause(colName,((ComboBoxControl)filterColOps.get(i)).getValue().toString(),null);
        filterColumns.put(colName,whereClause);

        if (grid.getQuickFilterValues().get(colName)==null)
          grid.getQuickFilterValues().put(colName,new FilterWhereClause[]{whereClause,null});
        else
          ((FilterWhereClause[])grid.getQuickFilterValues().get(colName))[1] = whereClause;
      }
    } // end for

    // repaint icons in column headers...
    grid.updateColumnHeaderIcons();

    // reload grid...
    grid.reload();
  }


  public final GenericButton getClosePanel() {
    return closePanel;
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to listen focus events in filter fields.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @version 1.0
   */
  class OpFocusListener extends FocusAdapter {

    private int pos;
    private JComponent field;


    public OpFocusListener(int pos,JComponent field) {
      this.pos = pos;
      this.field = field;
    }


    /**
     * FocusGained event in filter value field.
     */
    public void focusGained(FocusEvent e) {
      // scrolls horizontal scrollbar to the current filter field...
      filterScrollPane.getHorizontalScrollBar().setValue(field.getLocation().x);
    }

    /**
     * FocusLost event in filter value field.
     */
    public void focusLost(FocusEvent e) {
      String op = ((ComboBoxControl)filterColOps.get(pos)).getValue().toString();
      if (!op.equals(Consts.IS_NULL) && !op.equals(Consts.IS_NOT_NULL) &&
          (op.equals(Consts.IN) || op.equals(Consts.NOT_IN) || ClientSettings.ALLOW_OR_OPERATOR)) {
        // allows to add another filter field only if:
        // operator is not IS NULL and is not IS NOT NULL and
        // the selected operator is IN or NOT IN or OR logical connector is allowable
        JComponent[] cc = (JComponent[])filterColValues.get(pos);
        JPanel parentPanel = (JPanel)field.getParent();
        Object value = getValueComponent(field, (Column) filterColNames.get(pos));
        if (value!=null &&
            !"".equals(value) &&
            field.equals(cc[cc.length-1])) {
          // current filter field is the last and
          // its value is not null
          final JComponent c = createValueComponent((Column) filterColNames.get(pos));
          c.addFocusListener(new OpFocusListener(pos,c));
          JComponent[] newcc = new JComponent[cc.length+1];
          System.arraycopy(cc,0,newcc,0,cc.length);
          newcc[newcc.length-1] = c;
          filterColValues.set(pos,newcc);

          parentPanel.add(c,null);
          c.revalidate();
          c.repaint();
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              filterScrollPane.getHorizontalScrollBar().setValue(filterScrollPane.getHorizontalScrollBar().getMaximum()-filterScrollPane.getHorizontalScrollBar().getVisibleAmount());
              c.requestFocus();
            }
          });
        }
        else if ((value==null || "".equals(value)) && cc.length>1) {
          // there are more than one filters and
          // current filter value is null
          if (parentPanel!=null) {
            parentPanel.remove(field);
            parentPanel.revalidate();
            parentPanel.repaint();
          }

          for(int j=0;j<cc.length;j++)
            if (cc[j].equals(field)) {
              JComponent[] newcc = new JComponent[cc.length-1];
              System.arraycopy(cc,0,newcc,0,j);
              if (cc.length-1>j)
                System.arraycopy(cc,j+1,newcc,j,cc.length-j-1);
              filterColValues.set(pos,newcc);
              break;
            }
        }
      }
    }

  } // end inner class




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



  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to redirect key event to the inner JFormattedTextField.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class FormattedTextBox extends JFormattedTextField implements FormatterController{

    /** formatter controller */
    private FormatterController controller;

    public FormattedTextBox(FormatterController controller) {
      this.controller = controller;
    }



//      public FormattedTextBox() {
//        super();
//        try {
//          setFormatter(new javax.swing.text.MaskFormatter("###"));
//        }
//        catch (ParseException ex) {
//        }
//      }

      public void processKeyEvent(KeyEvent e) {
        try {
          super.processKeyEvent(e);
        }
        catch (Exception ex) {
        }
      }


      /**
       * Invoked when the user inputs an invalid value.
       */
      public void invalidEdit() {
        try {
          if (controller == null) {
            super.invalidEdit();
          }
          else {
            controller.invalidEdit();
          }
        }
        catch (Exception ex) {
        }
      }


      /**
       * Sets the current AbstractFormatter.
       * @param format formatter to set
       */
      public void setFormatter(JFormattedTextField.AbstractFormatter format) {
        try {
          if (controller == null) {
            if (getFormatterFactory()==null)
              super.setFormatterFactory(new DefaultFormatterFactory(format));
            else
              super.setFormatter(format);

          }
          else {
            controller.setFormatter(format);
          }
        }
        catch (Exception ex) {
        }
      }


  }




}
