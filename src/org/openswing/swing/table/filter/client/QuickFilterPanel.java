package org.openswing.swing.table.filter.client;

import java.math.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.table.client.ListFilterController;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import javax.swing.text.DefaultFormatterFactory;
import java.text.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JCalendar;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Quick filter panel, associated to the grid.</p>
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
public class QuickFilterPanel extends JPanel implements MenuElement, MenuContainer, KeyListener {

    private JLabel label1 = new JLabel();
    private JLabel label2 = new JLabel();
    private JComponent value1 = null;
    private JComponent value2 = null;
    private JToggleButton rangeButton = null;
    private JButton filterButton = null;
    private JPopupMenu parentPopup=null;
    private Icon filterIcon = null;

    public  static final int FILTER_TYPE_VALUE=0;
    public  static final int FILTER_TYPE_RANGE=1;

    private String attributeName=null;
    private int filterType=FILTER_TYPE_VALUE;
    private Object initValue=null;
    private Column colProperties=null;
    private Domain domain=null;

    private String EQUALS = ClientSettings.getInstance().getResources().getResource("equals");
    private String CONTAINS = ClientSettings.getInstance().getResources().getResource("contains");
    private String STARTS_WITH = ClientSettings.getInstance().getResources().getResource("starts with");
    private String ENDS_WITH = ClientSettings.getInstance().getResources().getResource("ends with");

    /** list of filter criteria */
    private JList opType = new JList(new String[] {
      EQUALS,
      CONTAINS,
      STARTS_WITH,
      ENDS_WITH
    });

    private JScrollPane opTypeScrollPane = new JScrollPane();

    private QuickFilterListener filterListener=null;

    /** default value that could be set in the quick filter criteria; values allowed: Consts.EQUALS,Consts.CONTAINS,Consts.STARTS_WITH,Consts.ENDS_WITH */
    private int defaultQuickFilterCriteria;

    /** combo-box filter to apply to column headers (optional) */
    private ListFilterController comboFilterController = null;

    /** filtering button icon */
    private Icon listIcon = new ImageIcon(ClientUtils.getImage(ClientSettings.LIST_FILTER_BUTTON));

    /** list button */
    private JButton listButton = null;

    /** domain to bind to checkbox list control to show when pressing combo button (optional)  */
    private Domain listControlDomain = null;

    /** used to show/hide checkbox list control (optional) */
    private boolean showListControl = false;

    /** checkbox list control (optional) */
    private CheckBoxListControl checkBoxListControl = new CheckBoxListControl();

    /** current applied filter */
    private FilterWhereClause[] filter = null;


