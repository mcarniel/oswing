package org.openswing.swing.client;

import java.beans.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;

import org.openswing.swing.domains.java.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: List input control with check-box: its items are retrieved through ClientSettings.getDomain method,
 * that uses domainId property to identify the correct domain to use.
 * If this control is linked to a Form, then ListSelectionModel.SINGLE_SELECTION_MODE is allowed:
 * other selection modes are not permitted.
 * If this control is not linked to a Form, then it is possible to select more than one item.
 * </p>
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
public class CheckBoxListControl extends BaseInputControl implements InputControl,SearchControl,MouseListener {

  private ArrayList mouseListeners = new ArrayList();

  /** flag used in addNotify method */
  private boolean firstTime = false;

  /** list */
  private JList list = new JList() {

    public void addMouseListener(MouseListener listener) {
      try {
        if (listener.equals(CheckBoxListControl.this)) {
          super.addMouseListener(listener);
        }
        else {
          mouseListeners.add(listener);
        }
      }
      catch (Exception ex) {
      }
    }

  };

  /** domain related to this input control */
  private Domain domain = null;

  /** domain identifier */
  private String domainId = null;

  /** list model */
  private DefaultListModel model = new DefaultListModel();

  /** define if in insert mode the list has no item selected; default value: <code>false</code> i.e. the first item is pre-selected */
  private boolean nullAsDefaultValue = false;

  /** define if description in list items must be translated; default value: <code>true</code> */
  private boolean translateItemDescriptions = true;


