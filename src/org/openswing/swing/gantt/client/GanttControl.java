package org.openswing.swing.gantt.client;

import java.beans.*;
import java.math.*;
import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.gantt.java.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that contains a grid on the left side (the gantt legend) and a gantt diagram on the right.
 * Each row of the gannt is defined by a GanttRowVO object, that contains the legend values for the row and a set of Appointment objects.
 * When the set of Appointment objects are read, this control analyze the objects and
 * split an Appointment in more than one, if the Appointment is spreaded on more days, so that each new Appointment is bound inside a day.
 * After editing the gantt, the set of changed Appointments of a specified row can be retrieved
 * by means of "getAppointments" method that analyzes Appointment objects: if there exists two consecutive Appointments,
 * then it joins them in a unique Appointment object, so that the method returns the minimal set of objects.</p>
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
public class GanttControl extends JPanel {

  /** starting date */
  private Date startDate = new Date();

  /** end date */
  private Date endDate = new Date();

  /** rows viewed in this panel */
  private ArrayList ganttRows = new ArrayList();

  JSplitPane splitPane = new JSplitPane();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane scrollPane1 = new JScrollPane();
  JScrollPane scrollPane2 = new JScrollPane();
  Table1Model model1 = new Table1Model();
  Table2Model model2 = new Table2Model();
  JTable table1 = new JTable();
  JTable table2 = new JTable();

  /** column width; default value: 150 pixels */
  private int columnWidth = 120;

  /** gantt data locator */
  private GanttDataLocator ganttDataLocator = null;

  /** gantt parameters */
  private Map ganttParameters = new HashMap();

  /** flag used to show the description above the colored rectangle */
  private boolean showDescription = true;

  /** flag used to show the appointment duration above the colored rectangle */
  private boolean showTime = true;

  /** flag used to load data the first time */
  private boolean firstTime = true;

  /** enable deleting of appointments; default value: <code>true</code> */
  private boolean enableDelete = true;

  /** enable inserting of appointments; default value: <code>true</code> */
  private boolean enableInsert = true;

  /** enable editing of appointments; default value: <code>true</code> */
  private boolean enableEdit = true;

  /** list of AppointmentChangeListener objects */
  private ArrayList appointmentChangeListeners = new ArrayList();

  /** column sizes for the grid (legend area); colleciton of pairs: column index,width */
  private Hashtable preferredSize = new Hashtable();

  /** collection of columns not visible in the grid (legend area) */
  private ArrayList visibility = new ArrayList();

  /** flag used to specify if data loading will be automatically performed when the gantt is set to visible; default value: "true". */
  private boolean autoLoadData = true;