    /**
     * Costructor called by Grid object.
     * @param defaultQuickFilterCriteria default value that could be set in the quick filter criteria; values allowed: Consts.EQUALS,Consts.CONTAINS,Consts.STARTS_WITH,Consts.ENDS_WITH
     * @param filterListener listener that manages filter events (the grid)
     * @param filterType type of filter: FILTER_TYPE_VALUE or FILTER_TYPE_RANGE (for date, number, ...)
     * @param colProperties column properties
     * @param initValue initial value to set into the filter
     */
    public QuickFilterPanel(
        ListFilterController comboFilterController,
        int defaultQuickFilterCriteria,
        QuickFilterListener filterListener,
        int filterType,
        Column colProperties,
        FilterWhereClause[] filter,
        Object initValue) {
      this.comboFilterController = comboFilterController;
      this.filterListener=filterListener;
      this.filterType=filterType;
      this.defaultQuickFilterCriteria = defaultQuickFilterCriteria;
      this.filter = filter;

      this.addComponentListener(new ComponentListener() {
      /**
       * componentHidden
       *
       * @param e ComponentEvent
       */
      public void componentHidden(ComponentEvent e) {
        new Thread().dumpStack();
      }

      /**
       * componentMoved
       *
       * @param e ComponentEvent
       */
      public void componentMoved(ComponentEvent e) {
      }

      /**
       * componentResized
       *
       * @param e ComponentEvent
       */
      public void componentResized(ComponentEvent e) {
      }

      /**
       * componentShown
       *
       * @param e ComponentEvent
       */
      public void componentShown(ComponentEvent e) {
      }
    });

      this.addAncestorListener(new AncestorListener() {
      /**
       * ancestorAdded
       *
       * @param event AncestorEvent
       */
      public void ancestorAdded(AncestorEvent event) {
      }

      /**
       * ancestorMoved
       *
       * @param event AncestorEvent
       */
      public void ancestorMoved(AncestorEvent event) {
      }

      /**
       * ancestorRemoved
       *
       * @param event AncestorEvent
       */
      public void ancestorRemoved(AncestorEvent event) {
        new Thread().dumpStack();
      }
    });

      ApplicationEventQueue.getInstance().addKeyListener(this);

      filterIcon = new ImageIcon(ClientUtils.getImage("filter.gif"));
      rangeButton= new JToggleButton(new ImageIcon(ClientUtils.getImage("chiuso.gif")),false) {

        /**
         * Method available in java 1.5
         */
        protected void processMouseEvent(MouseEvent e) {
          if (e.getID()==e.MOUSE_CLICKED) {
            if (QuickFilterPanel.this.filterType==FILTER_TYPE_VALUE) {
              QuickFilterPanel.this.filterType=FILTER_TYPE_RANGE;
              rangeButton.setIcon(new ImageIcon(ClientUtils.getImage("aperto.gif")));
            } else {
              QuickFilterPanel.this.filterType=FILTER_TYPE_VALUE;
              rangeButton.setIcon(new ImageIcon(ClientUtils.getImage("chiuso.gif")));
            }

            updateComponents(); // update filter panel...

            if (parentPopup!=null) {
              parentPopup.setVisible(false);
              parentPopup.setVisible(true);
              parentPopup.setSelected(value1);
            }
            if (!value1.hasFocus())
              value1.requestFocus();
          }
        }

      };
      rangeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (QuickFilterPanel.this.filterType==FILTER_TYPE_VALUE) {
            QuickFilterPanel.this.filterType=FILTER_TYPE_RANGE;
            rangeButton.setIcon(new ImageIcon(ClientUtils.getImage("aperto.gif")));
          }
          else {
            QuickFilterPanel.this.filterType=FILTER_TYPE_VALUE;
            rangeButton.setIcon(new ImageIcon(ClientUtils.getImage("chiuso.gif")));
          }
          updateComponents(); // update filter panel...

          if (parentPopup!=null) {
            parentPopup.setVisible(false);
            parentPopup.setVisible(true);
            parentPopup.setSelected(value1);
          }
          if (!value1.hasFocus())
            value1.requestFocus();

        }
      });
      filterButton=new JButton(filterIcon) {

        /**
         * Method available in java 1.5
         */
        protected void processMouseEvent(MouseEvent e) {
          if (e.getID()==e.MOUSE_CLICKED)
            filter();
        }

      };

      filterButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          filter();
        }
      });


      listButton=new JButton(listIcon) {

        /**
         * Method available in java 1.5
         */
        protected void processMouseEvent(MouseEvent e) {
          if (e.getID()==e.MOUSE_CLICKED)
            showComboBoxFilter();
        }

      };

      listButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          showComboBoxFilter();
        }
      });

      setFilterConfig(filterType,colProperties,initValue);

      if (comboFilterController!=null &&
          filter!=null &&
          filter[1]==null &&
          filter[0].getValue()!=null &&
          filter[0].getValue() instanceof java.util.List) {
        java.util.List values = (java.util.List)filter[0].getValue();
        showComboBoxFilter();
        checkBoxListControl.setValue(values);
      }
    }


    /**
     * Show list filter for the specified column.
     */
    private void showComboBoxFilter() {

      if (listControlDomain==null) {
        if (colProperties.getColumnType()==Column.TYPE_COMBO) {
          listControlDomain = ((ComboColumn)colProperties).getDomain();
        }
        else {
          // load data to fill in list domain...
          Response res = comboFilterController.getListControlValues(attributeName);
          if (res.isError()) {
            OptionPane.showMessageDialog(
                ClientUtils.getParentWindow(this),
                ClientSettings.getInstance().getResources().getResource("Error while loading data")+":\n"+res.getErrorMessage(),
                ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
                JOptionPane.ERROR_MESSAGE
            );
            return;
          }
          else {
            java.util.List rows = ((VOListResponse)res).getRows();
            listControlDomain = new Domain("DOMAIN_LIST_FILTER_"+attributeName);
            for(int i=0;i<rows.size();i++)
              listControlDomain.addDomainPair(rows.get(i),rows.get(i).toString());
          }
          checkBoxListControl.setTranslateItemDescriptions(false);
        }
        checkBoxListControl.setDomain(listControlDomain);
      }

      if (!showListControl) {
        showListControl = true;
        this.add(checkBoxListControl,             new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0
          ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 0, 0, 0), 0, 0));
        this.remove(label1);
        this.remove(label2);
        this.remove(rangeButton);
        this.remove(value1);
        this.remove(value2);
        this.remove(opTypeScrollPane);
        if (parentPopup!=null) {
          parentPopup.setVisible(false);
          parentPopup.setVisible(true);
        }
      }
      else {
        showListControl = false;
        layoutComponents();
        if (parentPopup!=null) {
          parentPopup.setVisible(false);
          parentPopup.setVisible(true);
        }
        if (!value1.hasFocus())
          value1.requestFocus();
      }
    }


    public final void setVisible(boolean v) {
      super.setVisible(v);
      if (!v)
        ApplicationEventQueue.getInstance().removeKeyListener(this);
    }


    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode()==e.VK_ESCAPE) {
        parentPopup.getParent();
        parentPopup.setVisible(false);
      }
    }


    public void keyTyped(KeyEvent e) {
    }


    public void keyReleased(KeyEvent e) {
    }



    /**
     * Listener linked to the JList object.
     */
    class ListAdjustmentListener implements AdjustmentListener {
      JList theList=null;
      public ListAdjustmentListener(JList theList) {
        this.theList=theList;
      }

      public void adjustmentValueChanged(AdjustmentEvent e) {
        if (e.getValueIsAdjusting())
          return;
        if (theList.getHeight()>0 && e.getValue()>0) {
          theList.setSelectedIndex((e.getValue()+20)/20);
        }
         else
          theList.setSelectedIndex(0);
      }
    }


    /**
     * Create an input control used to set the filter value, according to the column type.
     */
    private JComponent createValueComponent() {
      JComponent result=null;


      if (domain!=null) {

        Vector couple = null;
        DomainPair[] pairs = domain.getDomainPairList();
        Object[] items = new Object[pairs.length];
        int selIndex = -1;
        for(int i=0;i<items.length;i++) {
          items[i] = ClientSettings.getInstance().getResources().getResource(pairs[i].getDescription());
          if (initValue!=null && pairs[i].getCode().equals(initValue))
            selIndex = i;
        }
        final JList list=new JList(items);
        final int selectedIndex = selIndex;
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            list.setSelectedIndex(selectedIndex);
            list.ensureIndexIsVisible(selectedIndex);
          }
        });
        list.setToolTipText("");
        list.setVisibleRowCount(1);
        JScrollPane scrollPane=new JScrollPane();
        scrollPane.getViewport().add(list, null);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new ListAdjustmentListener(list));
        list.addKeyListener(new KeyAdapter() {
          public void keyReleased(KeyEvent e) {
            if (e.getKeyCode()==e.VK_ENTER)
              valueKeyPressed(e);
          }
        } );
        scrollPane.addKeyListener(new KeyAdapter() {
          public void keyReleased(KeyEvent e) {
            if (e.getKeyCode()==e.VK_ENTER)
              valueKeyPressed(e);
          }
        } );
        list.addKeyListener(new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            if (e.getKeyCode()==e.VK_ENTER)
              valueKeyPressed(e);
          }
        });

        return scrollPane;
      }

      switch (colProperties.getColumnType()) {

        case Column.TYPE_DATE :
        case Column.TYPE_DATE_TIME :
        case Column.TYPE_TIME :
        {
          result=new DateControl();

          ((DateControl)result).setDateType(colProperties.getColumnType());
          final DateControl res = ((DateControl)result);
          final JDateChooser dc = (JDateChooser)result.getComponents()[0];
          dc.getCalendarButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
/*
              System.out.println("parentPopup.getLocationOnScreen().x="+parentPopup.getLocationOnScreen().x);
              System.out.println("parentPopup.getLocationOnScreen().y="+parentPopup.getLocationOnScreen().y);

              System.out.println("dc.getCalendarButton().getX()="+dc.getCalendarButton().getX());
              System.out.println("dc.getCalendarButton().getY()="+dc.getCalendarButton().getY());

              System.out.println("res.getLocationOnScreen().x="+res.getLocationOnScreen().x);
              System.out.println("res.getLocationOnScreen().y="+res.getLocationOnScreen().y);
*/
              int x = res.getLocationOnScreen().x;
              int y = res.getLocationOnScreen().y+res.getHeight();
              Calendar calendar = Calendar.getInstance();
              Date date = dc.getDate();
              if (date != null) {
                      calendar.setTime(date);
              }
              dc.getJCalendar().setCalendar(calendar);

              final JWindow w = new JWindow(parentPopup.getGraphicsConfiguration());
              w.getContentPane().add(dc.getJCalendar(),BorderLayout.CENTER);
              w.setLocation(x,y);
              w.setSize(dc.getJCalendar().getPreferredSize());
              parentPopup.addPopupMenuListener(new PopupMenuListener() {

                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                  w.setVisible(false);
                  w.dispose();
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                  w.setVisible(false);
                  w.dispose();
                }
              });

              w.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                  if (e.getKeyCode()==e.VK_ESCAPE) {
                    w.setVisible(false);
                    w.dispose();
                }

                }
              });

              dc.getJCalendar().getDayChooser().addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("day")) {
                                if (w.isVisible()) {
                                        w.setVisible(false);
                                        res.setDate(dc.getJCalendar().getCalendar().getTime());
                                }
                        } else if (evt.getPropertyName().equals("date")) {
                          /*
                                if (evt.getSource() == dateEditor) {
                                        firePropertyChange("date", evt.getOldValue(), evt.getNewValue());
                                } else */
                                {
                                        res.setDate((Date) evt.getNewValue());
                                }
                        }
                }

              });



              w.setVisible(true);


              throw new RuntimeException();
            }
          });



          if (initValue!=null) {
            Date c = (java.util.Date)initValue;
            ((DateControl)result).setDate(c);
          } else {
            Date c = new Date();
            ((DateControl)result).setDate(c);
          }
          result.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
              if (e.getKeyCode()==e.VK_ENTER)
                valueKeyPressed(e);
            }
          } );
        }
        ;break;

        case Column.TYPE_CHECK :
        {
          result=new CheckBoxControl();
          if (initValue!=null) {
            ((CheckBoxControl)result).setValue(initValue);
          } else {
            ((CheckBoxControl)result).setValue(Boolean.TRUE);
          }
          result.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
              if (e.getKeyCode()==e.VK_ENTER || e.getKeyCode()==e.VK_SPACE)
                valueKeyPressed(e);
            }
          } );
        }
        ;break;

        case Column.TYPE_INT:
        {
          NumericControl num = new NumericControl();
          num.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
              valueKeyPressed(e);
            }
          } );
          if (colProperties!=null) { // if there exists some column properties, then use it...
            try {
              num.setDecimals(0);
              num.setMaxValue(((IntegerColumn)colProperties).getMaxValue());
              num.setMinValue(((IntegerColumn)colProperties).getMinValue());
            } catch (ClassCastException ex) {
              Logger.error(this.getClass().getName(),"QuickFilterPanel","Error while creating input field of type integer",ex);
            }
          }
          num.setText(initValue==null?null:initValue.toString());
          result=num;
        }
        ;break;
        case Column.TYPE_DEC :
        case Column.TYPE_PERC: {
          NumericControl num = new NumericControl();
          num.setPreferredSize(new Dimension(100, 20));
          num.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
              valueKeyPressed(e);
            }
          } );
          if (colProperties!=null) { // if there exists some column properties, then use it...
            try {
              num.setDecimals(((DecimalColumn)colProperties).getDecimals());
              num.setMaxValue(((DecimalColumn)colProperties).getMaxValue());
              num.setMinValue(((DecimalColumn)colProperties).getMinValue());
            } catch (ClassCastException ex) {
              Logger.error(this.getClass().getName(),"QuickFilterPanel","Error while creating input field of type decimal",ex);
            }
          }
          num.setText(initValue==null?null:initValue.toString());
          result=num;
        }
        ;break;
        case Column.TYPE_PROGRESS_BAR: {
          NumericControl num = new NumericControl();
          num.setPreferredSize(new Dimension(100, 20));
          num.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
              valueKeyPressed(e);
            }
          } );
          if (colProperties!=null) { // if there exists some column properties, then use it...
            try {
              num.setMaxValue(((ProgressBarColumn)colProperties).getMaxValue());
              num.setMinValue(((ProgressBarColumn)colProperties).getMinValue());
            } catch (ClassCastException ex) {
              Logger.error(this.getClass().getName(),"QuickFilterPanel","Error while creating input field of type decimal",ex);
            }
          }
          num.setText(initValue==null?null:initValue.toString());
          result=num;
        }
        ;break;
        case Column.TYPE_CURRENCY: {
          CurrencyControl num = new CurrencyControl();
          num.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
              valueKeyPressed(e);
            }
          } );
          if (colProperties!=null) { // if there exists some column properties, then use it...
            try {
              num.setDecimals(((CurrencyColumn)colProperties).getDecimals());
              num.setMaxValue(((CurrencyColumn)colProperties).getMaxValue());
              num.setMinValue(((CurrencyColumn)colProperties).getMinValue());
            } catch (ClassCastException ex) {
              Logger.error(this.getClass().getName(),"QuickFilterPanel","Error while creating input field of type currency",ex);
            }
          }
          num.setText(initValue==null?null:initValue.toString());
          result=num;
        }
        ;break;
        case Column.TYPE_TEXT:
        {
          TextControl edit = new TextControl();
          edit.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
              if (e.getKeyCode()==e.VK_ENTER)
                valueKeyPressed(e);
            }
          } );
          try {
            edit.setMaxCharacters(((TextColumn)colProperties).getMaxCharacters());
          } catch (ClassCastException ex) {
            Logger.error(this.getClass().getName(),"createValueComponent","Column property not supported",ex);
          }
          edit.setText(initValue==null?"":initValue.toString());
          result=edit;
        }
        ;break;
        case Column.TYPE_LINK:
        {
          TextControl edit = new TextControl();
          edit.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
              if (e.getKeyCode()==e.VK_ENTER)
                valueKeyPressed(e);
            }
          } );
          edit.setText(initValue==null?"":initValue.toString());
          result=edit;
        }
        ;break;
        case Column.TYPE_FORMATTED_TEXT:
        {
          FormattedTextBox edit = new FormattedTextBox( ((FormattedTextColumn)colProperties).getController() );
          edit.setFormatterFactory( ((FormattedTextColumn)colProperties).getFormatterFactory() );
          edit.setFormatter( ((FormattedTextColumn)colProperties).getFormatter() );
          edit.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
              if (e.getKeyCode()==e.VK_ENTER)
                valueKeyPressed(e);
            }
          } );
          edit.setValue(initValue==null?"":initValue);
          result=edit;
        }
        ;break;
        case Column.TYPE_LOOKUP:
        {
          TextControl edit = new TextControl();
          edit.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
              if (e.getKeyCode()==e.VK_ENTER)
                valueKeyPressed(e);
            }
          } );
          try {
            edit.setMaxCharacters(((CodLookupColumn)colProperties).getMaxCharacters());
          } catch (ClassCastException ex) {
            Logger.error(this.getClass().getName(),"createValueComponent","Column property not supported",ex);
          }
          edit.setText(initValue==null?"":initValue.toString());
          result=edit;
        }
        ;break;
        default: {
          Logger.error(this.getClass().getName(),"createValueComponent","Column property not supported",null);
        }
      }

      result.setPreferredSize(new Dimension(120, 20));
      result.setMinimumSize(new Dimension(120, 20));

      return result;
    }


    /**
     * Add components to the panel.
     */
    private void layoutComponents() {
      this.removeAll();
      this.add(label1,       new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
      this.add(label2,      new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
      opType.setVisibleRowCount(1);
      opTypeScrollPane.getViewport().removeAll();
      opTypeScrollPane.getViewport().add(opType);

      if (colProperties.getColumnType()==Column.TYPE_LOOKUP ||
          colProperties.getColumnType()==Column.TYPE_TEXT)
        this.add(opTypeScrollPane,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));
      this.add(value1,        new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));
      this.add(value2,      new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));
      this.add(rangeButton,             new GridBagConstraints(3, 0, 1, 1, 10.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));

      if (comboFilterController!=null) {
        this.add(listButton,       new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
      }

      this.add(filterButton,       new GridBagConstraints(5, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));

      opTypeScrollPane.getVerticalScrollBar().addAdjustmentListener(new ListAdjustmentListener(opType));
    }


    private void jbInit() throws Exception {
      label1.setPreferredSize(new Dimension(70, 20));
      label1.setMinimumSize(new Dimension(70, 20));

      this.setLayout(new GridBagLayout());
      label2.setText(ClientSettings.getInstance().getResources().getResource("To value"));
      value1=createValueComponent();
      value2=createValueComponent();

      rangeButton.setSelectedIcon(new ImageIcon(ClientUtils.getImage("aperto.gif")));
      rangeButton.setContentAreaFilled(false);
      rangeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
      rangeButton.setPreferredSize(new Dimension(20, 20));
      rangeButton.setMaximumSize(new Dimension(20, 20));
      rangeButton.setMinimumSize(new Dimension(20, 20));
      rangeButton.setBorderPainted(false);
      rangeButton.setSelected(filterType==FILTER_TYPE_RANGE);

      filterButton.setContentAreaFilled(false);
      filterButton.setPreferredSize(new Dimension(20, 20));
      filterButton.setMinimumSize(new Dimension(20, 20));
      filterButton.setMaximumSize(new Dimension(20, 20));
      filterButton.setBorderPainted(false);
      filterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

      listButton.setContentAreaFilled(false);
      listButton.setPreferredSize(new Dimension(20, 20));
      listButton.setMinimumSize(new Dimension(20, 20));
      listButton.setMaximumSize(new Dimension(20, 20));
      listButton.setBorderPainted(false);
      listButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

      layoutComponents();
      updateComponents();
      if (!value1.hasFocus())
        value1.requestFocus();
    }

    /**
     * Update filter icons.
     */
    private void updateComponents() {
      switch (filterType) {
        case FILTER_TYPE_VALUE:
          label1.setText(ClientSettings.getInstance().getResources().getResource("Filter by"));
          label2.setVisible(false);
          value2.setVisible(false);
        ;break;
        case FILTER_TYPE_RANGE:
          label1.setText(ClientSettings.getInstance().getResources().getResource("From value"));
          label2.setVisible(true);
          value2.setVisible(true);
        ;break;
      }
    }


    public JPopupMenu getParentPopup() {
      return parentPopup;
    }


    public void setParentPopup(JPopupMenu parentPopup) {
      this.parentPopup = parentPopup;
    }


    /**
     * Set the content of the filter panel, according to the filter type, column type and the initial value.
     */
    private void setFilterConfig(int filterType,Column colProperties,Object initValue) {
      this.attributeName=colProperties.getColumnName();
      this.filterType = filterType;
      this.initValue = initValue;
      this.colProperties = colProperties;
      if (colProperties.getColumnType()==Column.TYPE_COMBO) {
        if (((ComboColumn)colProperties).getDomainId()!=null)
          domain = ClientSettings.getInstance().getDomain(((ComboColumn)colProperties).getDomainId());
        else
          domain = ((ComboColumn)colProperties).getDomain();
      }
      else
        domain=null;

      try {
        jbInit();

        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            opType.ensureIndexIsVisible(defaultQuickFilterCriteria);
            opType.setSelectedIndex(defaultQuickFilterCriteria);
          }
        });
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }

    }


    /**
     * @return filter type; possible values: FILTER_TYPE_VALUE or FILTER_TYPE_RANGE (for date, number, ...)
     */
    public final int getFilterType() {
      return filterType;
    }


    /**
     * Dispatch focus to the input field.
     */
    public final void requestFocus() {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          if (!value1.hasFocus()) {

            // patch introduced to allow focus setting on the input control when popup menu is a child of a JFrame (no MDI frame)...
            java.awt.Component window = value1;
            while (window!=null && !(window instanceof Window)) {
                window = window.getParent();
            }
            if (window != null || !((Window)window).isFocusableWindow()) {
                ((Window)window).setFocusableWindowState(true);
            }

            value1.requestFocus();
          }
        }
      });
    }


    /**
     * @param value input field
     * @return value of the input field
     */
    private Object getValue(JComponent value) {
      if (showListControl) {
        return checkBoxListControl.getValue();
      }


      Object result=null;
      if (domain!=null) {
        int idx= ((JList)((JScrollPane)value).getViewport().getComponent(0)).getSelectedIndex();
        DomainPair[] pairs = domain.getDomainPairList();
        return pairs[idx].getCode();
      }


      switch (colProperties.getColumnType()) {
        case Column.TYPE_DATE :
        case Column.TYPE_DATE_TIME :
        case Column.TYPE_TIME :
        {
          if (((DateControl)value).getDate()==null)
            result = null;
          else if (colProperties.getColumnType()==Column.TYPE_DATE)
            result=new java.sql.Date(((DateControl)value).getDate().getTime());
          else
            result=new java.sql.Timestamp(((DateControl)value).getDate().getTime());
        }
        ;break;
        case Column.TYPE_CHECK:
        {
          result = ((CheckBoxControl)value).getValue();
          if (result==null || result instanceof Boolean && !((Boolean)result).booleanValue())
            result = ((CheckBoxColumn)colProperties).getNegativeValue();
          else
            result = ((CheckBoxColumn)colProperties).getPositiveValue();

        }
        ;break;
        case Column.TYPE_INT:
        {
          result = (BigDecimal) ((NumericControl)value).getValue();
        }
        ;break;
        case Column.TYPE_DEC :
        case Column.TYPE_PERC:
        {
          result = (BigDecimal) ((NumericControl)value).getValue();
        }
        ;break;
        case Column.TYPE_PROGRESS_BAR:
        {
          result = (BigDecimal) ((NumericControl)value).getValue();
        }
        ;break;
        case Column.TYPE_CURRENCY:
        {
          result = (BigDecimal) ((CurrencyControl)value).getValue();
        }
        ;break;
        case Column.TYPE_TEXT:
        case Column.TYPE_LINK:
        case Column.TYPE_LOOKUP:
        {
          result=((TextControl)value).getText();
        }
        ;break;
        case Column.TYPE_FORMATTED_TEXT:
        {
          try {
            ( (JFormattedTextField) value).commitEdit();
            result = ( (JFormattedTextField) value).getValue();
          }
          catch (ParseException ex) {
            result = null;
          }
        }
        ;break;
        default:
          result=null;
      }

      return result;
    }

    public Object getValue1() {
      return getValue(value1);
    }


    public Object getValue2() {
      if (showListControl) {
        return null;
      }

      Object result=getValue(value2);
      if (result instanceof Date) {
        Calendar c=GregorianCalendar.getInstance();
        c.setTime((Date)result);
//        c.set(c.HOUR,23);
//        c.set(c.MINUTE,59);
//        c.set(c.SECOND,59);
//        c.set(c.MILLISECOND,999);
        c.set(c.DAY_OF_MONTH,c.get(c.DAY_OF_MONTH)+1);
        c.set(c.HOUR,0);
        c.set(c.MINUTE,0);
        c.set(c.SECOND,0);
        c.set(c.MILLISECOND,0);

        if (colProperties.getColumnType()==Column.TYPE_DATE)
          result=new java.sql.Date(c.getTime().getTime());
        else
          result=new java.sql.Timestamp(c.getTime().getTime());
      }
      return result;
    }


    private void valueKeyPressed(KeyEvent e) {
      if (e.getKeyCode()==e.VK_ENTER) {
        filter();
        if (parentPopup!=null) {
          parentPopup.setVisible(false);
        }
      }
    }


    /**
     * Method called when user click the filter buttons.
     */
    private void filter() {
      int type = colProperties.getColumnType();
      if (filterType==FILTER_TYPE_VALUE) {
        Object value = getValue1();
        if (value!=null &&
            value.toString().indexOf("%")==-1 &&
            opType.getSelectedIndex()>0 &&
            domain==null &&
            !(type==Column.TYPE_DATE ||
              type==Column.TYPE_DATE_TIME ||
              type==Column.TYPE_TIME ||
              type==Column.TYPE_INT ||
              type==Column.TYPE_DEC ||
              type==Column.TYPE_PERC ||
              type==Column.TYPE_CURRENCY)
        ) {
          if (opType.getSelectedValue().equals(CONTAINS))
            value = "%"+value+"%";
          else if (opType.getSelectedValue().equals(STARTS_WITH))
            value = value+"%";
          else if (opType.getSelectedValue().equals(ENDS_WITH))
            value = "%"+value;
        }

        QuickFilterPanel.this.filterListener.filter(colProperties,value,null);
      } else
        QuickFilterPanel.this.filterListener.filter(colProperties,getValue1(),getValue2());
    }


    public void processMouseEvent(MouseEvent event, MenuElement[] path, MenuSelectionManager manager) {
      return;
    }


    public void processKeyEvent(KeyEvent event, MenuElement[] path, MenuSelectionManager manager) {
      return;
    }


    public void menuSelectionChanged(boolean isIncluded) {
      if (!isIncluded)
        transferFocus();
       else
        requestFocus();
    }


    public MenuElement[] getSubElements() {
      return null;
    }


    public Component getComponent() {
      return this;
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