  /**
   * Contructor.
   */
  public CheckBoxListControl() {
    this.setLayout(new GridBagLayout());
    this.add(new JScrollPane(list), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    setOpaque(false);
    list.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_CANCEL || e.getKeyCode()==e.VK_BACK_SPACE || e.getKeyCode()==e.VK_DELETE)
          list.setSelectedIndex(-1);
      }
    });

    list.setSelectionForeground((Color)UIManager.get("TextField.foreground"));

    initListeners();
    getBindingComponent().addFocusListener(new FocusAdapter() {

      public void focusGained(FocusEvent e) {
        if (ClientSettings.VIEW_BACKGROUND_SEL_COLOR && isEnabled()) {
          getBindingComponent().setBackground(defaultBackgroundColor);
        }
      }

    });

    new SearchWindowManager(this);
  }


  public final void addNotify() {
    super.addNotify();
    if (!firstTime) {
      firstTime = true;
      list.setCellRenderer(new CheckBoxListControlCellRenderer());
    }
  }


  /**
   * Set the domain identifier, so the control can retrieve its items from the domain.
   * @param domainId domain identifier
   */
  public final void setDomainId(String domainId) {
    this.domainId = domainId;
    if (Beans.isDesignTime())
      return;
    Domain domain = ClientSettings.getInstance().getDomain(domainId);
    setDomain(domain);
  }


  /**
   * Set the specified domain and retrieve the list items from the domain.
   * @param domain domain to use to retrieve the list items
   */
  public final void setDomain(Domain domain) {
    this.domain = domain;
    model.removeAllElements();
    if (domain!=null) {
      DomainPair[] pairs = domain.getDomainPairList();
      for(int i=0;i<pairs.length;i++)
        if (translateItemDescriptions)
          model.addElement(ClientSettings.getInstance().getResources().getResource(pairs[i].getDescription()));
        else
          model.addElement(pairs[i].getDescription());
      list.setModel(model);
      list.revalidate();
      list.setSelectedIndex(-1);
    }
  }


  /**
   * @return domain identifier
   */
  public final String getDomainId() {
    return domainId;
  }


  /**
   * Select an item from the list starting from the specified code (with ListSelectionModel.SINGLE_SELECTION) or
   * select a list of items starting from the specified java.util.List of codes (without ListSelectionModel.SINGLE_SELECTION) or
   * @param code used to retrieve the corresponding item and to select that item in the list (with ListSelectionModel.SINGLE_SELECTION) or java.util.List of codes (without ListSelectionModel.SINGLE_SELECTION)
   */
  public final void setValue(Object code) {
    if (list.getSelectionMode()==ListSelectionModel.SINGLE_SELECTION) {
      if (code==null)
        list.setSelectedIndex(-1);
      if (domain==null)
        return;
      DomainPair pair = domain.getDomainPair(code);
      if (pair!=null) {

        if (translateItemDescriptions)
          list.setSelectedValue( ClientSettings.getInstance().getResources().getResource(pair.getDescription()),true );
        else
          list.setSelectedValue( pair.getDescription(),true );
      }
    }
    else {
      if (code==null || code instanceof java.util.List && ((java.util.List)code).size()==0) {
        list.getSelectionModel().clearSelection();
        return;
      }
      if (domain==null)
        return;

      if (code instanceof java.util.List) {
        java.util.List codes = (java.util.List)code;
        list.getSelectionModel().clearSelection();
        for(int i=0;i<codes.size();i++)
          for(int j=0;j<domain.getDomainPairList().length;j++) {
            if (domain.getDomainPairList()[j].getCode().equals(codes.get(i)))
              list.getSelectionModel().addSelectionInterval(j,j);
        }
      }
      else
        Logger.error(this.getClass().getName(), "setValue", "You must specify a java.util.List argument type", null);
    }
  }


  /**
   * Select the list item related to the specified index.
   * @param index index to retrieve the corresponding item and to select that item in the list
   */
  public final void setSelectedIndex(int index) {
    list.setSelectedIndex(index);
    try {
      list.scrollRectToVisible(list.getCellBounds(index, index));
    }
    catch (Exception ex) {
    }
  }


  /**
   * Select the list item related to the specified index.
   * @param indices interval of indices to retrieve the corresponding item and to select that item in the list
   */
  public final void setSelectedIndices(int[] indices) {
    list.setSelectedIndices(indices);
    try {
      list.scrollRectToVisible(list.getCellBounds(indices[0], indices[0]));
    }
    catch (Exception ex) {
    }
  }


  /**
   * Returns the fixed cell height value -- the value specified by setting
   * the <code>fixedCellHeight</code> property,
   * rather than that calculated from the list elements.
   *
   * @return the fixed cell height, in pixels
   * @see #setFixedCellHeight
   */
  public final int getFixedCellHeight() {
    return list.getFixedCellHeight();
  }


  /**
   * Sets the height of every cell in the list.  If <code>height</code>
   * is -1, cell
   * heights are computed by applying <code>getPreferredSize</code>
   * to the <code>cellRenderer</code> component for each list element.
   * <p>
   * The default value of this property is -1.
   * <p>
   * This is a JavaBeans bound property.
   *
   * @param height an integer giving the height, in pixels, for all cells
   *        in this list
   * @see #getPrototypeCellValue
   * @see #setFixedCellWidth
   * @see JComponent#addPropertyChangeListener
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: Defines a fixed cell height when greater than zero.
   */
  public final void setFixedCellHeight(int height) {
    list.setFixedCellHeight(height);
  }



  /**
   * Returns the fixed cell width value -- the value specified by setting
   * the <code>fixedCellWidth</code> property, rather than that calculated
   * from the list elements.
   *
   * @return the fixed cell width
   * @see #setFixedCellWidth
   */
  public final int getFixedCellWidth() {
    return list.getFixedCellWidth();
  }


  /**
   * Sets the width of every cell in the list.  If <code>width</code> is -1,
   * cell widths are computed by applying <code>getPreferredSize</code>
   * to the <code>cellRenderer</code> component for each list element.
   * <p>
   * The default value of this property is -1.
   * <p>
   * This is a JavaBeans bound property.
   *
   * @param width   the width, in pixels, for all cells in this list
   * @see #getPrototypeCellValue
   * @see #setFixedCellWidth
   * @see JComponent#addPropertyChangeListener
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: Defines a fixed cell width when greater than zero.
   */
  public final void setFixedCellWidth(int width) {
    list.setFixedCellWidth(width);
  }


  /**
   * Returns the preferred number of visible rows.
   *
   * @return an integer indicating the preferred number of rows to display
   *         without using a scroll bar
   * @see #setVisibleRowCount
   */
  public final int getVisibleRowCount() {
    return list.getVisibleRowCount();
  }


  /**
   * Sets the preferred number of rows in the list that can be displayed
   * without a scrollbar, as determined by the nearest
   * <code>JViewport</code> ancestor, if any.
   * The value of this property only affects the value of
   * the <code>JList</code>'s <code>preferredScrollableViewportSize</code>.
   * <p>
   * The default value of this property is 8.
   * <p>
   * This is a JavaBeans bound property.
   *
   * @param visibleRowCount  an integer specifying the preferred number of
   *                         visible rows
   * @see #getVisibleRowCount
   * @see JComponent#getVisibleRect
   * @see JViewport
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: The preferred number of cells that can be displayed without a scroll bar.
   */
  public final void setVisibleRowCount(int visibleRowCount) {
    list.setVisibleRowCount(visibleRowCount);
  }


  /**
   * Returns <code>JList.VERTICAL</code> if the layout is a single
   * column of cells, or <code>JList.VERTICAL_WRAP</code> if the layout
   * is "newspaper style" with the content flowing vertically then
   * horizontally or <code>JList.HORIZONTAL_WRAP</code> if the layout is
   * "newspaper style" with the content flowing horizontally then
   * vertically.
   *
   * @return the value of the layoutOrientation property
   * @see #setLayoutOrientation
   * @since 1.4
   */
  public final int getLayoutOrientation() {
      return list.getLayoutOrientation();
  }


  /**
   * Defines the way list cells are layed out. Consider a <code>JList</code>
   * with four cells, this can be layed out in one of the following ways:
   * <pre>
   *   0
   *   1
   *   2
   *   3
   * </pre>
   * <pre>
   *   0  1
   *   2  3
   * </pre>
   * <pre>
   *   0  2
   *   1  3
   * </pre>
   * <p>
   * These correspond to the following values:
   *
   * <table border="1"
   *  summary="Describes layouts VERTICAL, HORIZONTAL_WRAP, and VERTICAL_WRAP">
   *   <tr><th><p align="left">Value</p></th><th><p align="left">Description</p></th></tr>
   *   <tr><td><code>JList.VERTICAL</code>
   *       <td>The cells should be layed out vertically in one column.
   *   <tr><td><code>JList.HORIZONTAL_WRAP</code>
   *       <td>The cells should be layed out horizontally, wrapping to
   *           a new row as necessary.  The number
   *           of rows to use will either be defined by
   *           <code>getVisibleRowCount</code> if > 0, otherwise the
   *           number of rows will be determined by the width of the
   *           <code>JList</code>.
   *   <tr><td><code>JList.VERTICAL_WRAP</code>
   *       <td>The cells should be layed out vertically, wrapping to a
   *           new column as necessary.  The number
   *           of rows to use will either be defined by
   *           <code>getVisibleRowCount</code> if > 0, otherwise the
   *           number of rows will be determined by the height of the
   *           <code>JList</code>.
   *  </table>
   * The default value of this property is <code>JList.VERTICAL</code>.
   * <p>
   * This will throw an <code>IllegalArgumentException</code> if
   * <code>layoutOrientation</code> is not one of
   * <code>JList.HORIZONTAL_WRAP</code> or <code>JList.VERTICAL</code> or
   * <code>JList.VERTICAL_WRAP</code>
   *
   * @param layoutOrientation New orientation, one of
   *        <code>JList.HORIZONTAL_WRAP</code>,  <code>JList.VERTICAL</code>
   *        or <code>JList.VERTICAL_WRAP</code>.
   * @see #getLayoutOrientation
   * @see #setVisibleRowCount
   * @see #getScrollableTracksViewportHeight
   * @since 1.4
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: Defines the way list cells are layed out.
   *        enum: VERTICAL JList.VERTICAL
   *              HORIZONTAL_WRAP JList.HORIZONTAL_WRAP
   *              VERTICAL_WRAP JList.VERTICAL_WRAP
   */
  public final void setLayoutOrientation(int layoutOrientation) {
    list.setLayoutOrientation(layoutOrientation);
  }


  /**
   * Determines whether single-item or multiple-item
   * selections are allowed.
   * The following <code>selectionMode</code> values are allowed:
   * <ul>
   * <li> <code>ListSelectionModel.SINGLE_SELECTION</code>
   *   Only one list index can be selected at a time.  In this
   *   mode the <code>setSelectionInterval</code> and
   *   <code>addSelectionInterval</code>
   *   methods are equivalent, and only the second index
   *   argument is used.
   * <li> <code>ListSelectionModel.SINGLE_INTERVAL_SELECTION</code>
   *   One contiguous index interval can be selected at a time.
   *   In this mode <code>setSelectionInterval</code> and
   *   <code>addSelectionInterval</code>
   *   are equivalent.
   * <li> <code>ListSelectionModel.MULTIPLE_INTERVAL_SELECTION</code>
   *   In this mode, there's no restriction on what can be selected.
   *   This is the default.
   * </ul>
   *
   * @param selectionMode an integer specifying the type of selections
   *                         that are permissible
   * @see #getSelectionMode
   * @beaninfo
   * description: The selection mode.
   *        enum: SINGLE_SELECTION            ListSelectionModel.SINGLE_SELECTION
   *              SINGLE_INTERVAL_SELECTION   ListSelectionModel.SINGLE_INTERVAL_SELECTION
   *              MULTIPLE_INTERVAL_SELECTION ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
   */
  public final void setSelectionMode(int selectionMode) {
    list.setSelectionMode(selectionMode);
  }


  /**
   * Returns whether single-item or multiple-item selections are allowed.
   *
   * @return the value of the <code>selectionMode</code> property
   * @see #setSelectionMode
   */
  public final int getSelectionMode() {
    return list.getSelectionMode();
  }


  /**
   * Sets the data model's <code>isAdjusting</code> property to true,
   * so that a single event will be generated when all of the selection
   * events have finished (for example, when the mouse is being
   * dragged over the list in selection mode).
   *
   * @param b the boolean value for the property value
   * @see ListSelectionModel#setValueIsAdjusting
   */
  public final void setValueIsAdjusting(boolean b) {
    list.setValueIsAdjusting(b);
  }


  /**
   * Returns the value of the data model's <code>isAdjusting</code> property.
   * This value is true if multiple changes are being made.
   *
   * @return true if multiple selection-changes are occurring, as
   *         when the mouse is being dragged over the list
   * @see ListSelectionModel#getValueIsAdjusting
   */
  public final boolean getValueIsAdjusting() {
    return list.getValueIsAdjusting();
  }


  /**
   * Returns the selection foreground color.
   *
   * @return the <code>Color</code> object for the foreground property
   * @see #setSelectionForeground
   * @see #setSelectionBackground
   */
  public Color getSelectionForeground() {
      return list.getSelectionForeground();
  }


  /**
   * Sets the foreground color for selected cells.  Cell renderers
   * can use this color to render text and graphics for selected
   * cells.
   * <p>
   * The default value of this property is defined by the look
   * and feel implementation.
   * <p>
   * This is a JavaBeans bound property.
   *
   * @param selectionForeground  the <code>Color</code> to use in the foreground
   *                             for selected list items
   * @see #getSelectionForeground
   * @see #setSelectionBackground
   * @see #setForeground
   * @see #setBackground
   * @see #setFont
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: The foreground color of selected cells.
   */
  public final void setSelectionForeground(Color selectionForeground) {
    list.setSelectionForeground(selectionForeground);
  }


  /**
   * Returns the background color for selected cells.
   *
   * @return the <code>Color</code> used for the background of
   * selected list items
   * @see #setSelectionBackground
   * @see #setSelectionForeground
   */
  public final Color getSelectionBackground() {
    return list.getSelectionBackground();
  }


  /**
   * Sets the background color for selected cells.  Cell renderers
   * can use this color to the fill selected cells.
   * <p>
   * The default value of this property is defined by the look
   * and feel implementation.
   * <p>
   * This is a JavaBeans bound property.
   *
   * @param selectionBackground  the <code>Color</code> to use for the
   *                             background of selected cells
   * @see #getSelectionBackground
   * @see #setSelectionForeground
   * @see #setForeground
   * @see #setBackground
   * @see #setFont
   * @beaninfo
   *       bound: true
   *   attribute: visualUpdate true
   * description: The background color of selected cells.
   */
  public void setSelectionBackground(Color selectionBackground) {
    list.setSelectionBackground(selectionBackground);
  }


  /**
   * Adds a listener to the list that's notified each time a change
   * to the selection occurs.  Listeners added directly to the
   * <code>JList</code>
   * will have their <code>ListSelectionEvent.getSource() ==
   * this JList</code>
   * (instead of the <code>ListSelectionModel</code>).
   *
   * @param listener the <code>ListSelectionListener</code> to add
   * @see #getSelectionModel
   * @see #getListSelectionListeners
   */
  public final void addListSelectionListener(ListSelectionListener listener) {
    list.addListSelectionListener(listener);
  }


  /**
   * Removes a listener from the list that's notified each time a
   * change to the selection occurs.
   *
   * @param listener the <code>ListSelectionListener</code> to remove
   * @see #addListSelectionListener
   * @see #getSelectionModel
   */
  public final void removeListSelectionListener(ListSelectionListener listener) {
    list.removeListSelectionListener(listener);
  }


  /**
   * @return code or java.util.List of codes related to the selected item/items; it return null if no item is selected (with ListSelectionModel.SINGLE_SELECTION) or an empty java.util.List (without ListSelectionModel.SINGLE_SELECTION)
   */
  public final Object getValue() {
    if (list.getSelectionMode()==ListSelectionModel.SINGLE_SELECTION) {
      if (domain==null)
        return null;
      DomainPair[] pairs = domain.getDomainPairList();
      if (list.getSelectedIndex()==-1)
        return null;
      return pairs[list.getSelectedIndex()].getCode();
    }
    else {
      if (domain==null)
        return new ArrayList();
      DomainPair[] pairs = domain.getDomainPairList();
      ArrayList codes = new ArrayList();
      int[] values = list.getSelectedIndices();
      for(int j=0;j<values.length;j++)
        codes.add(pairs[values[j]].getCode());
      return codes;
    }
  }


  public void setEnabled(boolean enabled) {
    list.setEnabled(enabled);
    list.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
    if (enabled) {
      list.setBackground( (Color) UIManager.get("TextField.background"));
      list.setSelectionBackground(ClientSettings.BACKGROUND_SEL_COLOR);
    }
    else {
      list.setBackground( (Color) UIManager.get("TextField.inactiveBackground"));
      try {
        list.setSelectionBackground(new Color(
            list.getBackground().getRed() - 10,
            list.getBackground().getGreen() - 10,
            list.getBackground().getBlue() - 10
            ));
      }
      catch (Exception ex) {
        list.setSelectionBackground(Color.lightGray);
      }
    }
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      return list.isEnabled();
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * @return component inside this whose contains the value
   */
  public JComponent getBindingComponent() {
//    if (list!=null && list.getSelectionMode()!=ListSelectionModel.SINGLE_SELECTION)
//      list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    return list;
  }


  /**
   * Adds the specified focus listener to receive focus events from
   * this component when this component gains input focus.
   * If listener <code>l</code> is <code>null</code>,
   * no exception is thrown and no action is performed.
   *
   * @param    l   the focus listener
   * @see      java.awt.event.FocusEvent
   * @see      java.awt.event.FocusListener
   * @see      #removeFocusListener
   * @see      #getFocusListeners
   * @since    JDK1.1
   */
  public final void addFocusListener(FocusListener l) {
    try {
      list.addFocusListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Removes the specified focus listener so that it no longer
   * receives focus events from this component. This method performs
   * no function, nor does it throw an exception, if the listener
   * specified by the argument was not previously added to this component.
   * If listener <code>l</code> is <code>null</code>,
   * no exception is thrown and no action is performed.
   *
   * @param    l   the focus listener
   * @see      java.awt.event.FocusEvent
   * @see      java.awt.event.FocusListener
   * @see      #addFocusListener
   * @see      #getFocusListeners
   * @since    JDK1.1
   */
  public final void removeFocusListener(FocusListener l) {
    try {
      list.removeFocusListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Adds the specified mouse listener to receive
   * mouse events from this textfield.
   *
   * @param l the mouse listener to be added
   */
  public final void addMouseListener(MouseListener l) {
    try {
      list.addMouseListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Removes the specified mouse listener so that it no longer
   * receives mouse events from this textfield.
   *
   * @param l the mouse listener to be removed
   */
  public final void removeMouseListener(MouseListener l) {
    try {
      list.removeMouseListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return define if in insert mode list has no item selected
   */
  public final boolean isNullAsDefaultValue() {
    return nullAsDefaultValue;
  }


  /**
   * Define if in insert mode list has no item selected.
   * @param nullAsDefaultValue define if in insert mode list has no item selected
   */
  public final void setNullAsDefaultValue(boolean nullAsDefaultValue) {
    this.nullAsDefaultValue = nullAsDefaultValue;
  }


  /**
   * @return domain to use to retrieve the list items
   */
  public final Domain getDomain() {
    return domain;
  }


  /**
   * @return the selected index in the input control
   */
  public final int getSelectedIndex() {
    return list.getSelectedIndex();
  }


  /**
   * @return total rows count in the input control
   */
  public final int getRowCount() {
    return list.getModel().getSize();
  }


  /**
   * @return the element at the specified index, converted in String format
   */
  public final String getValueAt(int index) {
    return list.getModel().getElementAt(index)==null?"":list.getModel().getElementAt(index).toString();
  }


  /**
   * @return combo control
   */
  public final JComponent getComponent() {
    return list;
  }


  /**
   * @return <code>true</code> if the input control is in read only mode (so search is enabled), <code>false</code> otherwise
   */
  public final boolean isReadOnly() {
    return !isEnabled();
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to render an item list.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class CheckBoxListControlCellRenderer extends DefaultListCellRenderer {

    /** check-box showed when CheckBoxListControl.isShowCheckBoxes is true */
    private CheckBoxLabel checkBox = new CheckBoxLabel();

    /** panel that contains check-box, image and description */
    private JPanel panel = new JPanel();


    public CheckBoxListControlCellRenderer() {
      checkBox.setOpaque(false);
      panel.setOpaque(false);
      panel.setLayout(new BorderLayout(0, 0));
      panel.add(this, BorderLayout.CENTER);
      checkBox.setSize(14,14);
      checkBox.setPreferredSize(new Dimension(14,14));
      panel.add(checkBox, BorderLayout.BEFORE_LINE_BEGINS);
      if (list.getSelectionMode()==ListSelectionModel.SINGLE_INTERVAL_SELECTION)
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

      list.addMouseListener(CheckBoxListControl.this);
    }


    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {
      Component c = super.getListCellRendererComponent(list,value,index,false,cellHasFocus);

      checkBox.setEnabled(CheckBoxListControl.this.isEnabled());
      checkBox.setSelected(isSelected);
      return panel;
    }


  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to render the check-box.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * <p> </p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class CheckBoxLabel extends JLabel {

    private boolean sel;

    public void setSelected(boolean sel) {
      this.sel = sel;
      repaint();
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.translate((int)this.getWidth()/2-6,this.getHeight()/2-5);
      BasicGraphicsUtils.drawLoweredBezel(g,0,0,12,12,Color.darkGray,Color.black,Color.white,Color.gray);
      if (sel) {
        if (!isEnabled())
          g.setColor(Color.GRAY);
        else
          g.setColor(Color.black);
        g.drawLine(3,5,5,7);
        g.drawLine(3,6,5,8);
        g.drawLine(3,7,5,9);
        g.drawLine(6,6,9,3);
        g.drawLine(6,7,9,4);
        g.drawLine(6,8,9,5);
      }
    }

  } // end inner class...


  public void mouseEntered(MouseEvent e) {
  }


  public void mouseExited(MouseEvent e) {
  }


  public void mouseReleased(MouseEvent e) {
  }


  public void mousePressed(MouseEvent e) {
  }


  public void mouseClicked(MouseEvent e) {
    if (isEnabled()) {
      try {
        int row = list.locationToIndex(e.getPoint());
        if (row!=-1) {
          if (list.getSelectionMode()==ListSelectionModel.SINGLE_SELECTION) {
            list.setSelectedIndex(row);
          }
          else {
            int[] indexes = list.getSelectedIndices();
            for(int i=0;i<indexes.length;i++)
              if (indexes[i]==row) {
                list.getSelectionModel().removeSelectionInterval(row,row);
                return;
              }
            list.getSelectionModel().addSelectionInterval(row,row);
          }
        }
      }
      catch (Exception ex) {
      }
    }
  }


  /**
   * @return <code>true</code> to disable key listening on input control (for instance, in case of nested grids), <code>false</code> to listen for key events
   */
  public final boolean disableListener() {
    return false;
  }


  /**
   * @return define if description in list items must be translated
   */
  public final boolean isTranslateItemDescriptions() {
    return translateItemDescriptions;
  }


  /**
   * Define if description in list items must be translated; default value: <code>true</code>.
   * @param translateItemDescriptions flag used to define if description in list items must be translated
   */
  public final void setTranslateItemDescriptions(boolean translateItemDescriptions) {
    this.translateItemDescriptions = translateItemDescriptions;
  }


  /**
   * Method invoked by SearchWindowManager when the specified "textToSeach" pattern has not matchings in the current content
   * of binded control.
   * This callback can be used to retrieve additional data into control and to search inside new data.
   * @param textToSeach patten of text to search
   * @return -1 if no additional data is available, otherwise the row index of data just added that satify the pattern
   */
  public final int search(String textToSeach) {
    return -1;
  }


}
