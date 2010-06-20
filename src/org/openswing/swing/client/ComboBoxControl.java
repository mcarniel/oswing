package org.openswing.swing.client;

import java.beans.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.domains.java.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Combo box input control: its items are retrieved from ClientSettings.getDomain method,
 * that uses domainId property to identify the correct domain to use.</p>
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
public class ComboBoxControl extends BaseInputControl implements InputControl,SearchControl {

  static {
    UIManager.put("ComboBox.disabledForeground", UIManager.get("ComboBox.foreground"));
    UIManager.put("ComboBox.disabledBackground", UIManager.get("TextField.inactiveBackground"));
    UIManager.put("ComboBox.selectionForeground", Color.black );
    UIManager.put("ComboBox.selectionBackground", ClientSettings.BACKGROUND_SEL_COLOR );
  }

  /** combo box */
  private JComboBox combo = new JComboBox();

  /** domain related to this input control */
  private Domain domain = null;

  /** domain identifier */
  private String domainId = null;

  /** combo box model */
  private DefaultComboBoxModel model = new DefaultComboBoxModel();

  /** background color when the input control is enabled */
  private Color enabledBackColor = combo.getBackground();

  /** define if in insert mode combo box has no item selected; default value: <code>false</code> i.e. the first item is pre-selected */
  private boolean nullAsDefaultValue = false;

  /** define if description in combo items must be translated; default value: <code>true</code> */
  private boolean translateItemDescriptions = true;


