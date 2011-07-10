package org.openswing.swing.table.editors.client;

import java.math.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.openswing.swing.lookup.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.client.AutoCompletitionListener;
import org.openswing.swing.client.InputControl;
import org.openswing.swing.client.LabelControl;
import org.openswing.swing.form.model.client.ValueChangeListener;
import org.openswing.swing.client.AutoCompletitionInputControl;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used to edit a lookup control.</p>
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
public class CodLookupCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener, LookupParent {

  /** lookup button */
  private JButton lookupButton = new JButton("...");

  /** "+" button, used to call the controller class, related to the code registry */
  private JButton plusButton = new JButton() {
    public void paint(Graphics g) {
      super.paint(g);
      int width = g.getFontMetrics().stringWidth("+");
      g.drawString("+", (this.getWidth()-width+1)/2, this.getHeight()/2+4);
    }
  };

  /** code input field */
  private TextBox codBox = new TextBox();

  /** value object associated to the lookup grid */
  private ValueObject gridValueObject = null;

  /** cell content: code input field and lookup button */
  private JPanel panel = new JPanel() {

    private KeyEvent oldEv = null;

    public boolean processKeyBinding(KeyStroke ks, KeyEvent e,
                                        int condition, boolean pressed) {
      if (e.getSource()!=null && e.getSource() instanceof org.openswing.swing.table.client.Grid) {
        try {
          if (oldEv==null || !e.equals(oldEv)) {
            oldEv = e;
            codBox.processKeyEvent(e);
            oldEv = null;
          }
        }
        catch (Exception ex) {
        }
      }
      else if (e.getKeyChar()=='\t' || e.getKeyChar()=='\n') {
        stopCellEditing();
        try {
          table.setColumnSelectionInterval(table.getSelectedColumn() + 1, table.getSelectedColumn() + 1);
        }
        catch (Exception ex) {
        }
      }
      return true;
    }

  };

  /** lookup controller */
  private LookupController lookupController = null;

  /** flag used to set mandatory property of the cell */
  private boolean required;

  /** grid (lookup container) reference, used to update table model after code validation/selection */
  private Grid table = null;

  /** current selected row on grid */
  private int selectedRow = -1;

  /** current selected column on grid */
  private int selectedCol = -1;

  /** attribute name linked to the code */
  private String codAttributeName = null;

  /** allow number only on code input field */
  private boolean numericValue;

  /** flag used to skip code validation if code isn't changed */
  private boolean alreadyValidated = false;

  /** flag used to disable code editability */
  private boolean codBoxEditable = false;

  /** flag used to hide code */
  private boolean codBoxVisible = false;

  /** last code value stored in the parent value object */
  private Object lastCodeValue = null;

  /** flag used in grid to automatically select data in cell when editing cell; default value: ClientSettings.SELECT_DATA_IN_EDIT; <code>false</code>to do not select data stored cell; <code>true</code> to automatically select data already stored in cell */
  private boolean selectDataOnEdit = ClientSettings.SELECT_DATA_IN_EDITABLE_GRID;

  /** flag used in getCellEditorValue to return the correct value */
  private boolean getTableCellEditorComponentInvoked = false;


