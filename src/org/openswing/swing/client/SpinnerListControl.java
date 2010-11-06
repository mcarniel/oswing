package org.openswing.swing.client;

import java.math.*;
import java.text.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.logger.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.domains.java.Domain;
import java.beans.Beans;
import java.util.ArrayList;
import org.openswing.swing.domains.java.DomainPair;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Spinner list input control.</p>
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
public class SpinnerListControl extends BaseInputControl implements InputControl,ChangeListener {

  SpinnerListModel model = new SpinnerListModel();
  JSpinner spinner = new JSpinner(model);
  JFormattedTextField ftf = getTextField(spinner);

  /** domain related to this input control */
  private Domain domain = null;

  /** domain identifier */
  private String domainId = null;

  /** number of visible characters; default value: 8 */
  private int columns = 8;

  /** horizontal alignment */
  private int horizontalAlignment = JTextField.RIGHT;

  /** define if description in spinner items must be translated; default value: <code>true</code> */
  private boolean translateItemDescriptions = true;

  /** background color when the input control is enabled */
  private Color enabledBackColor = new JTextField().getBackground();


  /**
   * Contructor.
   */
  public SpinnerListControl() {
    this.setLayout(new GridBagLayout());
    this.add(spinner, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    setOpaque(false);
    spinner.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_CANCEL || e.getKeyCode()==e.VK_BACK_SPACE || e.getKeyCode()==e.VK_DELETE)
          spinner.setValue(null);
      }
    });
    setColumns(columns);
    setHorizontalAlignment(horizontalAlignment);
    spinner.addChangeListener(this);

    initListeners();
  }


  /**
   * Return the formatted text field used by the editor, or
   * null if the editor doesn't descend from JSpinner.DefaultEditor.
   */
  public JFormattedTextField getTextField(JSpinner spinner) {
      JComponent editor = spinner.getEditor();
      if (editor instanceof JSpinner.DefaultEditor) {
          return ((JSpinner.DefaultEditor)editor).getTextField();
      } else {
        Logger.error(this.getClass().getName(),"getTextField","Unexpected editor type: "+spinner.getEditor().getClass()+" isn't a descendant of DefaultEditor",null);
          return null;
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
   * Set the specified domain and retrieve the spinner items from the domain.
   * @param domain domain to use to retrieve the spinner items
   */
  public final void setDomain(Domain domain) {
    this.domain = domain;
    if (domain!=null) {
      ArrayList list = new ArrayList();
      DomainPair[] pairs = domain.getDomainPairList();
      for(int i=0;i<pairs.length;i++)
        if (translateItemDescriptions)
          list.add(ClientSettings.getInstance().getResources().getResource(pairs[i].getDescription()));
        else
          list.add(pairs[i].getDescription());
      model.setList(list);
      spinner.setModel(model);
    }
  }


  /**
   * @return domain identifier
   */
  public final String getDomainId() {
    return domainId;
  }


  /**
   * Select the spinner item related to the specified code.
   * @param code used to retrieve the corresponding item and to select that item in the spinner
   */
  public final void setValue(Object code) {
    try {
      if (code == null) {
        spinner.setValue(null);
      }
    }
    catch (Exception ex) {
    }
    if (domain==null)
      return;
    DomainPair pair = domain.getDomainPair(code);
    if (pair!=null) {
      if (translateItemDescriptions)
        spinner.setValue( ClientSettings.getInstance().getResources().getResource(pair.getDescription()) );
      else
        spinner.setValue( pair.getDescription() );
    }
  }


  /**
   * @return code related to the selected spinner item; it return null if no item is selected
   */
  public final Object getValue() {
    if (domain==null)
      return null;
    DomainPair[] pairs = domain.getDomainPairList();
    for(int i=0;i<pairs.length;i++)
      if (spinner.getValue()==null)
        return null;
      else {
        if (translateItemDescriptions) {
          if (spinner.getValue().equals( ClientSettings.getInstance().getResources().getResource(pairs[i].getDescription()) ))
            return pairs[i].getCode();
        }
        else {
          if (spinner.getValue().equals( pairs[i].getDescription()) )
            return pairs[i].getCode();
        }
      }
    return null;
  }


  public void setEnabled(boolean enabled) {
    spinner.setEnabled(enabled);
    spinner.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
    if (!enabled) {
      spinner.setBackground((Color)UIManager.get("TextField.inactiveBackground"));
      ftf.setBackground((Color)UIManager.get("TextField.inactiveBackground"));
    }
    else {
      ftf.setBackground(enabledBackColor);
    }
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      return spinner.isEnabled();
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * @return component inside this whose contains the value
   */
  public JComponent getBindingComponent() {
    return spinner;
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
        if(spinner != null) spinner.addFocusListener(l);
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
      spinner.removeFocusListener(l);
    }
    catch (Exception ex) {
    }
  }


  public final void stateChanged(ChangeEvent e) {
  }


  /**
   * Adds a listener to the list that is notified each time a change
   * to the model occurs.  The source of <code>ChangeEvents</code>
   * delivered to <code>ChangeListeners</code> will be this
   * <code>JSpinner</code>.  Note also that replacing the model
   * will not affect listeners added directly to JSpinner.
   * Applications can add listeners to  the model directly.  In that
   * case is that the source of the event would be the
   * <code>SpinnerModel</code>.
   *
   * @param listener the <code>ChangeListener</code> to add
   * @see #removeChangeListener
   * @see #getModel
   */
  public void addChangeListener(ChangeListener listener) {
    try {
      spinner.addChangeListener(listener);
    }
    catch (Exception ex) {
    }
  }



  /**
   * Removes a <code>ChangeListener</code> from this spinner.
   *
   * @param listener the <code>ChangeListener</code> to remove
   * @see #fireStateChanged
   * @see #addChangeListener
   */
  public void removeChangeListener(ChangeListener listener) {
    try {
      spinner.removeChangeListener(listener);
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return domain to use to retrieve the spinner items
   */
  public final Domain getDomain() {
    return domain;
  }


  /**
   * @return spinner control
   */
  public final JComponent getComponent() {
    return spinner;
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
   * @return define if description in spinner items must be translated
   */
  public final boolean isTranslateItemDescriptions() {
    return translateItemDescriptions;
  }


  /**
   * Define if description in spinner items must be translated; default value: <code>true</code>.
   * @param translateItemDescriptions flag used to define if description in spinner items must be translated
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
    spinner.setComponentOrientation(o);
  }


  /**
   * @return component orientation
   */
  public final ComponentOrientation getTextOrientation() {
    try {
      return spinner.getComponentOrientation();
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * @return number of visible characters
   */
  public final int getColumns() {
    return columns;
  }


  /**
   * Set the number of visible characters.
   * @param columns number of visible characters
   */
  public final void setColumns(int columns) {
    this.columns = columns;
    ftf.setColumns(columns);
  }


  /**
   * Set horizontal alignment.
   * @param horizontalAlignment horizontal alignment
   */
  public final void setHorizontalAlignment(int horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
    ftf.setHorizontalAlignment(horizontalAlignment);
  }


  /**
   * @return horizontal alignment
   */
  public final int getHorizontalAlignment() {
    return horizontalAlignment;
  }


  /**
   * @return the object in the sequence that comes after the object returned by getValue()
   */
  public final Object getNextValue() {
    return spinner.getNextValue();
  }


  /**
   * @return the object in the sequence that comes before the object returned by getValue()
   */
  public final Object getPreviousValue() {
    return spinner.getPreviousValue();
  }



}