  /**
   * Contructor.
   */
  public ComboBoxControl() {
    this.setLayout(new GridBagLayout());
    this.add(combo, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

//    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//    add(combo);
    setOpaque(false);
    combo.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_CANCEL || e.getKeyCode()==e.VK_BACK_SPACE || e.getKeyCode()==e.VK_DELETE)
          combo.setSelectedIndex(-1);
      }
    });

    // intercepts key events listened by combo box...1
    combo.setKeySelectionManager(new JComboBox.KeySelectionManager() {
        public int selectionForKey(char aKey, ComboBoxModel aModel) {
            return -1;
        }
    });

    new SearchWindowManager(this);

    if (ClientSettings.TEXT_ORIENTATION!=null)
        setComponentOrientation(ClientSettings.TEXT_ORIENTATION);

    initListeners();
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
   * Set the specified domain and retrieve the combo items from the domain.
   * @param domain domain to use to retrieve the combo items
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
      combo.setModel(model);
      combo.revalidate();
      combo.setSelectedIndex(-1);
    }
  }


  /**
   * @return domain identifier
   */
  public final String getDomainId() {
    return domainId;
  }


  /**
   * Select the combo item related to the specified code.
   * @param code used to retrieve the corresponding item and to select that item in the combo
   */
  public final void setValue(Object code) {
    if (code==null)
      combo.setSelectedIndex(-1);
    if (domain==null)
      return;
    DomainPair pair = domain.getDomainPair(code);
    if (pair!=null) {
      if (translateItemDescriptions)
        combo.setSelectedItem( ClientSettings.getInstance().getResources().getResource(pair.getDescription()) );
      else
        combo.setSelectedItem( pair.getDescription() );
    }
  }


  /**
   * @return code related to the selected combo item; it return null if no item is selected
   */
  public final Object getValue() {
    if (domain==null)
      return null;
    DomainPair[] pairs = domain.getDomainPairList();
    for(int i=0;i<pairs.length;i++)
      if (combo.getSelectedItem()==null)
        return null;
      else {
        if (translateItemDescriptions) {
          if (combo.getSelectedItem().equals( ClientSettings.getInstance().getResources().getResource(pairs[i].getDescription()) ))
            return pairs[i].getCode();
        }
        else {
          if (combo.getSelectedItem().equals( pairs[i].getDescription()) )
            return pairs[i].getCode();
        }
      }
    return null;
  }


  /**
   * @return combo box
   */
  public final JComboBox getComboBox() {
    return combo;
  }


  public void setEnabled(boolean enabled) {
    combo.setEnabled(enabled);
    combo.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
    if (!enabled)
      combo.setBackground((Color)UIManager.get("TextField.inactiveBackground"));
    else
      combo.setBackground(enabledBackColor);
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      return combo.isEnabled();
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * @return component inside this whose contains the value
   */
  public JComponent getBindingComponent() {
    return combo;
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
        if(combo != null) combo.addFocusListener(l);
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
      combo.removeFocusListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Adds the specified action listener to receive
   * action events from this textfield.
   *
   * @param l the action listener to be added
   */
  public final void addActionListener(ActionListener l) {
    try {
      combo.addActionListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Removes the specified action listener so that it no longer
   * receives action events from this textfield.
   *
   * @param l the action listener to be removed
   */
  public final void removeActionListener(ActionListener l) {
    try {
      combo.removeActionListener(l);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Adds an <code>ItemListener</code>.
   * <p>
   * <code>aListener</code> will receive one or two <code>ItemEvent</code>s when
   * the selected item changes.
   *
   * @param aListener the <code>ItemListener</code> that is to be notified
   * @see #setSelectedItem
   */
  public final void addItemListener(ItemListener alistener) {
    try {
      combo.addItemListener(alistener);
    }
    catch (Exception ex) {
    }
  }


  /** Removes an <code>ItemListener</code>.
   *
   * @param aListener  the <code>ItemListener</code> to remove
   */
  public final void removeItemListener(ItemListener alistener) {
    try {
      combo.removeItemListener(alistener);
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return define if in insert mode combo box has no item selected
   */
  public final boolean isNullAsDefaultValue() {
    return nullAsDefaultValue;
  }


  /**
   * Define if in insert mode combo box has no item selected.
   * @param nullAsDefaultValue define if in insert mode combo box has no item selected
   */
  public final void setNullAsDefaultValue(boolean nullAsDefaultValue) {
    this.nullAsDefaultValue = nullAsDefaultValue;
  }


  /**
   * @return domain to use to retrieve the combo items
   */
  public final Domain getDomain() {
    return domain;
  }


  /**
   * @return the selected index in the input control
   */
  public final int getSelectedIndex() {
    return combo.getSelectedIndex();
  }


  /**
   * Set the selected index.
   */
  public final void setSelectedIndex(int index) {
    combo.setSelectedIndex(index);
  }


  /**
   * @return total rows count in the input control
   */
  public final int getRowCount() {
    return combo.getItemCount();
  }


  /**
   * @return the element at the specified index, converted in String format
   */
  public final String getValueAt(int index) {
    return combo.getItemAt(index)==null?"":combo.getItemAt(index).toString();
  }


  /**
   * @return combo control
   */
  public final JComponent getComponent() {
    return combo;
  }


  /**
   * @return <code>true</code> if the input control is in read only mode (so search is enabled), <code>false</code> otherwise
   */
  public final boolean isReadOnly() {
    return isEnabled();
  }


  /**
   * @return <code>true</code> to disable key listening on input control (for instance, in case of nested grids), <code>false</code> to listen for key events
   */
  public final boolean disableListener() {
    return false;
  }


  /**
   * @return define if description in combo items must be translated
   */
  public final boolean isTranslateItemDescriptions() {
    return translateItemDescriptions;
  }


  /**
   * Define if description in combo items must be translated; default value: <code>true</code>.
   * @param translateItemDescriptions flag used to define if description in combo items must be translated
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


  /**
   * Set the component orientation: from left to right or from right to left.
   * @param o component orientation
   */
  public final void setTextOrientation(ComponentOrientation o) {
    combo.setComponentOrientation(o);
  }


  /**
   * @return component orientation
   */
  public final ComponentOrientation getTextOrientation() {
    try {
      return combo.getComponentOrientation();
    }
    catch (Exception ex) {
      return null;
    }
  }

}