  /**
   * Constructor.
   * @param maxCharacters maximum number of characters
   * @param lookupController lookup controller
   * @param required flag used to set mandatory property of the cell
   * @param numericValue allow number only on code input field
   * @param codBoxVisible flag used to hide code
   * @param codBoxEditable flag used to disable code editability
   * @param buttonVisible flag used to hide button
   * @param controllerClassName class name of the controller that must be invoked by pressing the "+" button
   * @param controllerMethodName method name defined in ClientFacade class, related to the controller that must be invoked by pressing the "+" button
   * @param autoCompletitionWaitTime wait time (expressed in ms) before showing code auto completition feature for lookup controls; default value: ClientSettings.LOOKUP_AUTO_COMPLETITION_WAIT_TIME
   * @param codAttributeName attribute name linked to the code
   */
  public CodLookupCellEditor(
      int maxCharacters,
      LookupController lookupController,
      boolean required,
      boolean numericValue,
      boolean codBoxVisible,
      boolean codBoxEditable,
      boolean buttonVisible,
      final String controllerClassName,
      final String controllerMethodName,
      final long autoCompletitionWaitTime,
      String codAttributeName,
      ComponentOrientation orientation,
      boolean selectDataOnEdit) {
    this.lookupController = lookupController;
    this.required = required;
    this.numericValue = numericValue;
    this.codBoxVisible = codBoxVisible;
    this.codBoxEditable = codBoxEditable;
    this.codAttributeName = codAttributeName;
    this.selectDataOnEdit = selectDataOnEdit;
    codBox.setColumns(maxCharacters);
    codBox.setEnabled(codBoxEditable);

    if (orientation!=null)
      codBox.setComponentOrientation(orientation);

    panel.setLayout(new GridBagLayout());
    if (codBoxVisible) {
      panel.add(codBox,      new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
      if (buttonVisible) {
        lookupButton.setMinimumSize(new Dimension(20,panel.getPreferredSize().height));
        panel.add(lookupButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
              , GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 0, 0), 0, 0));
      }

      if (controllerMethodName!=null && !controllerMethodName.equals("")) {
        plusButton.setMinimumSize(new Dimension(16,panel.getPreferredSize().height));
        panel.add(plusButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
              , GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 0, 0), 0, 0));
      }

    }
    else {
      if (buttonVisible) {
      //      lookupButton.setMinimumSize(new Dimension(panel.getPreferredSize().width-2,panel.getPreferredSize().height));
        panel.add(lookupButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
              new Insets(0, 0, 0, 0), 0, 0));
      }


      if (controllerMethodName!=null || controllerMethodName!=null) {
        plusButton.setMinimumSize(new Dimension(16,panel.getPreferredSize().height));
        panel.add(plusButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              , GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 0, 0), 0, 0));
      }

    }
    lookupButton.addActionListener(this);


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

    if (autoCompletitionWaitTime>=0 && codAttributeName!=null) {
      codBox.addKeyListener(
        new AutoCompletitionListener(
          new VirtualInputControl(),
          new LookupAutoCompletitionDataLocator(
            lookupController,
            codAttributeName
          ),
          autoCompletitionWaitTime
        )
      );
    }


    codBox.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==ClientSettings.LOOKUP_OPEN_KEY.getKeyCode() &&
            e.getModifiers()+e.getModifiersEx()==ClientSettings.LOOKUP_OPEN_KEY.getModifiers()) {
          actionPerformed(null);
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


    codBox.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) { alreadyValidated=false; }
      public void insertUpdate(DocumentEvent e) { alreadyValidated=false; }
      public void removeUpdate(DocumentEvent e) { alreadyValidated=false; }
    });

    lookupController.addLookupListener(new LookupListener() {

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        if (CodLookupCellEditor.this.codAttributeName==null)
          return;

        try {
          String attrName;
          Object newValue = null;
          attrName = CodLookupCellEditor.this.codAttributeName;
          if (parentVO != null) {
            String aux = attrName;
            Object obj = parentVO;
            // check if there exists a "." in the attribute name definition for the lookup code:
            // in this case retrieve the lookup code value by extracting it from the inner vos...
            while ( aux.indexOf(".") != -1) {
              obj = ClientUtils.getPropertyDescriptor(obj.getClass(), aux.substring(0, aux.indexOf("."))).getReadMethod().invoke(obj, new Object[0]);
              aux = aux.substring(aux.indexOf(".") + 1);
            }
            newValue = ClientUtils.getPropertyDescriptor(obj.getClass(), aux).getReadMethod().invoke(obj, new Object[0]);
          } else {
            newValue = null;
          }

          if (newValue == null) {
              codBox.setText("");
          } else {
              codBox.setText(newValue.toString());
          }
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        finally {
          Object c = ClientUtils.getParentWindow(table);
          if (c!=null && c instanceof MDIFrame) {
            table.requestFocus();
          }
        }

//        String getter = "get"+codAttributeName.substring(0,1).toUpperCase();
//        if (codAttributeName.length()>1)
//          try {
//          getter += codAttributeName.substring(1);
//          Object codValue = parentVO.getClass().getMethod(getter, new Class[0]).invoke(parentVO, new Object[0]);
//          if (codValue==null)
//            codBox.setText("");
//          else
//            codBox.setText(codValue.toString());
//          alreadyValidated = true;
//        }
//        catch (Exception ex) {
//          ex.printStackTrace();
//        }
      }

      public void codeValidated(boolean validated) {}
      public void beforeLookupAction(ValueObject parentVO) {}
      public void forceValidate() {}

    });

    codBox.addKeyListener(new KeyAdapter() {

      public void keyPressed(KeyEvent e) {
        if (table!=null && (e.getKeyCode()==e.VK_UP || e.getKeyCode()==e.VK_DOWN) && autoCompletitionWaitTime<0) {
          stopCellEditing();
          if (table!=null) {
            new Thread() {
              public void run() {
                yield();
                try {
                  sleep(500);
                }
                catch (InterruptedException ex) {
                }
                table.requestFocus();
//                table.setColumnSelectionInterval(grid.g,selectedCol);
              }
            }.start();
          }
          table.dispatchEvent(e);
        }
        else if (e.getKeyCode()==e.VK_TAB || e.getKeyCode()==e.VK_ENTER) {

          // used to validate editable codes...
          if (CodLookupCellEditor.this.codBoxEditable && codBox.getText()!=null)
            codBox.setText(codBox.getText().toUpperCase().trim());

          stopCellEditing();

          if (CodLookupCellEditor.this.lookupController!=null &&
              CodLookupCellEditor.this.lookupController.getOnInvalidCode()==CodLookupCellEditor.this.lookupController.ON_INVALID_CODE_RESTORE_FOCUS &&
             !CodLookupCellEditor.this.lookupController.isCodeValid()) {
            e.consume();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                if (!table.hasFocus())
                  table.requestFocus();
                table.editCellAt(selectedRow,selectedCol);
              }
            });
            return;
          }

          if (table!=null) {
            new Thread() {
              public void run() {
                yield();
                try {
                  sleep(500);
                }
                catch (InterruptedException ex) {
                }
                table.requestFocus();
//                table.setColumnSelectionInterval(grid.g,selectedCol);
              }
            }.start();
          }
        }
      }

    });
  }


  /**
   * Method called when user clicks the lookup button.
   */
  public final void actionPerformed(ActionEvent e) {
    if (lookupController!=null) {
      lookupController.openLookupFrame(ClientUtils.getParentWindow(table),this);

      // now the parent v.o. (i.e. gridValueObject) has the code attributed filled:
      // set codBox content with that code value...
//      String lookupMethodName = "get" + String.valueOf(Character.toUpperCase(codAttributeName.charAt(0))) + codAttributeName.substring(1);
      int colIndex = this.table.getVOListTableModel().findColumn(codAttributeName);
      Object value = this.table.getVOListTableModel().getValueAt(selectedRow,colIndex);
      codBox.setText(value==null?null:value.toString());

      fireEditingStopped();
//      stopCellEditing();
      if (table!=null) {
        table.repaint();
        if (!table.hasFocus())
          table.requestFocus();
      }
    }
  }


  /**
   * Stop cell editing. This method stops cell editing (effectively committing the edit) only if the data entered is validated successfully.
   * @return <code>true</code> if cell editing may stop, and <code>false</code> otherwise.
   */
  public final boolean stopCellEditing() {
    return validate();
  }


  /**
   * Perform the validation.
   */
  public final boolean validate() {
    fireEditingStopped();
//    if (!alreadyValidated && lookupController!=null && codBox.getText()!=null && !codBox.getText().trim().equals(""))
    if (!alreadyValidated &&
        lookupController != null &&
        codBox.getText() != null &&
        codBox.isEnabled())
      try {
        if (lastCodeValue==null || lastCodeValue!=null && !lastCodeValue.toString().toUpperCase().equals(codBox.getText().toUpperCase()))
          lookupController.validateCode(table, codBox.getText().toUpperCase(), this);
      }
      catch (RestoreFocusOnInvalidCodeException ex) {
        final int row = selectedRow;
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            table.editCellAt(row,table.getColumnIndex(codAttributeName));
            if (!codBox.hasFocus())
              codBox.requestFocus();
          }
        });
      }

    alreadyValidated = true;

    if (table!=null)
      table.repaint();
    return true;
  }


  /**
   * Method called by LookupController to update parent v.o.
   * @param attributeName attribute name in the parent v.o. that must be updated
   * @param value updated value
   */
  public void setValue(String attributeName,Object value) {
    if (selectedRow!=-1)
      table.getVOListTableModel().setValueAt(value,selectedRow,table.getColumnIndex(attributeName));
  }


  /**
   * @return parent value object
   */
  public ValueObject getValueObject() {
    if (selectedRow!=-1)
      return table.getVOListTableModel().getObjectForRow(selectedRow);
    else
      return null;
  }


  /**
   * @return attribute name in the parent value object related to lookup code
   */
  public Object getLookupCodeParentValue() {
    return lastCodeValue;
  }


  public void cancelCellEditing() {
    super.cancelCellEditing();
    getTableCellEditorComponentInvoked = false;
  }


  public final Object getCellEditorValue() {
    lastCodeValue = table.getVOListTableModel().getValueAt(selectedRow,table.getColumnIndex(codAttributeName));
    if (!getTableCellEditorComponentInvoked)
      return lastCodeValue;

    getTableCellEditorComponentInvoked = false;

    if (numericValue) {
      if (codBox.getText().trim().length()==0)
        return null;
      return new BigDecimal(codBox.getText());
    }
    return codBox.getText();
  }


  /**
   * Prepare the editor for a value.
   */
  private final Component _prepareEditor(Object value) {
    codBox.setText(value==null?null:value.toString());

    if (selectDataOnEdit)
      codBox.select(0,codBox.getText().length());

    return panel;
  }


  public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {
    getTableCellEditorComponentInvoked = true;
    if (!codBoxVisible) {
      lookupButton.setMinimumSize(new Dimension(panel.getPreferredSize().width-5,panel.getPreferredSize().height-6));
      lookupButton.setMaximumSize(new Dimension(panel.getPreferredSize().width-5,panel.getPreferredSize().height-6));
    }
    this.table = ((Grid)table);
    gridValueObject = ((Grid)table).getVOListTableModel().getObjectForRow(row);

    // backup of the current value object...
    this.table.getVOListTableModel().setValueAt(value,row,table.convertColumnIndexToModel(column));

    JComponent c = (JComponent)_prepareEditor(value);
    this.codAttributeName = this.table.getVOListTableModel().getColumnName(table.convertColumnIndexToModel(column));
    this.selectedRow = row;
    this.selectedCol = column;
    if (required) {
      c.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_REQUIRED_CELL_BORDER));