  public GanttControl() {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.HOUR_OF_DAY,0);
    cal.set(cal.MINUTE,0);
    cal.set(cal.SECOND,0);
    cal.set(cal.MILLISECOND,0);
    startDate = cal.getTime();
    cal.set(cal.MONTH,cal.get(cal.MONTH)+1);
    endDate = cal.getTime();
    try {
      jbInit();
      setRowHeight(35);
//      scrollPane1.getViewport().setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
//      scrollPane2.getViewport().setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
      table1.setBackground(ClientSettings.GRID_CELL_BACKGROUND);
      table2.setBackground(ClientSettings.GRID_CELL_BACKGROUND);
      table1.getTableHeader().setReorderingAllowed(false);
      table1.setSelectionBackground(ClientSettings.GRID_SELECTION_BACKGROUND);
      table1.setSelectionForeground(ClientSettings.GRID_SELECTION_FOREGROUND);
      table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      table2.setShowVerticalLines(false);
      table2.setDefaultRenderer(ArrayList.class,new GanttColumnRenderer());
      table2.setDefaultEditor(ArrayList.class,new GanttColumnEditor());
      table2.getTableHeader().setResizingAllowed(false);
      table2.getTableHeader().setReorderingAllowed(false);
      table2.getColumnModel().setColumnMargin(0);

      // add mouse listener to set focus to the selected cell and to show the pop-up menu
      // used to fill in the whole day or remove all day appointments...
      table2.addMouseListener(new MouseAdapter() {

        public void mouseClicked(MouseEvent e) {
          if (!model2.isCellEditable(0,0))
            return;
          if (model2!=null &&
              model2.getRowCount()>0) {
            table2.setRowSelectionInterval(table2.rowAtPoint(e.getPoint()),table2.rowAtPoint(e.getPoint()));
            table2.setColumnSelectionInterval(table2.columnAtPoint(e.getPoint()),table2.columnAtPoint(e.getPoint()));
            table2.editCellAt(table2.rowAtPoint(e.getPoint()),table2.columnAtPoint(e.getPoint()));
          }

          if (model2!=null &&
              model2.getRowCount()>0 &&
              table2.getSelectedRow()!=-1 &&
              table2.getSelectedColumn()!=-1 &&
              SwingUtilities.isRightMouseButton(e)) {
            JPopupMenu p = new JPopupMenu();

            if (enableInsert) {
              JMenuItem sel = new JMenuItem(ClientSettings.getInstance().getResources().getResource("fill in the whole day"));
              p.add(sel);
              sel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  // add a new appointment for the selected day...
                  GanttRowVO ganttRow = (GanttRowVO) ganttRows.get(table2.getSelectedRow());
                  ArrayList value = (ArrayList)model2.getValueAt(table2.getSelectedRow(),table2.getSelectedColumn());
                  Appointment a,olda;
                  if (value!=null) {
                    long t1 = startDate.getTime()+86400000L*((long)table2.getSelectedColumn());
                    long t2 = t1+86400000L; // -1 ???
                    for(int i=0;i<value.size();i++) {
                      a = (Appointment)value.get(i);
                      olda = (Appointment)a.clone();
                      if (a.getStartDate().getTime()<t1 &&
                          a.getEndDate().getTime()<=t2 &&
                          enableEdit &&
                          a.isEnableEdit()) {
                        a.setEndDate(new java.sql.Timestamp(t1));
                        a.setDuration(new BigDecimal(getTime(a,table2.getSelectedRow(),table2.getSelectedColumn())));
                        fireAppointmentChangedEvent(table2.getSelectedRow(),olda,a);
                      }
                      else if (a.getEndDate().getTime()>t2 &&
                               a.getStartDate().getTime()>=t1 &&
                               enableEdit &&
                               a.isEnableEdit()) {
                        a.setStartDate(new java.sql.Timestamp(t2));
                        a.setDuration(new BigDecimal(getTime(a,table2.getSelectedRow(),table2.getSelectedColumn())));
                        fireAppointmentChangedEvent(table2.getSelectedRow(),olda,a);
                      }
                      else if (a.getEndDate().getTime()<=t2 &&
                               a.getStartDate().getTime()>=t1 &&
                               enableDelete &&
                               a.isEnableDelete()) {
                        ganttRow.getAppointments().remove(a);
                        fireAppointmentDeletedEvent(table2.getSelectedRow(),a);
                      }
                    }
                  }

                  try {
                    a = (Appointment) ganttRow.getAppointmentClass().newInstance();
                    a.setDescription("");
                    Calendar cal = Calendar.getInstance();
                    GanttWorkingHours whVO = getWhVO(table2.getSelectedRow(),table2.getSelectedColumn());
                    if (whVO==null || whVO.getMorningStartHour()==null || whVO.getAfternoonStartHour()==null) {
                      OptionPane.showMessageDialog(
                          GanttControl.this,
                          ClientSettings.getInstance().getResources().getResource("no working hours defined for this day"),
                          ClientSettings.getInstance().getResources().getResource("Attention"),
                          JOptionPane.ERROR_MESSAGE
                      );
                      return;
                    }

                    table2.getCellEditor().stopCellEditing();

                    cal.setTimeInMillis(whVO.getMorningStartHour().getTime());
                    int h = cal.get(cal.HOUR_OF_DAY);
                    int m = cal.get(cal.MINUTE);
                    cal.setTimeInMillis(startDate.getTime()+86400000L*(long)table2.getSelectedColumn());
                    cal.set(cal.HOUR_OF_DAY,h);
                    cal.set(cal.MINUTE,m);
                    cal.set(cal.SECOND,0);
                    cal.set(cal.MILLISECOND,0);
                    a.setStartDate(new java.sql.Timestamp(cal.getTimeInMillis()));

                    cal.setTimeInMillis(whVO.getMorningEndHour().getTime());
                    h = cal.get(cal.HOUR_OF_DAY);
                    m = cal.get(cal.MINUTE);
                    cal.setTimeInMillis(startDate.getTime()+86400000L*(long)table2.getSelectedColumn());
                    cal.set(cal.HOUR_OF_DAY,h);
                    cal.set(cal.MINUTE,m);
                    cal.set(cal.SECOND,0);
                    cal.set(cal.MILLISECOND,0);
                    a.setEndDate(new java.sql.Timestamp(cal.getTimeInMillis()));
                    a.setDuration(new BigDecimal(getTime(a,table2.getSelectedRow(),table2.getSelectedColumn())));
                    ganttRow.getAppointments().add(a);
                    fireNewAppointmentEvent(table2.getSelectedRow(),a);

                    a = (Appointment) ganttRow.getAppointmentClass().newInstance();
                    a.setDescription("");

                    cal.setTimeInMillis(whVO.getAfternoonStartHour().getTime());
                    h = cal.get(cal.HOUR_OF_DAY);
                    m = cal.get(cal.MINUTE);
                    cal.setTimeInMillis(startDate.getTime()+86400000L*(long)table2.getSelectedColumn());
                    cal.set(cal.HOUR_OF_DAY,h);
                    cal.set(cal.MINUTE,m);
                    cal.set(cal.SECOND,0);
                    cal.set(cal.MILLISECOND,0);
                    a.setStartDate(new java.sql.Timestamp(cal.getTimeInMillis()));

                    cal.setTimeInMillis(whVO.getAfternoonEndHour().getTime());
                    h = cal.get(cal.HOUR_OF_DAY);
                    m = cal.get(cal.MINUTE);
                    cal.setTimeInMillis(startDate.getTime()+86400000L*(long)table2.getSelectedColumn());
                    cal.set(cal.HOUR_OF_DAY,h);
                    cal.set(cal.MINUTE,m);
                    cal.set(cal.SECOND,0);
                    cal.set(cal.MILLISECOND,0);
                    a.setEndDate(new java.sql.Timestamp(cal.getTimeInMillis()));
                    ganttRow.getAppointments().add(a);
                    a.setDuration(new BigDecimal(getTime(a,table2.getSelectedRow(),table2.getSelectedColumn())));
                    fireNewAppointmentEvent(table2.getSelectedRow(),a);

                    table2.tableChanged(new TableModelEvent(
                        model2,
                        table2.getSelectedRow(),
                        table2.getSelectedRow(),
                        table2.getSelectedColumn()
                        ));

                  }
                  catch (Throwable ex) {
                    ex.printStackTrace();
                  }
                }
              });
            }

            if (enableDelete) {
              JMenuItem desel = new JMenuItem(ClientSettings.getInstance().getResources().getResource("remove all day appointments"));
              p.add(desel);
              desel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  // delete appointments in the selected day...
                  ArrayList value = (ArrayList)model2.getValueAt(table2.getSelectedRow(),table2.getSelectedColumn());
                  GanttRowVO ganttRow = (GanttRowVO) ganttRows.get(table2.getSelectedRow());
                  table2.getCellEditor().stopCellEditing();
                  Appointment a,olda;

                  if (value!=null) {
                    long t1 = startDate.getTime()+86400000L*((long)table2.getSelectedColumn());
                    long t2 = t1+86400000L; // -1 ???
                    for(int i=0;i<value.size();i++) {
                      a = (Appointment)value.get(i);
                      olda = (Appointment)a.clone();
                      if (a.getStartDate().getTime()<t1 &&
                          a.getEndDate().getTime()<=t2 &&
                          enableEdit &&
                          a.isEnableEdit()) {
                        a.setEndDate(new java.sql.Timestamp(t1));
                        a.setDuration(new BigDecimal(getTime(a,table2.getSelectedRow(),table2.getSelectedColumn())));
                        fireAppointmentChangedEvent(table2.getSelectedRow(),olda,a);
                      }
                      else if (a.getEndDate().getTime()>t2 &&
                               a.getStartDate().getTime()>=t1 &&
                               enableEdit &&
                               a.isEnableEdit()) {
                        a.setStartDate(new java.sql.Timestamp(t2));
                        a.setDuration(new BigDecimal(getTime(a,table2.getSelectedRow(),table2.getSelectedColumn())));
                        fireAppointmentChangedEvent(table2.getSelectedRow(),olda,a);
                      }
                      else if (a.getEndDate().getTime()<=t2 &&
                               a.getStartDate().getTime()>=t1 &&
                               enableDelete &&
                               a.isEnableDelete()) {
                        ganttRow.getAppointments().remove(a);
                        fireAppointmentDeletedEvent(table2.getSelectedRow(),a);
                      }
                    }
                  }

                  table2.tableChanged(new TableModelEvent(
                      model2,
                      table2.getSelectedRow(),
                      table2.getSelectedRow(),
                      table2.getSelectedColumn()
                  ));

                }
              });
            }

            p.show(e.getComponent(),e.getX(),e.getY());
          }

        }
      });



      init();

      scrollPane2.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent e) {
          table2.repaint();
        }
      });
      scrollPane1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent e) {
          if (scrollPane2.getVerticalScrollBar().getValue()!=scrollPane1.getVerticalScrollBar().getValue())
            scrollPane2.getVerticalScrollBar().setValue(scrollPane1.getVerticalScrollBar().getValue());
        }
      });
      scrollPane2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent e) {
          if (scrollPane1.getVerticalScrollBar().getValue()!=scrollPane2.getVerticalScrollBar().getValue())
            scrollPane1.getVerticalScrollBar().setValue(scrollPane2.getVerticalScrollBar().getValue());
        }
      });

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Set start date
   * @param startDate start date
   */
  public final void setStartDate(Date startDate) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(startDate);
    cal.set(cal.HOUR_OF_DAY,0);
    cal.set(cal.MINUTE,0);
    cal.set(cal.SECOND,0);
    cal.set(cal.MILLISECOND,0);
    this.startDate = cal.getTime();
    init(model1.getColumnNames());
  }


  /**
   * Set end date.
   * @param endDate end date
   */
  public final void setEndDate(Date endDate) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(endDate);
    cal.set(cal.HOUR_OF_DAY,0);
    cal.set(cal.MINUTE,0);
    cal.set(cal.SECOND,0);
    cal.set(cal.MILLISECOND,0);
    this.endDate = cal.getTime();
    init(model1.getColumnNames());
  }


  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setDividerSize(1);
    table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.add(splitPane,  BorderLayout.CENTER);
    splitPane.add(scrollPane1, JSplitPane.TOP);
    splitPane.add(scrollPane2, JSplitPane.BOTTOM);
    scrollPane2.getViewport().add(table2, null);
    scrollPane1.getViewport().add(table1, null);
    splitPane.setDividerLocation(150);
  }


  /**
   * Create the grid content with no grid on the left.
   */
  private void init() {
    init(new String[0]);
  }


  /**
   * Create the grid content.
   * @param columnNames column identifiers for the grid on the left
   */
  public void init(String[] columnNames) {
    ganttRows.clear();
    boolean lastEnabled = model1.isEnabled();
    model1 = new Table1Model();
    model1.resetModel(columnNames);
    model1.setEnabled(lastEnabled);
    lastEnabled = model2.isEnabled();
    model2 = new Table2Model();
    model2.resetModel();
    model2.setEnabled(lastEnabled);
    for(int i=0;i<table2.getColumnModel().getColumnCount();i++)
      table2.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
    table1.repaint();
    table2.repaint();
    if (columnNames.length==0)
      splitPane.setDividerLocation(0);
    else if (splitPane.getDividerLocation()==0)
      splitPane.setDividerLocation(200);
  }


  /**
   * Set columns width.
   * @param width column width
   */
  public final void setColumnWidth(int width) {
    this.columnWidth = columnWidth;
    for(int i=0;i<table2.getColumnModel().getColumnCount();i++)
      table2.getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
    table2.repaint();
  }


  /**
   * @return column width
   */
  public final int getColumnWidth() {
    return columnWidth;
  }



  /**
   * Method called by the JVM to load gannt.
   */
  public final void addNotify() {
  super.addNotify();
    if (!Beans.isDesignTime() && this.isVisible() && firstTime) {
      firstTime = false;
      if (autoLoadData)
        reloadData();
    }
  }


  /**
   * Load data in the gantt panel.
   */
  public void reloadData() {
    ganttRows.clear();
    table1.repaint();
    table2.repaint();
    Response res = ganttDataLocator.loadData(ganttParameters);
    if (res.isError()) {
      OptionPane.showMessageDialog(
          this,
          ClientSettings.getInstance().getResources().getResource("Error while loading data")+":\n"+ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
          ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else {
      java.util.List rows = ( (VOListResponse) res).getRows();
      GanttRowVO vo = null;
      for(int i=0;i<rows.size();i++) {
        vo = (GanttRowVO)rows.get(i);
        addRow(vo);
      }
    }
  }



  /**
   * Add a row to the panel (grid on the left + gantt).
   */
  public final void addRow(GanttRowVO ganttRow) {
    ganttRows.add(ganttRow);
    table1.revalidate();
    table2.revalidate();
    table1.repaint();
    table2.repaint();
  }



  /**
   * Set gantt editability.
   * @param enabled <code>true</code> if the gantt is editable, <code>false</code> otherwise
   */
  public final void setGanttEnabled(boolean enabled) {
    model2.setEnabled(enabled);
    table2.repaint();
  }


  /**
   * @return gantt editability
   */
  public final boolean isGanttEnabled() {
    return model2.isEnabled();
  }



  /**
   * Set editability for the grid on the left.
   * @param enabled <code>true</code> if the grid on the left is editable, <code>false</code> otherwise
   */
  public final void setGridEnabled(boolean enabled) {
    model1.setEnabled(enabled);
    if (enabled)
      table1.setBackground(ClientSettings.GRID_CELL_BACKGROUND);
    else
      table1.setBackground(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);

    table1.repaint();
  }


  /**
   * @return editability for the grid on the left
   */
  public final boolean isGridEnabled() {
    return model1.isEnabled();
  }


  /**
   * Set row height
   * @param height row height
   */
  public final void setRowHeight(int height) {
    table1.setRowHeight(height);
    table2.setRowHeight(height);
  }


  /**
   * @return row height
   */
  public final int getRowHeight() {
    return table1.getRowHeight();
  }


  /**
   * Set divider location.
   * @param location divider location
   */
  public final void setDividerLocation(int location) {
    splitPane.setDividerLocation(location);
  }


  /**
   * @return divider location
   */
  public final int getDividerLocation() {
    return splitPane.getDividerLocation();
  }


  /**
   * @return gantt data locator
   */
  public final GanttDataLocator getGanttDataLocator() {
    return ganttDataLocator;
  }


  /**
   * Set the gantt data locator.
   * @param ganttDataLocator gantt data locator
   */
  public final void setGanttDataLocator(GanttDataLocator ganttDataLocator) {
    this.ganttDataLocator = ganttDataLocator;
  }


  /**
   * @return gantt parameters
   */
  public final Map getGanttParameters() {
    return ganttParameters;
  }


  /**
   * Set the gantt parameters.
   * @param ganttParameters gantt parameters
   */
  public final void setGanttParameters(Map ganttParameters) {
    this.ganttParameters = ganttParameters;
  }


  /**
   * @param row row in the gantt diagram
   * @return set of Appointment objects defined in the specified row
   */
  public final HashSet getAppointments(int row) {
    GanttRowVO vo = (GanttRowVO)ganttRows.get(row);
    return vo.getAppointments();
  }


  /**
   * @return gantt rows number
   */
  public final int getRowCount() {
    return ganttRows.size();
  }


  /**
   * @return show the description above the colored rectangle
   */
  public final boolean isShowDescription() {
    return showDescription;
  }


  /**
   * Show the description above the colored rectangle.
   * @param showDescription show the description above the colored rectangle
   */
  public final void setShowDescription(boolean showDescription) {
    this.showDescription = showDescription;
  }


  /**
   * @return start date in the gantt
   */
  public final Date getStartDate() {
    return startDate;
  }


  /**
   * @return end date in the gantt
   */
  public final Date getEndDate() {
    return endDate;
  }


  private GanttWorkingHours getWhVO(int row,int column) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new java.util.Date(startDate.getTime()+(long)column*86400000L));
    GanttWorkingHours whVO = null;
    if (cal.get(cal.DAY_OF_WEEK)==cal.SUNDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getSundayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.MONDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getMondayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.TUESDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getTuesdayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.WEDNESDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getWednesdayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.THURSDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getThursdayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.FRIDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getFridayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.SATURDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getSaturdayWorkingHours();
    return whVO;
  }


  /**
   * @return enable deleting of appointments
   */
  public final boolean isEnableDelete() {
    return enableDelete;
  }


  /**
   * Enable deleting of appointments.
   * @param enableDelete enable deleting of appointments
   */
  public final void setEnableDelete(boolean enableDelete) {
    this.enableDelete = enableDelete;
  }


  /**
   * @return enable inserting of appointments
   */
  public final boolean isEnableInsert() {
    return enableInsert;
  }


  /**
   * Enable inserting of appointments.
   * @param enableInsert enable inserting of appointments
   */
  public final void setEnableInsert(boolean enableInsert) {
    this.enableInsert = enableInsert;
  }


  /**
   * @return show the appointment duration above the colored rectangle
   */
  public final boolean isShowTime() {
    return showTime;
  }


  /**
   * Used to show the appointment duration above the colored rectangle.
   * @param showTime show the appointment duration above the colored rectangle
   */
  public final void setShowTime(boolean showTime) {
    this.showTime = showTime;
  }


  /**
   * Add a AppointmentChangeListener listener to the current gantt control.
   * @param listener listener to add
   */
  public final void addAppointmentChangeListener(AppointmentChangeListener listener) {
    appointmentChangeListeners.add(listener);
  }


  /**
   * Remove the specified AppointmentChangeListener listener from the current gantt control.
   * @param listener listener to remove
   */
  public final void removeAppointmentChangeListener(AppointmentChangeListener listener) {
    appointmentChangeListeners.remove(listener);
  }


  /**
   * Fire a new appointment event.
   * @param rowNumber row number in the GanttControl
   * @param appointment Appointment that fires the event
   */
  protected void fireNewAppointmentEvent(int rowNum,Appointment app) {
    for(int i=0;i<appointmentChangeListeners.size();i++) {
      ((AppointmentChangeListener)appointmentChangeListeners.get(i)).appointmentChange(new AppointmentChangeEvent(
        AppointmentChangeEvent.NEW_APPOINTMENT,
        rowNum,
        app,
        this
      ));
    }
  }


  /**
   * Fire an appointment changed event.
   * @param rowNumber row number in the GanttControl
   * @param appointment Appointment that fires the event
   */
  protected void fireAppointmentChangedEvent(int rowNum,Appointment oldapp,Appointment newapp) {
    for(int i=0;i<appointmentChangeListeners.size();i++) {
      ((AppointmentChangeListener)appointmentChangeListeners.get(i)).appointmentChange(new AppointmentChangeEvent(
        AppointmentChangeEvent.APPOINTMENT_CHANGED,
        rowNum,
        oldapp,
        newapp,
        this
      ));
    }
  }


  /**
   * Fire an appointment deleted event.
   * @param rowNumber row number in the GanttControl
   * @param appointment Appointment that fires the event
   */
  protected void fireAppointmentDeletedEvent(int rowNum,Appointment app) {
    for(int i=0;i<appointmentChangeListeners.size();i++) {
      ((AppointmentChangeListener)appointmentChangeListeners.get(i)).appointmentChange(new AppointmentChangeEvent(
        AppointmentChangeEvent.APPOINTMENT_DELETED,
        rowNum,
        app,
        this
      ));
    }
  }


  /**
   * @param rowNumber row number
   * @param colIndex column index
   * @return Object[] grid content (in the legend area), related to the specified row number
   */
  public final Object getValueAt(int rowNumber,int colIndex) {
    return model1.getValueAt(rowNumber,colIndex);
  }


  /**
   * Set column width.
   * @param columnIndex column index in the grid (legend area)
   * @param width column width to set
   */
  public final void setGridColumnWidth(int columnIndex,int width) {
    table1.getColumnModel().getColumn(columnIndex).setPreferredWidth(width);
    preferredSize.put(new Integer(columnIndex),new Integer(width));
  }


  /**
   * Set column visibility.
   * @param columnIndex column index in the grid (legend area)
   * @param visible column visibility
   */
  public final void setGridColumnVisible(int columnIndex,boolean visible) {
    if (!visible) {
      table1.getColumnModel().removeColumn(table1.getColumnModel().getColumn(columnIndex));
      visibility.add(new Integer(columnIndex));
    }
  }


  /**
   * @return appointment duration (in minutes(
   */
  private long getTime(Appointment a,int row,int column) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new java.util.Date(startDate.getTime()+(long)column*86400000L));
    GanttWorkingHours whVO = null;
    if (cal.get(cal.DAY_OF_WEEK)==cal.SUNDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getSundayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.MONDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getMondayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.TUESDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getTuesdayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.WEDNESDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getWednesdayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.THURSDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getThursdayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.FRIDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getFridayWorkingHours();
    else if (cal.get(cal.DAY_OF_WEEK)==cal.SATURDAY)
      whVO = ((GanttRowVO)ganttRows.get(row)).getSaturdayWorkingHours();

    long m1 = startDate.getTime()+((long)column)*86400000L;
    long m2 = startDate.getTime()+((long)column)*86400000L;
    long a1 = startDate.getTime()+((long)column)*86400000L+86400000L;
    long a2 = startDate.getTime()+((long)column)*86400000L+86400000L;
    if (whVO!=null) {
      if (whVO.getMorningStartHour()!=null) {
        cal.setTime(whVO.getMorningStartHour());
        m1 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:0L);
      }
      if (whVO.getMorningEndHour()!=null) {
        cal.setTime(whVO.getMorningEndHour());
        m2 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:0L);
      }
      if (whVO.getAfternoonStartHour()!=null) {
        cal.setTime(whVO.getAfternoonStartHour());
        a1 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:86400000L);
      }
      if (whVO.getAfternoonEndHour()!=null) {
        cal.setTime(whVO.getAfternoonEndHour());
        a2 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:86400000L);
      }
    }

    long d1 = 0;
    long d2 = 0;
    long d3 = 0;
    long d4 = 0;
    long delta = 0;
    if (a.getStartDate().getTime()<=m1) {
      d1 = m1;
      if (a.getEndDate().getTime()<=m2)
        d2 = a.getEndDate().getTime();
      else {
        d2 = m2;
        if (a.getEndDate().getTime()>=a1) {
          d3 = a1;
          if (a.getEndDate().getTime()<=a2)
            d4 = a.getEndDate().getTime();
          else {
            d4 = a2;
            delta = getTime(a,row,column+1);
            if (delta<0)
              delta = 0;
          }
        }
        else {

        }
      }
    }
    else {
      if (a.getStartDate().getTime()>m2) {
        if (a.getStartDate().getTime()>a1) {
          d3 = a.getStartDate().getTime();
          if (a.getEndDate().getTime()<a2)
            d4 = a.getEndDate().getTime();
          else {
            d4 = a2;
            delta = getTime(a,row,column+1);
            if (delta<0)
              delta = 0;
          }
        }
        else {
          if (a1==a2)
            d3 = a.getStartDate().getTime();
          else
            d3 = a1;
          if (a.getEndDate().getTime()<a2)
            d4 = a.getEndDate().getTime();
          else {
            d4 = a2;
            delta = getTime(a,row,column+1);
            if (delta<0)
              delta = 0;
          }
        }
      }
      else {
        d1 = a.getStartDate().getTime();
        if (a.getEndDate().getTime()<=m2)
          d2 = a.getEndDate().getTime();
        else {
          if (a.getEndDate().getTime()<a1)
            d2 = m2;
          else {
            d2 = m2;
            d3 = a1;
            if (a.getEndDate().getTime()<=a2)
              d4 = a.getEndDate().getTime();
            else {
              d4 = a2;
              delta = getTime(a,row,column+1);
              if (delta<0)
                delta = 0;
            }
          }
        }
      }
    }

    return delta+(d2-d1+d4-d3)/60000L;
  }


  /**
   * @return enable editing of appointments
   */
  public final boolean isEnableEdit() {
    return enableEdit;
  }

  /**
   * enable editing of appointments; default value: <code>true</code>.
   * @param enableEdit enable editing of appointments
   */
  public final void setEnableEdit(boolean enableEdit) {
    this.enableEdit = enableEdit;
  }


  /**
   * @return <code>true</code> to automatically load data when the gantt is showed, <code>false</code> otherwise
   */
  public final boolean isAutoLoadData() {
    return this.autoLoadData;
  }


  /**
   * Define if grid must be automatically loaded when it is showed.
   * @param autoLoadData <code>true</code> to automatically load data when the gantt is showed, <code>false</code> otherwise
   */
  public final void setAutoLoadData(boolean autoLoadData) {
    this.autoLoadData = autoLoadData;
  }


















  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to define the table model linked to the grid on the left.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class Table1Model extends AbstractTableModel {

    /** column identifiers */
    private String[] cols = new String[0];

    /** flag used to set grid editability */
    private boolean enabled = true;


    public Table1Model() { }


    /**
     * @return column names
     */
    public final String[] getColumnNames() {
      return cols;
    }


    /**
     * Set grid editability.
     * @param enabled <code>true</code> if the grid is editable, <code>false</code> otherwise
     */
    public final void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }


    /**
     * return grid editability
     */
    public final boolean isEnabled() {
      return this.enabled;
    }


    /**
     * Recalculate date interval and rows.
     */
    public final void resetModel(String[] cols) {
      this.cols = cols;
      table1.setModel(this);
      Enumeration keys = preferredSize.keys();
      Integer colIndex = null;
      while(keys.hasMoreElements()) {
        colIndex = (Integer)keys.nextElement();
        table1.getColumnModel().getColumn(colIndex.intValue()).setPreferredWidth(((Integer)preferredSize.get(colIndex)).intValue());
      }
      for(int i=0;i<visibility.size();i++)
        table1.getColumnModel().removeColumn(table1.getColumnModel().getColumn(((Integer)visibility.get(i)).intValue()));
    }


    public int getColumnCount() {
      return cols.length;
    }


    public String getColumnName(int columnIndex) {
      return cols[columnIndex];
    }


    public int getRowCount() {
      return ganttRows.size();
    }


    public Class getColumnClass(int columnIndex) {
      return ganttRows.size()>0 && getValueAt(0,columnIndex)!=null ?
             getValueAt(0,columnIndex).getClass() :
             Object.class;
    }


    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return enabled;
    }


    public Object getValueAt(int rowIndex, int columnIndex) {
      return ((GanttRowVO)ganttRows.get(rowIndex)).getLegend()[columnIndex];
    }


    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      ((GanttRowVO)ganttRows.get(rowIndex)).getLegend()[columnIndex] = aValue;
    }


  } // end Table1Model



  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to define the table model linked to the gantt grid.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class Table2Model extends AbstractTableModel {

    /** column identifiers */
    private ArrayList dates = new ArrayList();

    /** flag used to set grid editability */
    private boolean gridEnabled = true;

    public Table2Model() { }


    /**
     * Set grid editability.
     * @param enabled <code>true</code> if the grid is editable, <code>false</code> otherwise
     */
    public final void setEnabled(boolean enabled) {
      this.gridEnabled = enabled;
    }


    /**
     * return grid editability
     */
    public final boolean isEnabled() {
      return this.gridEnabled;
    }


    /**
     * Recalculate date interval and rows.
     */
    public final void resetModel() {
      SimpleDateFormat sdf = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE));
      Calendar cal = Calendar.getInstance();
      cal.setTime(startDate);
      while(cal.getTimeInMillis()<=endDate.getTime()) {
        dates.add(sdf.format(cal.getTime()));
        cal.set(cal.DAY_OF_MONTH,cal.get(cal.DAY_OF_MONTH)+1);
      }
      table2.setModel(this);
    }


    public int getColumnCount() {
      return dates.size();
    }


    public String getColumnName(int columnIndex) {
      return (String)dates.get(columnIndex);
    }


    public int getRowCount() {
      return ganttRows.size();
    }


    public Class getColumnClass(int columnIndex) {
      return ganttRows.size()>0 && getValueAt(0,columnIndex)!=null ?
             getValueAt(0,columnIndex).getClass() :
             Object.class;
    }


    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return gridEnabled;
    }


    /**
     * @return ArrayList list of Appointment objects defined inside the specified day
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
      long t1 = startDate.getTime()+86400000L*((long)columnIndex);
      long t2 = t1+86400000L; // -1 ???
      ArrayList list = new ArrayList();
      HashSet alist = ((GanttRowVO)ganttRows.get(rowIndex)).getAppointments();
      Appointment a = null;
      Iterator it = alist.iterator();
      while(it.hasNext()) {
        a = (Appointment)it.next();
        if (a.getEndDate().getTime()>=t1 && a.getEndDate().getTime()<=t2 ||
            a.getStartDate().getTime()>=t1 && a.getStartDate().getTime()<=t2 ||
            a.getStartDate().getTime()<=t1 && a.getEndDate().getTime()>=t2)
          list.add(a);
      }
      return list;
    }


    /**
     * Set the ArrayList of Appointment objects defined inside the specified day.
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      long t1 = startDate.getTime()+86400000L*columnIndex;
      long t2 = t1+86400000L; // -1 ???
      ArrayList list = (ArrayList)aValue;
      HashSet alist = ((GanttRowVO)ganttRows.get(rowIndex)).getAppointments();
      Appointment a = null;
      Iterator it = alist.iterator();
      HashSet toRemove = new HashSet();
      while(it.hasNext()) {
        a = (Appointment)it.next();
        if (a.getEndDate().getTime()>=t1 && a.getEndDate().getTime()<=t2 ||
            a.getStartDate().getTime()>=t1 && a.getStartDate().getTime()<=t2 ||
            a.getStartDate().getTime()<=t1 && a.getEndDate().getTime()>=t2)
          toRemove.add(a);
      }
      alist.removeAll(toRemove);
      alist.addAll(list);
    }


  } // end Table2Model





  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Interface used as callback in GanttCell class.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * <p> </p>
   * @author Mauro Carniel
   * @version 1.0
   */
  interface OldAppointmentCallback {

    public void setLastDescr(String lastDescr);

    public String getLastDescr();

    public void setLastAppointment(Appointment appointment);

    public Appointment getLastAppointment();

  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to render the gantt columns (gantt bar).</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class GanttColumnRenderer extends DefaultTableCellRenderer implements OldAppointmentCallback {

    /** cell content */
    private GanttCell canvas = new GanttCell();

    private Calendar cal = Calendar.getInstance();

    /** last description */
    private String lastDescr = null;

    /** last row */
    private int lastRow = -1;

    /** last Appointment processed (viewed) */
    private Appointment appointment = null;


    public void setLastDescr(String lastDescr) {
      this.lastDescr = lastDescr;
    }


    public String getLastDescr() {
      return lastDescr;
    }


    public void setLastAppointment(Appointment appointment) {
      this.appointment = appointment;
    }

    public Appointment getLastAppointment() {
      return appointment;
    }


    public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {
      if (row!=lastRow) {
        lastDescr = null;
        lastRow = row;
      }

      long width = 86400000L;
      cal.setTime(new java.util.Date(startDate.getTime()+(long)column*86400000L));
      GanttWorkingHours whVO = null;
      if (cal.get(cal.DAY_OF_WEEK)==cal.SUNDAY)
        whVO = ((GanttRowVO)ganttRows.get(row)).getSundayWorkingHours();
      else if (cal.get(cal.DAY_OF_WEEK)==cal.MONDAY)
        whVO = ((GanttRowVO)ganttRows.get(row)).getMondayWorkingHours();
      else if (cal.get(cal.DAY_OF_WEEK)==cal.TUESDAY)
        whVO = ((GanttRowVO)ganttRows.get(row)).getTuesdayWorkingHours();
      else if (cal.get(cal.DAY_OF_WEEK)==cal.WEDNESDAY)
        whVO = ((GanttRowVO)ganttRows.get(row)).getWednesdayWorkingHours();
      else if (cal.get(cal.DAY_OF_WEEK)==cal.THURSDAY)
        whVO = ((GanttRowVO)ganttRows.get(row)).getThursdayWorkingHours();
      else if (cal.get(cal.DAY_OF_WEEK)==cal.FRIDAY)
        whVO = ((GanttRowVO)ganttRows.get(row)).getFridayWorkingHours();
      else if (cal.get(cal.DAY_OF_WEEK)==cal.SATURDAY)
        whVO = ((GanttRowVO)ganttRows.get(row)).getSaturdayWorkingHours();
      if (whVO!=null)
        width = (whVO.getMorningEndHour()!=null?(whVO.getMorningEndHour().getTime()-whVO.getMorningStartHour().getTime()):0)+
                (whVO.getAfternoonEndHour()!=null?(whVO.getAfternoonEndHour().getTime()-whVO.getAfternoonStartHour().getTime()):0);

      long m1 = startDate.getTime()+((long)column)*86400000L;
      long m2 = startDate.getTime()+((long)column)*86400000L;
      long a1 = startDate.getTime()+((long)column)*86400000L+86400000L;
      long a2 = startDate.getTime()+((long)column)*86400000L+86400000L;
      if (whVO!=null) {
        if (whVO.getMorningStartHour()!=null) {
          cal.setTime(whVO.getMorningStartHour());
          m1 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:0L);
        }
        if (whVO.getMorningEndHour()!=null) {
          cal.setTime(whVO.getMorningEndHour());
          m2 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:0L);
        }
        if (whVO.getAfternoonStartHour()!=null) {
          cal.setTime(whVO.getAfternoonStartHour());
          a1 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:86400000L);
        }
        if (whVO.getAfternoonEndHour()!=null) {
          cal.setTime(whVO.getAfternoonEndHour());
          a2 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:86400000L);
        }
      }

      canvas.setAppointments(
        (ArrayList)value,
        startDate.getTime()+((long)column)*86400000L,
        startDate.getTime()+(((long)column)+1L)*86400000L,
        width,
        m1,
        m2,
        a1,
        a2,
        row,
        this,
        column
      );
      return canvas;
    }


  } // end GanttColumnRenderer




  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Gantt Cell viewer, used by cell renderer/editor.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class GanttCell extends JPanel {

    private ArrayList appointments = new ArrayList();
    private long t1;
    private long t2;
    private long w;
    private long m1;
    private long m2;
    private long a1;
    private long a2;
    private int row;
    private OldAppointmentCallback callback;
    private int column;

    public void setAppointments(ArrayList appointments,long t1,long t2,long w,long m1,long m2,long a1,long a2,int row,OldAppointmentCallback callback,int column) {
      this.appointments = appointments;
      this.t1 = t1;
      this.t2 = t2;
      this.w = w;
      this.m1 = m1;
      this.m2 = m2;
      this.a1 = a1;
      this.a2 = a2;
      this.row = row;
      this.callback = callback;
      this.column = column;
    }


    public void paint(Graphics g) {
      super.paint(g);
      Appointment a = null;
      int x,len;
      long minutes;
      int delta;
      String time = null;
      if (model2.isEnabled())
        g.setColor(ClientSettings.GRID_CELL_BACKGROUND);
      else
        g.setColor(ClientSettings.GRID_NOT_EDITABLE_CELL_BACKGROUND);
      g.fillRect(0,0,getWidth(),getHeight());
      for(int i=0;i<appointments.size();i++) {
        a = (Appointment)appointments.get(i);
        if (a.getStartDate().getTime()<m1)
          x = 0;
        else {
          if (a.getStartDate().getTime()>m2)
            x = (int)(getWidth()*(m2-m1+a.getStartDate().getTime()-a1)/w);
          else
            x = (int)(getWidth()*(a.getStartDate().getTime()-m1)/w);
        }
        if (a.getEndDate().getTime()>a2)
          len = getWidth()-x;
        else {
          if (a.getEndDate().getTime()<=a1)
            len = (int) (getWidth() * (a.getEndDate().getTime() - m1) / w);
          else
            len = (int) (getWidth() * (m2-m1+a.getEndDate().getTime() - a1) / w); // ???
        }
        if (a.getBackgroundColor()!=null) {
          g.setColor(a.getBackgroundColor());
          g.fillRect(
              x,
              0,
              len,
              getHeight()
          );
        }
        g.setColor(a.getForegroundColor()==null?Color.black:a.getForegroundColor());
        g.fillRect(
          x,
          20,
          len,
          10
        );
        delta = 0;
//        if (showDescription && a.getDescription()!=null && !a.getDescription().equals(callback.getLastDescr()) && len>0) {
        if (showDescription && a.getDescription()!=null && len>0) {
          g.setColor(Color.black);

          if (!a.getDescription().equals(callback.getLastDescr())) {
            callback.setLastDescr(a.getDescription());
            g.drawString(
              callback.getLastDescr().length()>15?
              callback.getLastDescr().substring(0,15):
              callback.getLastDescr(),
              x+2,
              12
            );
            delta = g.getFontMetrics().stringWidth(
                callback.getLastDescr().length()>15?
                callback.getLastDescr().substring(0,15):
                callback.getLastDescr()
            )+2;
          }
          if (showTime) {
            g.setColor(Color.black);
            minutes = getTime(a,row,column);
            time = (minutes/60)+":"+(minutes%60);
            if (!a.equals(callback.getLastAppointment())) {
              callback.setLastAppointment(a);
              g.drawString("["+time+"]",x+2+delta,12);
            }
          }
        }
        if (!showDescription && showTime) {
          g.setColor(Color.black);
          minutes = getTime(a,row,column);
          time = (minutes/60)+":"+(minutes%60);
          if (!a.equals(callback.getLastAppointment())) {
            callback.setLastAppointment(a);
            g.drawString("["+time+"]",x+2+delta,12);
          }
        }

      } // end for
    }



  } // end GanttCell



  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Column editor used to resize the gantt bar.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  public class GanttColumnEditor extends AbstractCellEditor implements TableCellEditor,OldAppointmentCallback {

    private GanttCell label = new GanttCell();
    private JTable table = null;

    /** list of Appointment objects stored in the editing cell */
    private ArrayList value = null;

    /** flag used to store when the mouse is left clicked */
    private boolean isLeftMouseButtonPressed = false;

    /** x pixel stored when user press the mouse the first time */
    private long lastX = -1;

    /** column index related to the edited cell */
    private int lastCellIndex = -1;

    /** flag used to store editing cell selection */
    private boolean isSelected = false;

    /** temporary variable */
    private Calendar cal = Calendar.getInstance();

    /** last description */
    private String lastDescr = null;

    /** last row */
    private int lastRow = -1;

    /** last Appointment processed (viewed) */
    private Appointment appointment = null;


    public void setLastDescr(String lastDescr) {
      this.lastDescr = lastDescr;
    }


    public String getLastDescr() {
      return lastDescr;
    }


    public void setLastAppointment(Appointment appointment) {
      this.appointment = appointment;
    }

    public Appointment getLastAppointment() {
      return appointment;
    }


    public GanttColumnEditor() {
      label.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
          if (!table.getModel().isCellEditable(0,0))
            return;
          isLeftMouseButtonPressed = SwingUtilities.isLeftMouseButton(e);
          lastX = e.getX();
        }
        public void mouseReleased(MouseEvent e) {
          if (!table.getModel().isCellEditable(0,0))
            return;
          if (isLeftMouseButtonPressed) {
//            int currentCellIndex = table.convertColumnIndexToModel(table.getSelectedColumn());

            long width1 = 86400000L;
            long m1 = 0;
            GanttWorkingHours whVO1 = getWhVO(lastRow,lastCellIndex);
            if (whVO1!=null) {
              width1 = (whVO1.getMorningEndHour()!=null?(whVO1.getMorningEndHour().getTime()-whVO1.getMorningStartHour().getTime()):0)+
                      (whVO1.getAfternoonEndHour()!=null?(whVO1.getAfternoonEndHour().getTime()-whVO1.getAfternoonStartHour().getTime()):0);

              cal.setTime(whVO1.getMorningStartHour());
              m1 = cal.get(cal.HOUR_OF_DAY)*3600000;
            }
            int currentColIndex = (int)(e.getX()+lastCellIndex*(long)label.getWidth())/label.getWidth();
            long width2 = 86400000L;
            long m2 = 0;
            GanttWorkingHours whVO2 = getWhVO(lastRow,currentColIndex);
            if (whVO2!=null) {
              width2 = (whVO2.getMorningEndHour()!=null?(whVO2.getMorningEndHour().getTime()-whVO2.getMorningStartHour().getTime()):0)+
                      (whVO2.getAfternoonEndHour()!=null?(whVO2.getAfternoonEndHour().getTime()-whVO2.getAfternoonStartHour().getTime()):0);

              cal.setTime(whVO2.getMorningStartHour());
              m2 = cal.get(cal.HOUR_OF_DAY)*3600000;
            }
            long x = 0;
            if (e.getX()>0)
              x = e.getX()%label.getWidth();
            else
              x = label.getWidth()-((-e.getX())%label.getWidth());

            if (value!=null && lastX!=e.getX()) {
              if (lastX<e.getX()) {
                // enlarge appointment width: find out if there exists an appointment at lastX coordinate...
                Appointment a,olda;
                boolean exists = false;
                long t1 = startDate.getTime()+86400000L*lastCellIndex+m1+width1*lastX/(long)label.getWidth();
                long t2 = startDate.getTime()+86400000L*currentColIndex+m2+width2*x/(long)label.getWidth();
                for(int i=0;i<value.size();i++) {
                  a = (Appointment)value.get(i);
                  olda = (Appointment)a.clone();
                  if (a.getStartDate().getTime()<=t1 && t1<=a.getEndDate().getTime()) {
                    if (enableEdit && a.isEnableEdit()) {
                      a.setEndDate(new java.sql.Timestamp(t2));
                      a.setDuration(new BigDecimal(getTime(a, lastRow, lastCellIndex)));
                      fireAppointmentChangedEvent(table2.getSelectedRow(),olda,a);
                    }
                    exists = true;
                    break;
                  }
                }

                if (!exists && enableInsert) {
                  // no appointment found: it will be created another one...
                  try {
                    GanttRowVO ganttRow = (GanttRowVO) ganttRows.get(lastRow);
                    a = (Appointment) ganttRow.getAppointmentClass().newInstance();
                    a.setDescription("");
                    a.setStartDate(new java.sql.Timestamp(t1));
                    a.setEndDate(new java.sql.Timestamp(t2));
                    a.setDuration(new BigDecimal(getTime(a,lastRow,lastCellIndex)));
                    fireNewAppointmentEvent(table2.getSelectedRow(),a);
                    value.add(a);
                  }
                  catch (Throwable ex) {
                    ex.printStackTrace();
                  }

                }
              }
              else {
                // reduce appointment width or delete appointment...
                Appointment a,olda;
//                long t2 = startDate.getTime()+86400000L*x/(long)label.getWidth();
                long t2 = startDate.getTime()+86400000L*currentColIndex+m2+width2*x/(long)label.getWidth();
                for(int i=0;i<value.size();i++) {
                  a = (Appointment)value.get(i);
                  olda = (Appointment)a.clone();
                  if (a.getStartDate().getTime()<=t2 && t2<=a.getEndDate().getTime()) {
                    if (enableEdit && a.isEnableEdit()) {
                      a.setEndDate(new java.sql.Timestamp(t2));
                      if (a.getStartDate().getTime()>=a.getEndDate().getTime() && enableDelete && a.isEnableDelete()) {
                        GanttRowVO ganttRow = (GanttRowVO) ganttRows.get(lastRow);
                        ganttRow.getAppointments().remove(a);
                        fireAppointmentDeletedEvent(table2.getSelectedRow(),a);
                      }
                      else
                        fireAppointmentChangedEvent(table2.getSelectedRow(),olda,a);

                    }
                    break;
                  }
                }
              }

              if (table!=null)
                table.editingStopped(null);
              table2.repaint();

            }
          }
          isLeftMouseButtonPressed = false;
        }
      });
    }


    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int row, int column) {
      this.table = table;


      if (row!=lastRow) {
        lastDescr = null;
        lastRow = row;
      }

      long width = 86400000L;
      cal.setTime(new java.util.Date(startDate.getTime()+(long)column*86400000L));
      GanttWorkingHours whVO = getWhVO(row,column);
      if (whVO!=null)
        width = (whVO.getMorningEndHour()!=null?(whVO.getMorningEndHour().getTime()-whVO.getMorningStartHour().getTime()):0)+
                (whVO.getAfternoonEndHour()!=null?(whVO.getAfternoonEndHour().getTime()-whVO.getAfternoonStartHour().getTime()):0);

      this.value = (ArrayList)value;
      this.isSelected = isSelected;
      lastCellIndex = table.convertColumnIndexToModel(column);

      long m1 = startDate.getTime()+((long)column)*86400000L;
      long m2 = startDate.getTime()+((long)column)*86400000L;
      long a1 = startDate.getTime()+((long)column)*86400000L+86400000L;
      long a2 = startDate.getTime()+((long)column)*86400000L+86400000L;
      if (whVO!=null) {
        if (whVO.getMorningStartHour()!=null) {
          cal.setTime(whVO.getMorningStartHour());
          m1 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:0L);
        }
        if (whVO.getMorningEndHour()!=null) {
          cal.setTime(whVO.getMorningEndHour());
          m2 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:0L);
        }
        if (whVO.getAfternoonStartHour()!=null) {
          cal.setTime(whVO.getAfternoonStartHour());
          a1 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:86400000L);
        }
        if (whVO.getAfternoonEndHour()!=null) {
          cal.setTime(whVO.getAfternoonEndHour());
          a2 = startDate.getTime()+((long)column)*86400000L+(whVO!=null?cal.get(cal.HOUR_OF_DAY)*3600000L+cal.get(cal.MINUTE)*60000:86400000L);
        }
      }

      label.setAppointments(
        (ArrayList)value,
        startDate.getTime()+((long)column)*86400000L,
        startDate.getTime()+(((long)column)+1L)*86400000L,
        width,
        m1,
        m2,
        a1,
        a2,
        row,
        this,
        column
      );

     label.repaint();

     return label;
   }


   public Object getCellEditorValue() {
     return value;
   }



  } // end GanttColumnEditor




}


