package org.openswing.swing.client;


import java.beans.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.form.client.*;
import org.openswing.swing.form.model.client.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup input Control used to digit a code. A code can be an alphanumeric or a numeric value.
 * When focus is lost from this control, a code validation is executed (only if the specified code is changed).
 * It contains also a lookup button, i.e. a button which allows to view a lookup grid of codes:
 * the user can select a code from the lookup grid and this code will be set in the code input field.
 * It optionally contains also a "+" button, used to call a controller class, related to the code registry,
 * i.e. the registry that allows to insert/update/delete codes referred by the lookup.
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
public class CodLookupControl extends BaseInputControl implements CodBoxContainer,InputControl,LookupParent,AutoCompletitionInputControl {

  /** separator between code input field and lookup button */
  private Component buttonSeparator = javax.swing.Box.createHorizontalStrut(5);

  /** flag used to set if code field is editable */
  private boolean enableCodBox = true;

  /** lookup button */
  private JButton lookupButton = new JButton() {
    public void paint(Graphics g) {
      super.paint(g);
      int width = g.getFontMetrics().stringWidth("...");
      if (isEnabled())
        g.setColor(UIManager.getColor("Button.foreground"));
      else
        g.setColor(UIManager.getColor("Button.disabledForeground"));
      g.drawString("...", (this.getWidth()-width+1)/2, this.getHeight()/2+4);
    }
  };

  /** code input field */
  private CodBox codBox = new CodBox();

  /** lookup controller: this class will execute the code validation and the lookup grid opening */
  private LookupController validationController = null;

  /** class name of the controller that must be invoked by pressing the "+" button */
  private String controllerClassName = null;

  /** method name defined in ClientFacade class, related to the controller that must be invoked by pressing the "+" button */
  private String controllerMethodName = null;

  /** "+" button, used to call the controller class, related to the code registry */
  private JButton plusButton = new JButton() {
    public void paint(Graphics g) {
      super.paint(g);
      int width = g.getFontMetrics().stringWidth("...");
      g.drawString("+", (this.getWidth()-width+1)/2, this.getHeight()/2+4);
    }
  };

  /** wait time (expressed in ms) before showing code auto completition feature for lookup controls; default value: ClientSettings.LOOKUP_AUTO_COMPLETITION_WAIT_TIME */
  private long autoCompletitionWaitTime = ClientSettings.LOOKUP_AUTO_COMPLETITION_WAIT_TIME;

  /** used in addNotify method */
  private boolean firstTime = true;

  private AutoCompletitionListener autoCompletitionListener = null;