//      codBox.setBorder(new CompoundBorder(new RequiredBorder(),c.getBorder()));
    }
    new Thread() {
      public void run() {
        yield();
        try {
          sleep(500);
        }
        catch (InterruptedException ex) {
        }
        codBox.requestFocus();
      }
    }.start();
    return c;
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Input control implementation used by AutoCompletitionListener to </p>
   * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
   * @version 1.0
   */
  class VirtualInputControl implements AutoCompletitionInputControl {


    /**
     * @return value related to the input control
     */
    public Object getValue() {
      return codBox.getText();
    }


    /**
     * Set value to the input control.
     * @param value value to set into the input control
     */
    public void setValue(Object value) {
      codBox.setText(value==null?null:value.toString());
    }


    /**
     * Registers <code>listener</code> so that it will receive
     * <code>AncestorEvents</code> when it or any of its ancestors
     * move or are made visible or invisible.
     * Events are also sent when the component or its ancestors are added
     * or removed from the containment hierarchy.
     *
     * @param listener  the <code>AncestorListener</code> to register
     * @see AncestorEvent
     */
    public void addAncestorListener(AncestorListener listener) {
      codBox.addAncestorListener(listener);
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
    public void addFocusListener(FocusListener l) {
      codBox.addFocusListener(l);
    }


    /**
     * Gets the location of this component in the form of a point
     * specifying the component's top-left corner in the screen's
     * coordinate space.
     * @return an instance of <code>Point</code> representing
     * 		the top-left corner of the component's bounds in the
     * 		coordinate space of the screen
     * @throws <code>IllegalComponentStateException</code> if the
     * 		component is not showing on the screen
     * @see #setLocation
     * @see #getLocation
     */
    public Point getLocationOnScreen() {
      return codBox.getLocationOnScreen();
    }


    /**
     * Returns the current height of this component.
     * This method is preferable to writing
     * <code>component.getBounds().height</code>, or
     * <code>component.getSize().height</code> because it doesn't cause any
     * heap allocations.
     *
     * @return the current height of this component
     */
    public int getHeight() {
      return codBox.getHeight();
    }


    /**
     * Returns the current width of this component.
     * This method is preferable to writing
     * <code>component.getBounds().width</code>, or
     * <code>component.getSize().width</code> because it doesn't cause any
     * heap allocations.
     *
     * @return the current width of this component
     */
    public int getWidth() {
      return codBox.getWidth();
    }



  } // end inner class


  /**
   * Method invoked by AutoCompletitionListener class.
   */
  public final void forceValidate() {
    lookupController.forceValidate();
  }


  public final void finalize() {
    table = null;
    lookupController = null;
  }


}




/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Inner class used to redirect key event to the inner JTextField.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */
class TextBox extends JTextField {

    public void processKeyEvent(KeyEvent e) {
      super.processKeyEvent(e);
    }

}