  /**
   * Costructor.
   */
  public CodLookupControl() {
    codBox.setContainer(this);
    setColumns(10);

    lookupButton.setText(null);
    lookupButton.setPreferredSize(new Dimension(21, codBox.getPreferredSize().height));
    ClientUtils.addTabListener(lookupButton);
    lookupButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        if (validationController!=null)
          validationController.openLookupFrame(lookupButton,CodLookupControl.this);
      }

    });

    plusButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        if (controllerClassName!=null) {
          try {
            Class.forName(controllerClassName).newInstance();
          }
          catch (Throwable ex) {
            ex.printStackTrace();
          }
        }
        else if (controllerMethodName!=null) {
          try {
            MDIFrame.getClientFacade().getClass().getMethod(controllerMethodName,new Class[0]).invoke(MDIFrame.getClientFacade(), new Object[0]);
          }
          catch (Throwable ex) {
            ex.printStackTrace();
          }
        }
      }

    });

    this.setLayout(new GridBagLayout());
    this.add(codBox, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(buttonSeparator, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(lookupButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

    lookupButton.setPreferredSize(new Dimension(lookupButton.getPreferredSize().width,codBox.getPreferredSize().height));
    lookupButton.setMaximumSize(new Dimension(lookupButton.getPreferredSize().width,codBox.getPreferredSize().height));
    lookupButton.setMinimumSize(new Dimension(lookupButton.getPreferredSize().width,codBox.getPreferredSize().height));

    plusButton.setPreferredSize(new Dimension(lookupButton.getPreferredSize().width,codBox.getPreferredSize().height));
    plusButton.setMaximumSize(new Dimension(lookupButton.getPreferredSize().width,codBox.getPreferredSize().height));
    plusButton.setMinimumSize(new Dimension(lookupButton.getPreferredSize().width,codBox.getPreferredSize().height));

//    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//    this.add(codBox);
//    this.add(buttonSeparator);
//    this.add(lookupButton);

    if (ClientSettings.TEXT_ORIENTATION!=null)
        setComponentOrientation(ClientSettings.TEXT_ORIENTATION);


    initListeners();
  }


  /**
   * @return code input field
   */
  public JComponent getBindingComponent() {
    return codBox;
  }


  /**
   * @return code value
   */
  public final Object getValue() {
    return codBox.getValue();
  }


  /**
   * Select the combo item related to the specified code.
   * @param code used to retrieve the corresponding item and to select that item in the combo
   */
  public final void setValue(Object code) {
    codBox.setValue(code);
  }


  /**
   * @return maximum code length
   */
  public final int getMaxCharacters() {
    return codBox.getMaxCharacters();
  }


  /**
   * Set the maximum code length.
   * @param maxCharacters maximum code length
   */
  public final void setMaxCharacters(int maxCharacters) {
    this.codBox.setMaxCharacters(maxCharacters);
  }


  /**
   * @return code input field visibility
   */
  public final boolean isCodBoxVisible() {
    return codBox.isVisible();
  }


  /**
   * Set code input field visibility.
   * @param codBoxVisible code input field visibility
   */
  public final void setCodBoxVisible(boolean codBoxVisible) {
    this.codBox.setVisible(codBoxVisible);
    if (!codBoxVisible)
      setColumns(0);
  }


  /**
   * @return code input field columns
   */
  public final int getColumns() {
    return codBox.getColumns();
  }


  /**
   * Set code input field columns.
   * @param columns code input field columns
   */
  public final void setColumns(int columns) {
    this.codBox.setColumns(columns);

    StringBuffer s = new StringBuffer(columns); for(int i=0;i<columns;i++) s.append("W");
    int w =
        getFontMetrics(getFont()).stringWidth(s.toString())+
        lookupButton.getPreferredSize().width+
        buttonSeparator.getPreferredSize().width+10;
    if (controllerMethodName!=null)
      w += plusButton.getPreferredSize().width;

    int h = new JButton("A").getPreferredSize().height;

    setMinimumSize(new Dimension(w,h));
    setPreferredSize(new Dimension(w,h));
  }


  /**
   * @return lookup controller
   */
  public final LookupController getLookupController() {
    return validationController;
  }


  /**
   * Method that listen validation code event fired by the code input field: it validates the code.
   * @param code code to validate
   */
  public final void validateCode(String code) throws RestoreFocusOnInvalidCodeException {
    if (autoCompletitionWaitTime>=0 &&
        autoCompletitionListener!=null &&
        autoCompletitionListener.isWindowVisible()) {
      codBox.setText(null);
      return;
    }

    if (validationController!=null)
      validationController.validateCode(
          codBox,
          code,
          this
      );
  }


  public final void setEnabled(boolean enabled) {
    if (enableCodBox)
      this.codBox.setEditable(enabled);
    else
      this.codBox.setEditable(false);
//    this.codBox.setEnabled(enabled);
    this.lookupButton.setEnabled(enabled);
    this.codBox.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
    this.lookupButton.setFocusable(enabled);
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      return this.codBox.isEditable();
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * @return code input field
   */
  public final CodBox getCodBox() {
    return codBox;
  }


  /**
   * Set lookup controller.
   * This method calls also addLookupListener method.
   * This method calls also setForm methos of the LookupController.
   * @param controller lookup controller.
   */
  public final void setLookupController(LookupController controller) {
    this.validationController = controller;
    addLookupListener(controller);

    // set Form object in the lookup container...
    Form form = ClientUtils.getLinkedForm(this);
    if (form!=null)
      controller.setForm(form);
  }


  /**
   * Check if Form has been setted.
   */
  public final void addNotify() {
    super.addNotify();

    if (firstTime) {
      firstTime = false;

      // set Form object in the lookup container...
      if (!Beans.isDesignTime() && validationController!=null && validationController.getForm()==null) {
        Form form = ClientUtils.getLinkedForm(this);
        if (form != null)
          validationController.setForm(form);
      }

      if (!Beans.isDesignTime() && autoCompletitionWaitTime>=0 && autoCompletitionListener==null && getAttributeName()!=null) {
        autoCompletitionListener =
            new AutoCompletitionListener(
              this,
              new LookupAutoCompletitionDataLocator(
                validationController,
                getAttributeName()
              ),
              autoCompletitionWaitTime
            );

        codBox.addKeyListener(autoCompletitionListener);
      }


      codBox.addKeyListener(new KeyAdapter() {
        public void keyReleased(KeyEvent e) {
          if (e.getKeyCode()==ClientSettings.LOOKUP_OPEN_KEY.getKeyCode() &&
              e.getModifiers()+e.getModifiersEx()==ClientSettings.LOOKUP_OPEN_KEY.getModifiers()) {
            if (validationController!=null)
              validationController.openLookupFrame(codBox,CodLookupControl.this);
          }
          if (e.getKeyCode()==ClientSettings.LOOKUP_CONTROLLER_KEY.getKeyCode() &&
              e.getModifiers()+e.getModifiersEx()==ClientSettings.LOOKUP_CONTROLLER_KEY.getModifiers()) {
            if (controllerClassName!=null) {
              try {
                Class.forName(controllerClassName).newInstance();
              }
              catch (Throwable ex) {
                ex.printStackTrace();
              }
            }
            else if (controllerMethodName!=null) {
              try {
                MDIFrame.getClientFacade().getClass().getMethod(controllerMethodName,new Class[0]).invoke(MDIFrame.getClientFacade(), new Object[0]);
              }
              catch (Throwable ex) {
                ex.printStackTrace();
              }
            }
          }
        }
      });

    } // end if
  }


  private final void addLookupListener(LookupController controller) {
    // lookup controller listener...
    controller.addLookupListener(new LookupListener() {

      /**
       * This method is called before code validation and when pressing lookup button.
       */
      public void beforeLookupAction(ValueObject parentVO) {}


      /**
       * Callback called on code validation.
       * @param validated validation result
       */
      public void codeValidated(boolean validated) {
        codBox.codeValidated(validated);
      }


      /**
       * Callback called when code is changed.
       * @param parentVO v.o. of the parent lookup container
       * @param parentChangedAttributes changed attributes of the v.o. of the parent lookup container
       */
      public void codeChanged(ValueObject parentVO, Collection parentChangedAttributes) {
        try {
          String attrName;
          Iterator list = parentChangedAttributes.iterator();
          Object newValue = null;
          while (list.hasNext()) {
            attrName = (String) list.next();

            if (parentVO!=null) {
              String aux = attrName;
              Object obj = parentVO;
              while(aux.indexOf(".")!=-1) {
                obj = ClientUtils.getPropertyDescriptor(obj.getClass(),aux.substring(0,aux.indexOf("."))).getReadMethod().invoke(obj,new Object[0]);
                aux = aux.substring(aux.indexOf(".")+1);
              }
              if (obj==null)
                newValue = null;
              else if (ClientUtils.getPropertyDescriptor(obj.getClass(),aux)==null)
                newValue = null;
              else
                newValue = ClientUtils.getPropertyDescriptor(obj.getClass(),aux).getReadMethod().invoke(obj,new Object[0]);
            }
            else
              newValue = null;

            ValueChangeEvent e = new ValueChangeEvent(this, attrName, null, newValue);
            ValueChangeListener[] listeners = getValueChangeListeners();
            for (int i = 0; i < listeners.length; i++)
              listeners[i].valueChanged(e);

          }
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        catch (Error er) {
          er.printStackTrace();
        }
      }


      /**
       * Force the code validation.
       */
      public void forceValidate() {
        codBox.forceValidate();
      }

    });

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
  public final void addFocusListener(FocusListener listener) {
    try {
      codBox.addFocusListener(listener);
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
  public final void removeFocusListener(FocusListener listener) {
    try {
      codBox.removeFocusListener(listener);
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
  public final void addActionListener(ActionListener listener) {
    try {
      codBox.addActionListener(listener);
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
  public final void removeActionListener(ActionListener listener) {
    try {
      codBox.removeActionListener(listener);
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return define if the cod box allows numeric values only
   */
  public final boolean isAllowOnlyNumbers() {
    return codBox.isAllowOnlyNumbers();
  }


  /**
   * Define if the cod box allows numeric values only.
   * @param allowOnlyNumbers define if the cod box allows numeric values only
   */
  public final void setAllowOnlyNumbers(boolean allowOnlyNumbers) {
    codBox.setAllowOnlyNumbers(allowOnlyNumbers);
  }


  /**
   * Set if code field is editable.
   * @param enableCodBox code field is editable
   */
  public void setEnableCodBox(boolean enableCodBox) {
    this.enableCodBox = enableCodBox;
    if (!enableCodBox)
      codBox.setEnabled(enableCodBox);
  }


  /**
   * @return code field is editable
   */
  public boolean isEnableCodBox() {
    return enableCodBox;
  }


  /**
   * Method called by LookupController to update parent v.o.
   * @param attributeName attribute name in the parent v.o. that must be updated
   * @param value updated value
   */
  public void setValue(String attributeName,Object value) {
    if (validationController.getForm()!=null)
      validationController.getForm().getVOModel().setValue(attributeName,value);
  }


  /**
   * @return parent value object
   */
  public ValueObject getValueObject() {
    if (validationController.getForm()==null)
      return null;
    if (validationController.getForm().getVOModel()==null)
      return null;
    return validationController.getForm().getVOModel().getValueObject();
  }


  /**
   * @return attribute name in the parent value object related to lookup code
   */
  public Object getLookupCodeParentValue() {
    if (validationController.getForm()!=null && validationController.getForm().getVOModel()!=null && getAttributeName()!=null)
      return validationController.getForm().getVOModel().getValue(getAttributeName());
    return "";
  }


  /**
   * @return class name of the controller that must be invoked by pressing the "+" button
   */
  public final String getControllerClassName() {
    return controllerClassName;
  }


  /**
   * @return method name defined in ClientFacade class, related to the controller that must be invoked by pressing the "+" button
   */
  public final String getControllerMethodName() {
    return controllerMethodName;
  }


  /**
   * Set the class name of the controller that must be invoked by pressing the "+" button.
   * @param controllerClassName class name of the controller that must be invoked by pressing the "+" button
   */
  public final void setControllerClassName(String controllerClassName) {
    this.controllerClassName = controllerClassName;
    if (controllerMethodName!=null)
      this.add(plusButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
  }


  /**
   * Set the method name defined in ClientFacade class, related to the controller that must be invoked by pressing the "+" button.
   * @param controllerMethodName method name defined in ClientFacade class, related to the controller that must be invoked by pressing the "+" button
   */
  public final void setControllerMethodName(String controllerMethodName) {
    this.controllerMethodName = controllerMethodName;
    if (controllerMethodName!=null)
      this.add(plusButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
  }


  /**
   * @return wait time (expressed in ms) before showing code auto completition feature for lookup controls; <code>-1</code>, to do not enable auto completition
   */
  public final long getAutoCompletitionWaitTime() {
    return autoCompletitionWaitTime;
  }


  /**
   * Wait time before showing code auto completition feature for this lookup control.
   * @param autoCompletitionWaitTime wait time (expressed in ms) before showing code auto completition feature for this lookup control; default value: <code>-1</code> to do not enable auto completition
   */
  public final void setAutoCompletitionWaitTime(long autoCompletitionWaitTime) {
    this.autoCompletitionWaitTime = autoCompletitionWaitTime;
  }


  /**
   * @return lookup button visibility
   */
  public final boolean isLookupButtonVisible() {
    if (lookupButton!=null)
      return lookupButton.isVisible();
    else
      return true;
  }


  /**
   * Set lookup button visibility.
   * @param lookup button visibility
   */
  public final void setLookupButtonVisible(boolean lookupButtonVisible) {
    if (lookupButton!=null)
      this.lookupButton.setVisible(lookupButtonVisible);
  }


  /**
   * Set the component orientation: from left to right or from right to left.
   * @param o component orientation
   */
  public final void setTextOrientation(ComponentOrientation o) {
    codBox.setComponentOrientation(o);
  }


  /**
   * @return component orientation
   */
  public final ComponentOrientation getTextOrientation() {
    try {
      return codBox.getComponentOrientation();
    }
    catch (Exception ex) {
      return null;
    }
  }


}
